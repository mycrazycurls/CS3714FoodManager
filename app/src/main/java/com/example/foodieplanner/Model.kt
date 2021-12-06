package com.example.foodieplanner

import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Model: ViewModel() {
    var database: DatabaseReference = Firebase.database.reference

    fun addMeal(meal: Meal) {
        database.child("Meals").child(meal.name).setValue(meal)
    }

    fun deleteMeal(name: String) {
        database.child("Meals").child(name).removeValue()
    }

    fun addAlbum(album: String) {
        database.child("Albums").child(album).setValue(album)
    }

    fun deleteAlbum(album: String) {
        database.child("Albums").child(album).removeValue()
    }
}