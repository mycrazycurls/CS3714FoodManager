package com.example.foodieplanner

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.foodieplanner.databinding.FragmentDayBinding
import com.example.foodieplanner.models.CalendarDay
import com.example.foodieplanner.models.Meal
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class DayFragment : Fragment() {

    private lateinit var binding: FragmentDayBinding
    private val model: Model by activityViewModels()
    val adapter = MealCardAdapter()
    var actionMode: ActionMode? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDayBinding.inflate(layoutInflater)

        var selectedDay = model.selectedDay?.let { model.days.value?.get(it) }

        val datetime = selectedDay?.day
        binding.dayTopBar.title = "${datetime?.day}, ${datetime?.month} ${datetime?.date}"
        binding.dayHeaderCals.text = "Calories\n0"
        binding.dayHeaderCals.text = "Cost\n0"

        val callback = object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                activity?.menuInflater?.inflate(R.menu.contextual_action_bar, menu)
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                return when (item?.itemId) {
                    R.id.contextual_delete -> {
                        // Handle delete icon press
                        adapter.deleteSelected()
                        mode?.finish()
                        true
                    }
                    else -> false
                }
            }
            override fun onDestroyActionMode(mode: ActionMode?) {
                actionMode = null
                adapter.clearSelected()
            }
        }

        binding.dayMealRecyclerView.adapter = adapter

        binding.dayTopBar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        binding.dayTopBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.day_toolbar_edit -> {
                    when (actionMode) {
                        null -> {
                            actionMode = activity?.startActionMode(callback)
                            actionMode?.title = "0 Selected"
                            true
                        }
                        else -> false
                    }
                }
                else -> false
            }
        }

        binding.addMealsToDay.setOnClickListener {
            model.meals_for_day = adapter.data
            findNavController().navigate(R.id.action_dayFragment_to_pickMealsFragment)
        }

        // Pull meals
        val locationListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                adapter.clearMeals()
                for (snapShot in dataSnapshot.children) {
                    val meal = snapShot.getValue<Meal>()
                    if (meal != null) {
                        adapter.addMeal(meal)
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.e("MainRetrieveData", "loadPost:onCancelled", databaseError.toException())
            }
        }
        model.database.child("Days").child(model.selectedDay.toString()).child("meals").addValueEventListener(locationListener)

        return binding.root
    }

    inner class MealCardAdapter: RecyclerView.Adapter<MealCardViewHolder>() {

        var data = arrayListOf<Meal>()
        var selected = arrayListOf<Meal>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealCardViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.card_day_meal, parent, false)
            return MealCardViewHolder(view)
        }

        override fun onBindViewHolder(holder: MealCardViewHolder, position: Int) {
            holder.title.text = data.get(position).name
            holder.cost.text = "${data.get(position).cost}"
            holder.calories.text = "Calories: ${data.get(position).calories}"
            holder.view.setCardBackgroundColor(Color.WHITE)
            holder.view.isSelected = false

            holder.view.setOnClickListener {
                when (actionMode) {
                    null -> {

                    }
                    else -> {
                        holder.view.isSelected = !holder.view.isSelected
                        if (holder.view.isSelected) {
                            holder.view.setCardBackgroundColor(Color.LTGRAY)
                            selected.add(data.get(position))
                            actionMode?.title = "${selected.size} Selected"
                        }
                        else {
                            holder.view.setCardBackgroundColor(Color.WHITE)
                            selected.remove(data.get(position))
                            actionMode?.title = "${selected.size} Selected"
                        }
                    }
                }
            }
        }

        override fun getItemCount(): Int {
            return data.size
        }

        fun clearSelected() {
            selected.clear()
            notifyDataSetChanged()
        }

        fun deleteSelected() {
            model.removeMealsFromDay(selected)
        }

        fun addMeal(meal: Meal) {
            data.add(meal)
            binding.dayHeaderCals.text = "Calories\n${totals().second}"
            binding.dayHeaderCost.text = "Cost\n\$%.2f".format(totals().first)
            notifyDataSetChanged()
        }

        fun clearMeals() {
            data.clear()
            notifyDataSetChanged()
        }

        private fun totals(): Pair<Double, Int> {
            var totalCals = 0
            var totalCost = 0.0
            for (meal in data) {
                var cal = meal.calories
                var cost = meal.cost
                if (cal != null) {
                    totalCals += cal
                }
                if (cost != null) {
                    totalCost += cost
                }
            }
            return Pair(totalCost,totalCals)
        }


    }

    inner class MealCardViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.day_meal_title)
        val cost: TextView = view.findViewById(R.id.day_meal_cost)
        val calories: TextView = view.findViewById(R.id.day_meal_calories)
        val view: CardView = view.findViewById(R.id.day_card_view)
    }
}


