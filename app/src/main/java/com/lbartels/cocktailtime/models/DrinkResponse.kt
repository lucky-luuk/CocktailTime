package com.lbartels.cocktailtime.models

import com.google.gson.annotations.SerializedName

class DrinkResponse {
    @SerializedName("drinks")
    var drinks = ArrayList<DrinkModel>()
}