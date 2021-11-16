package com.example.foodieplanner

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AlbumsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private val meals = ArrayList<MealCard>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Dummy data
        meals.add(MealCard("Omlette", Color.LTGRAY, 8.0))
        meals.add(MealCard("Pancakes", Color.GRAY, 9.5))

        var view = inflater.inflate(R.layout.fragment_albums, container, false)

        view.findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.saved_meals_album_toolbar).setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        recyclerView = view.findViewById(R.id.saved_meals_albums_meal_list)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        viewAdapter = MealAdapter(meals)
        recyclerView.adapter = viewAdapter
        return view
    }
}

class MealAdapter(private val mealList: ArrayList<MealCard>):
    RecyclerView.Adapter<MealAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MealAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.meal_card_view,
            parent, false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(mealList[position])
    }

    override fun getItemCount() = mealList.size

    class ViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {
        fun bindItems(mealCard: MealCard) {
            val title: TextView = itemView.findViewById(R.id.meal_card_title)
            title.setBackgroundColor(mealCard.color)
            title.text = mealCard.mealName

            val rating: TextView = itemView.findViewById(R.id.meal_card_rating)
            rating.text = mealCard.rating.toString()

            val image: ImageView = itemView.findViewById(R.id.album_card_image)
            when (mealCard.mealName) {
                "Omlette" -> image.setImageResource(R.drawable.omelette)
                "Pancakes" -> image.setImageResource(R.drawable.pancakes)
            }

            itemView.setOnClickListener {
                view.findNavController().navigate(R.id.action_albumsFragment_to_mealFragment)
            }
        }
    }
}