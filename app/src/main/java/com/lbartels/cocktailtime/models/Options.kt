package com.lbartels.cocktailtime.models


class Options(private val image: Int,
              private val name : String,
              private val search : String) {
    fun getImage() : Int {
        return image
    }
    fun getName(): String{
        return name
    }
    fun getSearch() : String {
        return search
    }
}
