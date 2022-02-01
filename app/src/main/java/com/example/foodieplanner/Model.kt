package com.example.foodieplanner

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodieplanner.models.Day
import com.example.foodieplanner.models.Meal
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Model: ViewModel() {

    var database: DatabaseReference = Firebase.database.reference

    var meals_for_day: ArrayList<Meal> = arrayListOf()

    // TODO
    var days: MutableLiveData<ArrayList<Day>> = MutableLiveData(arrayListOf())
    var albums: MutableLiveData<ArrayList<String>> = MutableLiveData(arrayListOf())
    var savedMeals: MutableLiveData<ArrayList<Meal>> = MutableLiveData(arrayListOf())
    var selectedDay: Int? = null

    fun addDay(day: Day) {
        var dayList = days.value
        if (dayList == null) {
            dayList = arrayListOf()
        }
        dayList.add(day)
        database.child("Days").setValue(dayList)
//        database.child("Days").child(day.day?.timeInMiliSeconds.toString()).setValue(day)
    }

    fun removeMealsFromDay(selected: ArrayList<Meal>) {
        val pos = selectedDay
        var dayList = days.value
        if (pos != null && dayList != null) {
            var day = dayList.get(pos)
            var meals = day.meals
            when (meals) {
                null -> {}
                else -> {
                    for (meal in selected) {
                        meals.remove(meal)
                    }
                    database.child("Days")
                        .child(pos.toString())
                        .child("meals")
                        .setValue(meals)
                }
            }
        }

    }

    fun addMealsToDay(nMeals: ArrayList<Meal>) {
        // TODO update day instead of entire list
        val pos = selectedDay
        var dayList = days.value
        if (pos != null && dayList != null) {
            var day = dayList.get(pos)
            var meals = day.meals
            if (meals == null) {
                day.meals = nMeals
            }
            else {
                meals.addAll(nMeals)
            }
            database.child("Days").setValue(dayList)
        }
    }

    fun getLatestDay(): Day? {
        var dayList = days.value
        if (dayList == null || dayList.isEmpty()) {
            return null
        }
        return dayList.get(dayList.size-1)
    }

    //------------------------------------------------------------------------


    fun addMeal(meal: Meal) {
        var name = meal.name
        if (name != null) {
            database.child("Meals").child(name).setValue(meal)
        }
    }

    fun deleteMeal(name: String?) {
        if (name != null) {
            database.child("Meals").child(name).removeValue()
        }
    }

    fun addAlbum(album: String) {
        database.child("Albums").child(album).setValue(album)
    }

    fun deleteAlbum(album: String) {
        database.child("Albums").child(album).removeValue()
    }

}