package com.example.foodieplanner.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foodieplanner.R
import com.example.foodieplanner.models.Day
import com.example.foodieplanner.models.Meal
import com.example.foodieplanner.viewholders.CalendarDayCardHeaderViewHolder
import com.example.foodieplanner.viewholders.CalendarDayCardViewHolder

class CalendarDayCardAdapter(private val onItemClicked: (pos: Int) -> Unit): ListAdapter<Day, RecyclerView.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            0 -> CalendarDayCardHeaderViewHolder.create(parent)
            else -> CalendarDayCardViewHolder.create(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            0 -> (holder as CalendarDayCardHeaderViewHolder).bind(getItem(position), onItemClicked)
            else -> (holder as CalendarDayCardViewHolder).bind(getItem(position), onItemClicked)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(position) {
            0 -> {
                0
            }
            else -> 1
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Day>() {
            override fun areItemsTheSame(oldItem: Day, newItem: Day): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Day, newItem: Day): Boolean {
                return false
            }
        }
    }

}

