package com.example.foodieplanner

import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Model: ViewModel() {
    var database: DatabaseReference = Firebase.database.reference

    var meals_for_day: ArrayList<Meal> = arrayListOf()

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

    fun addMealsForDay(meals: ArrayList<Meal>, month: String, date: String) {
        for (meal in meals) {
            database.child("Dates").child(month).child(date).child("Meals").child(meal.name).setValue(meal)
        }
    }
}