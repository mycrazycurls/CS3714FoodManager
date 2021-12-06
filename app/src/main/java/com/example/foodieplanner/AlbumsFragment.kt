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
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random
import androidx.recyclerview.widget.ItemTouchHelper.Callback.makeMovementFlags

import androidx.recyclerview.widget.ItemTouchHelper




class AlbumsFragment : Fragment() {
    private val model: Model by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private val meals = ArrayList<MealCard>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_albums, container, false)

        recyclerView = view.findViewById(R.id.saved_meals_albums_meal_list)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        viewAdapter = MealAdapter(meals)
        recyclerView.adapter = viewAdapter

        // For deleting meals
        val itemTouchHelperCallback = object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    (viewAdapter as MealAdapter).removeMeal(viewHolder.adapterPosition)
                }

            }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        val albumName = this.arguments?.getString("albumName")

        // Load albums from firebase
        model.database.child("Meals").get().addOnSuccessListener { data ->
            for (meal in data.children) {
                // Extract album the meal belongs to
                var album = ""
                var rating = 0.0
                var name = ""
                for (field in meal.children) {
                    when (field.key) {
                        "albumName" -> album = field.value.toString()
                        "name" -> name = field.value.toString()
                        "rating" -> rating = field.value.toString().toDouble()
                    }
                }

                // All meals
                if (albumName == "All_") {
                    view.findViewById<com.google.android.material.appbar.MaterialToolbar>(
                        R.id.saved_meals_album_toolbar).title = "All meals"
                    (viewAdapter as MealAdapter).insertMeal(name, rating)
                }
                // Specific album
                else {
                    view.findViewById<com.google.android.material.appbar.MaterialToolbar>(
                        R.id.saved_meals_album_toolbar).title = albumName
                    // Display only meals associated with this album instance
                    if (album == albumName) {
                        (viewAdapter as MealAdapter).insertMeal(name, rating)
                    }

                }
            }
        }

        view.findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.saved_meals_album_toolbar).setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        return view
    }

    inner class MealAdapter(private val mealList: ArrayList<MealCard>):
        RecyclerView.Adapter<MealAdapter.ViewHolder>() {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): MealAdapter.ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(
                R.layout.card_albums_meal,
                parent, false
            )
            return ViewHolder(v)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bindItems(mealList[position])
        }

        fun insertMeal(name: String, rating: Double) {
            val cardColor: Int = getRandomColor()
            mealList.add(MealCard(name, cardColor, rating))
            notifyDataSetChanged()
        }

        fun removeMeal(pos: Int) {
            model.deleteMeal(mealList[pos].mealName)
            mealList.removeAt(pos)
            notifyItemRemoved(pos)
            notifyItemRangeChanged(pos, mealList.size)
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
            fun bindItems(mealCard: MealCard) {
                val title: TextView = itemView.findViewById(R.id.meal_card_title)
                title.setBackgroundColor(mealCard.color)
                title.text = mealCard.mealName

                val rating: TextView = itemView.findViewById(R.id.meal_card_rating)
                rating.text = mealCard.rating.toString()

                val image: ImageView = itemView.findViewById(R.id.album_card_image)
                image.setImageResource(R.drawable.default_pic)

                itemView.setOnClickListener {
                    view.findNavController().navigate(R.id.action_albumsFragment_to_mealFragment)
                }
            }
        }
    }
}