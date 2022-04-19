package com.lbartels.cocktailtime.API


import com.lbartels.cocktailtime.models.DrinkResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("search.php?")
    fun getDrink(
        @Query("s") name: String
    ) : Call<DrinkResponse>

    @GET("random.php?")
    fun getRandomCocktail() : Call<DrinkResponse>



    companion object {
        private val BASE_URL = "https://www.thecocktaildb.com/api/json/v1/1/"

        fun create() : ApiService {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiService::class.java)

        }
    }

}
