package com.example.foodieplanner.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodieplanner.R
import com.example.foodieplanner.models.Ingredient

class IngredientViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val text: TextView = view.findViewById(R.id.new_meal_ingredient_text)
    private val quantity: TextView = view.findViewById(R.id.new_meal_ingredient_quantity)

    fun bind(data: Ingredient, onItemClicked: (Int) -> Unit) {
        text.text = data.name
        quantity.text = data.quToString()

        text.setOnClickListener {
            onItemClicked(adapterPosition)
        }
    }

    companion object {
        fun create(parent: ViewGroup): IngredientViewHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_new_meal_ingredient, parent, false)
            return IngredientViewHolder(view)
        }
    }

}
