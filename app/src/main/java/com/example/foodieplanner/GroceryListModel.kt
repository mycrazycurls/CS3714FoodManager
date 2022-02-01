package com.example.foodieplanner

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodieplanner.models.Day
import com.example.foodieplanner.models.Ingredient
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class GroceryListModel: ViewModel() {

    var dateRange: MutableLiveData<ArrayList<Long>> = MutableLiveData(arrayListOf())
    //var days: MutableLiveData<ArrayList<Day>> = MutableLiveData(arrayListOf())

    fun updateRange(first: Long, second: Long) {
        var timeList = arrayListOf<Long>()
        var calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = first
        while (calendar.timeInMillis <= second) {
            timeList.add(calendar.timeInMillis)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        dateRange.postValue(timeList)
    }

    fun getRangeText(): String {
        var dateRangeCopy = dateRange.value
        if (dateRangeCopy == null || dateRangeCopy.isEmpty()) {
            return "Select Dates"
        }
        return milisecDateToString(dateRangeCopy[0], dateRangeCopy[dateRangeCopy.size-1])
    }

    fun getSelection(): Pair<Long, Long>? {
        var dateRangeCopy = dateRange.value
        if (dateRangeCopy == null || dateRangeCopy.isEmpty()) {
            return null
        }
        return Pair(dateRangeCopy[0],dateRangeCopy[dateRangeCopy.size-1])
    }

    fun getIngredients(days: ArrayList<Day>): ArrayList<Ingredient> {
        val newIngredients = arrayListOf<Ingredient>()
        var dateRangeCopy = dateRange.value
        if (dateRangeCopy != null && days != null) {
            for (day in days) {
                if (day.day?.timeInMiliSeconds in dateRangeCopy) {
                    var meals = day.meals
                    if (meals != null) {
                        for (meal in meals) {
                            var ingredients = meal.ingredients
                            if (ingredients != null) {
                                for (ing in ingredients) {
                                    var nQuantity = ing.quantity
                                    var nUnit = ing.unit

                                    if (ing in newIngredients) {
                                        var eIndex = newIngredients.indexOf(ing)
                                        var eQuantity = newIngredients[eIndex].quantity
                                        var eUnit = newIngredients[eIndex].unit
                                        var conversion = nUnit?.convertTo(eUnit)

                                        if (nQuantity != null && eQuantity != null && conversion != null) {
                                            if (conversion < 1.0) {
                                                newIngredients[newIngredients.indexOf(ing)].quantity = eQuantity + nQuantity*conversion
                                            } else {
                                                newIngredients[newIngredients.indexOf(ing)].unit = nUnit
                                                newIngredients[newIngredients.indexOf(ing)].quantity = eQuantity/conversion + nQuantity
                                            }
                                        } else {
                                            newIngredients.add(ing.copy())
                                        }
                                    } else {
                                        newIngredients.add(ing.copy())
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return newIngredients
    }

    private fun milisecDateToString(first : Long, second: Long) : String {
        var formatter = SimpleDateFormat("MM/dd")
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = first
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        var str = "${formatter.format(calendar.timeInMillis)}"
        calendar.timeInMillis = second
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        str += " - ${formatter.format(calendar.timeInMillis)}"
        return str
    }



}