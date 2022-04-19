package com.lbartels.cocktailtime.models

data class DrinkModel(
    var idDrink: String? = null,
    val strDrink: String? = null,
//    val strCategory: String? = null,
    val strInstructions: String? = null,
    val strDrinkThumb: String? = null,
    val strIngredient1: String? = null,
    val strIngredient2: String? = null,
    val strIngredient3: String? = null,
    val strIngredient4: String? = null,
    val strIngredient5: String? = null,
    val strIngredient6: String? = null,
    val strIngredient7: String? = null,
    val strIngredient8: String? = null,
    val strIngredient9: String? = null,
    val strIngredient10: String? = null,
    val strIngredient11: String? = null,
) : java.io.Serializable