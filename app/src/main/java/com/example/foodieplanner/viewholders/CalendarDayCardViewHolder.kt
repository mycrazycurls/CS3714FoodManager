package com.example.foodieplanner.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodieplanner.R
import com.example.foodieplanner.models.Day
import com.example.foodieplanner.models.Meal

class CalendarDayCardViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val day: TextView = view.findViewById(R.id.home_card_day)
    private val date: TextView = view.findViewById(R.id.home_card_date)
    private val meals: TextView = view.findViewById(R.id.home_card_meals)
    private val cals: TextView = view.findViewById(R.id.home_card_cals)
    private val price: TextView = view.findViewById(R.id.home_card_price)
    private val cardView: CardView = view.findViewById(R.id.home_card_view)


    fun bind(data: Day, onItemClicked: (pos: Int) -> Unit) {

        date.text = data.day?.date
        day.text = data.day?.day

        val numMeals = data.meals?.size
        meals.text = if (numMeals != null) "Meals: $numMeals" else ""

        val calories = totals(data.meals).second
        cals.text = if (calories != 0) "Calories: $calories" else ""

        val cost = totals(data.meals).first
        price.text = if (cost != 0.0) "Price: $cost" else ""

        itemView.setOnClickListener {
            onItemClicked(adapterPosition)
        }
    }

    private fun totals(meals: ArrayList<Meal>?): Pair<Double, Int> {
        var totalCals = 0
        var totalCost = 0.0
        if (meals != null) {
            for (meal in meals) {
                var cal = meal.calories
                var cost = meal.cost
                if (cal != null) {
                    totalCals += cal
                }
                if (cost != null) {
                    totalCost += cost
                }
            }
        }
        return Pair(totalCost,totalCals)
    }


    companion object {
        fun create(parent: ViewGroup): CalendarDayCardViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.card_home_calendar, parent, false)
                return CalendarDayCardViewHolder(view)
        }
    }
}