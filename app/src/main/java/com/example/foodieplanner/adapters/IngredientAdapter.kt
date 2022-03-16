package com.example.foodieplanner.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foodieplanner.models.Ingredient
import com.example.foodieplanner.viewholders.IngredientViewHolder

// New Ingredients to Populate Recycler View
class IngredientAdapter(private val onItemClicked: (Int) -> Unit):
    ListAdapter<Ingredient, RecyclerView.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return IngredientViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as IngredientViewHolder).bind(getItem(position), onItemClicked)
    }

    override fun submitList(list: MutableList<Ingredient>?) {
        super.submitList(list?.let { ArrayList(list) })
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Ingredient>() {
            override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
                return false
            }
        }
    }

}