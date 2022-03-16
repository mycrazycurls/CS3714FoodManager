package com.example.foodieplanner.viewmodels

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodieplanner.R
import com.example.foodieplanner.models.Ingredient

class IngredientFormViewModel: ViewModel() {

    var ingredient: Ingredient? = null

    var name: MutableLiveData<String> = MutableLiveData("")

    fun setName(str: String) {
        name.postValue(str)
    }

    fun getName(): String {
        var name = ingredient?.name

        return when(name) {
            null -> {
                Log.e("Ing View Model","Null")
                "Null"
            }
            else -> {
                Log.e("Ing View Model",name)
                name
            }
        }
    }

}