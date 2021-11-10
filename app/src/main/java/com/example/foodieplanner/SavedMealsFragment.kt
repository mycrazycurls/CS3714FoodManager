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

class SavedMealsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private val albums = ArrayList<AlbumCard>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Dummy data
        albums.add(AlbumCard("Breakfest", Color.LTGRAY))
        albums.add(AlbumCard("Dinner", Color.GRAY))

        var view = inflater.inflate(R.layout.fragment_saved_meals, container, false)

        recyclerView = view.findViewById(R.id.saved_meals_albums_list)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        viewAdapter = AlbumnAdapter(albums)
        recyclerView.adapter = viewAdapter
        return view
    }
}

class AlbumnAdapter(private val albumList: ArrayList<AlbumCard>):
    RecyclerView.Adapter<AlbumnAdapter.ViewHolder>() {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): AlbumnAdapter.ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(
                R.layout.album_card_view,
                parent, false
            )
            return ViewHolder(v)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bindItems(albumList[position])
        }

        override fun getItemCount() = albumList.size

        class ViewHolder(private val view: View) :
            RecyclerView.ViewHolder(view) {
                fun bindItems(albumCard: AlbumCard) {
                    val title: TextView = itemView.findViewById(R.id.meal_card_title)
                    title.setBackgroundColor(albumCard.color)
                    title.text = albumCard.albumName

                    val image: ImageView = itemView.findViewById(R.id.album_card_image)
                    when (albumCard.albumName) {
                        "Breakfest" -> image.setImageResource(R.drawable.breakfast)
                        "Dinner" -> image.setImageResource(R.drawable.dinner)
                    }

                    itemView.setOnClickListener {
                        view.findNavController().navigate(R.id.action_savedMealsFragment_to_albumsFragment)
                    }
                }
            }
}