package com.example.foodieplanner

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodieplanner.models.Meal
import kotlin.random.Random

class PickMealsFragment : Fragment() {
    private val model: Model by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private val meals = ArrayList<Meal>()

    // Compiles all the meals selected by the user to be uploaded to the day
    private val selectedMeals = arrayListOf<Meal>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_pick_meals, container, false)

        recyclerView = view.findViewById(R.id.pick_meal_list)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        viewAdapter = MealAdapter(meals)
        recyclerView.adapter = viewAdapter

        //Load meals from Firebase to adapter for recycler
        model.savedMeals.observe(viewLifecycleOwner, Observer {
            it?.let {
                (viewAdapter as MealAdapter).setMeals(it)
            }
        })

        view.findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.pick_meals_album_toolbar).setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        view.findViewById<Button>(R.id.submit_meals_button).setOnClickListener {
            //model.addMealsForDay(model.meals_for_day, month!!, date!!)
            //model.addDay(model.meals_for_day, CalendarDay(month!!, day!!, date!!,timeInMillis!!))
            model.addMealsToDay(selectedMeals)
            activity?.onBackPressed()
        }

        return view
    }

    private fun toUnit(str: String): Unit {
        when (str) {
            "TSP" -> return Unit.TSP
            "TBSP" -> return Unit.TBSP
            "FLOZ" -> return Unit.FLOZ
            "CUP"-> return Unit.CUP
            "PINT:" -> return Unit.PINT
            "QUART" -> return Unit.QUART
            "GAL" -> return Unit.GAL
            "ML" -> return Unit.ML
            "LITER" -> return Unit.LITER
            "LB" -> return Unit.LB
            "OUNCE" -> return Unit.OUNCE
            "GRAM" -> return Unit.GRAM
            "MG" -> return Unit.MG
            "KG" -> return Unit.KG
            "NONE" -> return Unit.NONE
        }
        return Unit.NONE
    }

    inner class MealAdapter(private val mealList: ArrayList<Meal>):
        RecyclerView.Adapter<MealAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(
                R.layout.card_albums_meal,
                parent, false
            )
            return ViewHolder(v)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bindItems(mealList[position])
        }

        fun setMeals(meals: ArrayList<Meal>) {
            mealList.clear()
            mealList.addAll(meals)
            notifyDataSetChanged()
        }

        fun insertMeal(meal: Meal) {
            mealList.add(meal)
            notifyDataSetChanged()
        }

        private fun getRandomColor(): Int {
            val rand = Random.nextInt(0, 3)
            when (rand) {
                0 -> return Color.BLACK
                1 -> return Color.DKGRAY
                2 -> return Color.GRAY
                3 -> return Color.LTGRAY
            }
            return Color.BLACK
        }

        override fun getItemCount() = mealList.size

        inner class ViewHolder(private val view: View) :
            RecyclerView.ViewHolder(view) {
            fun bindItems(meal: Meal) {
                val title: TextView = itemView.findViewById(R.id.meal_card_title)
                val cardColor: Int = getRandomColor()
                title.setBackgroundColor(cardColor)
                title.text = meal.name

                val rating: TextView = itemView.findViewById(R.id.meal_card_rating)
                rating.text = meal.rating.toString()

                val image: ImageView = itemView.findViewById(R.id.album_card_image)
                image.setImageResource(R.drawable.default_pic)

                itemView.setOnClickListener {
                    //model.meals_for_day.add(meal)
                    selectedMeals.add(meal)
                    Toast.makeText(context, meal.name + " added!", Toast.LENGTH_SHORT)
                }
            }
        }
    }
}