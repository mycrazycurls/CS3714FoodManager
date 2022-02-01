package com.example.foodieplanner.models

data class Day(
    var meals: ArrayList<Meal>? = null,
    var day: CalendarDay? = null,
    var groceriesBought: Boolean? = false)