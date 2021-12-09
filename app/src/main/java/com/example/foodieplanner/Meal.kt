package com.example.foodieplanner

data class Meal(
    var name: String,
    var ingredients: ArrayList<Ingredient>,
    var instructions: ArrayList<String>,
    var albumName: String,
    var rating: Float
)