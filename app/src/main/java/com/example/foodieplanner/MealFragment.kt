package com.example.foodieplanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MealFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private val ingredients = ArrayList<Ingredient>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ingredients.add(Ingredient("Eggs", "5", Unit.NONE))
        ingredients.add(Ingredient( "Milk","1", Unit.CUP))
        ingredients.add(Ingredient("Cheese", "2", Unit.CUP))

        var view = inflater.inflate(R.layout.fragment_meal, container, false)

        view.findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.saved_meals_album_meal_toolbar).setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        recyclerView = view.findViewById(R.id.saved_meals_albums_meals_ingredient_list)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        viewAdapter = IngredientsAdapter(ingredients)
        recyclerView.adapter = viewAdapter
        return view
    }
}

class IngredientsAdapter(private val ingredientList: ArrayList<Ingredient>):
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IngredientsAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.view_meal_listitem,
            parent, false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(ingredientList[position])
    }

    override fun getItemCount() = ingredientList.size

    class ViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {
        fun bindItems(ingredient: Ingredient) {
            val amount: TextView = itemView.findViewById(R.id.ingredient_amount)
            amount.text = ingredient.quToString()

            val name: TextView = itemView.findViewById(R.id.ingredient_name)
            name.text = ingredient.name
        }
    }
}