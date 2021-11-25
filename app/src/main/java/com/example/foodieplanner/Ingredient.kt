package com.example.foodieplanner


data class Ingredient(
    var name: String? = null,
    var quantity: String? = null,
    var unit: Unit? = Unit.NONE,
    var measure: String? = "vol",
    var standard: String? = "us"
) {

    fun quToString(): String {
        if (unit == Unit.NONE) {
            return "$quantity"
        }
        return "$quantity ${unit.toString()}"
    }

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