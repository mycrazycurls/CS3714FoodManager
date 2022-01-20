package com.example.foodieplanner

enum class Unit {

    TSP,TBSP,FLOZ,CUP,PINT,QUART,GAL,ML,LITER,LB,OUNCE,GRAM,MG,KG,NONE;

    override fun toString(): String {
        return when(this) {
            TSP -> "tsp"
            TBSP -> "tbsp"
            FLOZ -> "fl oz"
            CUP -> "cup"
            PINT -> "pint"
            QUART -> "qt"
            GAL -> "gal"
            ML -> "ml"
            LITER -> "l"
            LB -> "lb"
            OUNCE -> "oz"
            GRAM -> "g"
            MG -> "mg"
            KG -> "kg"
            NONE -> "none"
        }
    }

    fun suffix(): String {
        return when(this) {
            TSP -> "teaspoon"
            TBSP -> "tablespoon"
            FLOZ -> "fluid ounce"
            CUP -> "cup"
            PINT -> "pint"
            QUART -> "quart"
            GAL -> "gallon"
            ML -> "milliliter"
            LITER -> "liter"
            LB -> "pound"
            OUNCE -> "ounce"
            GRAM -> "grams"
            MG -> "milligrams"
            KG -> "kilograms"
            NONE -> ""
        }
    }

    fun id(): Int {
        return when(this) {
            TSP -> R.id.tsp
            TBSP -> R.id.tbsp
            FLOZ -> R.id.floz
            CUP -> R.id.cup
            PINT -> R.id.pint
            QUART -> R.id.quart
            GAL -> R.id.gal
            ML -> R.id.ml
            LITER -> R.id.liter
            LB -> R.id.pound
            OUNCE -> R.id.ounce
            GRAM -> R.id.grams
            MG -> R.id.mg
            KG -> R.id.kg
            NONE -> R.id.none
        }
    }

    fun convertTo(unit: Unit?) : Double? {
        if (unit != null) {
            return when(this) {
                TSP -> tspTo(unit)
                TBSP -> tbspTo(unit)
//            FLOZ ->
                CUP -> cupTo(unit)
//            PINT ->
//            QUART ->
//            GAL ->
//            ML ->
//            LITER ->
//            LB ->
//            OUNCE ->
//            GRAM ->
//            MG ->
//            KG ->
//            NONE ->
                else -> null
            }
        }
        return null
    }

    fun tspTo(unit: Unit): Double? {
        return when(unit) {
            TSP -> 1.0
            TBSP -> 0.333333
            FLOZ -> 0.166667
            CUP -> 0.0208333
            PINT -> 0.0104167
            QUART -> 0.00520833
            GAL -> 0.00130208
            ML -> 4.92892
            LITER -> 0.00492892
            else -> null
//            LB ->
//            OUNCE ->
//            GRAM ->
//            MG ->
//            KG ->
//            NONE ->
        }
    }

    fun tbspTo(unit: Unit): Double? {
        return when(unit) {
            TSP -> 1.0
            TBSP -> 3.0
            FLOZ -> 0.5
            CUP -> 0.0625
            PINT -> 0.03125
            QUART -> 0.015625
            ML -> 14.7868
            LITER -> 0.0147868
            else -> null
//            LB ->
//            OUNCE ->
//            GRAM ->
//            MG ->
//            KG ->
//            NONE ->
        }
    }

    fun cupTo(unit: Unit): Double? {
        return when(unit) {
            TSP -> 48.0
            TBSP -> 16.0
            FLOZ -> 8.0
            CUP -> 1.0
            PINT -> 0.5
            QUART -> 0.25
            GAL -> 0.0625
            ML -> 236.588
            LITER -> 0.236588
            else -> null
//            LB ->
//            OUNCE ->
//            GRAM ->
//            MG ->
//            KG ->
//            NONE ->
        }
    }

}