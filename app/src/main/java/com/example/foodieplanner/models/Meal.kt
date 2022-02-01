package com.example.foodieplanner.models

import com.example.foodieplanner.models.Ingredient

data class Meal(
    var name: String? = null,
    var ingredients: ArrayList<Ingredient>? = null,
    var instructions: ArrayList<String>? = null,
    var albumName: String? = null,
    var rating: Float? = null,
    var calories: Int? = null,
    var cost: Double? = null
)