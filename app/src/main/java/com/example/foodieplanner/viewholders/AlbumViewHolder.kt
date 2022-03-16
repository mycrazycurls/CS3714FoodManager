package com.example.foodieplanner.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodieplanner.R
import kotlin.random.Random

class AlbumAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val title: TextView = itemView.findViewById(R.id.meal_card_title)
    private val image: ImageView = itemView.findViewById(R.id.album_card_image)

    fun bind(data: String, onItemClicked: (pos: Int) -> Unit) {

        title.text = data
        image.setBackgroundColor(getRandomColor())
        itemView.setOnClickListener {
            onItemClicked(adapterPosition)
        }

    }

    private fun getRandomColor(): Int {
        when (Random.nextInt(0, 3)) {
            0 -> return R.color.softblue
            1 -> return R.color.rosyhiglight
            2 -> return R.color.brewedmustard
            3 -> return R.color.creamypeach
        }
        return R.color.grisilla
    }

}