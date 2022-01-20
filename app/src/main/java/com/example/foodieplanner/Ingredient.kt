package com.example.foodieplanner

import com.google.firebase.database.Exclude
import kotlin.reflect.typeOf

data class Ingredient(
    var name: String? = null,
    var quantity: Double? = null,
    var unit: Unit? = Unit.NONE,
    var measure: String? = "vol",
    var standard: String? = "us"
) {

    @Exclude
    override fun equals(obj: Any?): Boolean {
        if (obj == null) {
            return false
        }
        if (obj is Ingredient) {
            if (obj.name == this.name) {
                return true
            }
        }
        return false
    }

    @Exclude
    fun quToString(): String {
        if (unit == Unit.NONE) {
            return "$quantity"
        }
        return "$quantity ${unit.toString()}"
    }

    @Exclude
    fun setMeasure(checkedId: Int) {
        when (checkedId) {
            R.id.ni_volume_chip -> {
                measure = "vol"
            }
            R.id.ni_weight_chip -> {
                measure = "weight"
            }
            R.id.ni_absolute_chip -> {
                measure = "none"
            }
        }
    }

    @Exclude
    fun setStandard(checkedId: Int) {
        when (checkedId) {
            R.id.ni_us_chip -> {
                standard = "us"
            }
            R.id.ni_metric_chip -> {
                standard = "metric"
            }
        }
    }
}