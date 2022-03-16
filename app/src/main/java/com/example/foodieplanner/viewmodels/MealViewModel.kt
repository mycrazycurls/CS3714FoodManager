package com.example.foodieplanner.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodieplanner.models.Ingredient
import com.example.foodieplanner.models.Meal

class MealViewModel: ViewModel() {

    var ingredients: MutableLiveData<ArrayList<Ingredient>> = MutableLiveData(arrayListOf())

    fun addIngredient(ing: Ingredient) {
        var newList = ingredients.value
        newList?.add(ing)
        ingredients.postValue(newList)
    }

    fun updateIngredient(pos: Int, ing: Ingredient) {
        var newList = ingredients.value
        newList?.set(pos, ing)
        ingredients.postValue(newList)
    }

    fun removeIngredient(pos: Int) {
        var newList = ingredients.value
        newList?.removeAt(pos)
        ingredients.postValue(newList)
    }

}