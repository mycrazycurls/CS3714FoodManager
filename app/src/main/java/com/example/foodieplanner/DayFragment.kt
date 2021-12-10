package com.example.foodieplanner

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.foodieplanner.databinding.FragmentDayBinding

class DayFragment : Fragment() {

    private lateinit var binding: FragmentDayBinding

    var month: String? = null
    var date: String? = null
    var day: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDayBinding.inflate(layoutInflater)

        month = arguments?.getString("month")
        date = arguments?.getString("date")
        day = arguments?.getString("day")

        binding.dayTopBar.title = day + ", " + month + " " + date

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
                    R.id.delete -> {
                        // Handle delete icon press
                        true
                    }
                    else -> false
                }
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
            }
        }

        val adapter = MealCardAdapter()
        binding.dayMealRecyclerView.adapter = adapter

        binding.dayTopBar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        binding.dayTopBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.day_toolbar_edit -> {
                    val actionMode = activity?.startActionMode(callback)
                    actionMode?.title = "1 selected"
                    true
                }
                else -> false
            }
        }

        binding.addMealsToDay.setOnClickListener {
            findNavController().navigate(R.id.action_dayFragment_to_pickMealsFragment,
                bundleOf("month" to month, "date" to date))
        }


        return binding.root
    }

    inner class MealCardAdapter: RecyclerView.Adapter<MealCardViewHolder>() {

        var data = arrayListOf<Pair<String,String>>(
            Pair("Eggs and Toast","200 cals"),
            Pair("Chicken and Rice","900 cals"),
            Pair("Pot Roast","1200 cals"),
            Pair("Apple Pie","500 cals")
        )
            set(value) {
                field = value
                notifyDataSetChanged()
            }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealCardViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.card_day_meal, parent, false)
            return MealCardViewHolder(view)
        }

        override fun onBindViewHolder(holder: MealCardViewHolder, position: Int) {
            holder.title.text = data.get(position).first
            holder.subtitle.text = data.get(position).second
        }

        override fun getItemCount(): Int {
            return data.size
        }

    }

    inner class MealCardViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.day_meal_title)
        val subtitle: TextView = view.findViewById(R.id.day_meal_subtitle)
    }
}