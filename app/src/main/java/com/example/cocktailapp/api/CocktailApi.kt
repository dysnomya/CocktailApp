package com.example.cocktailapp.api

import com.example.cocktailapp.model.Cocktail
import com.example.cocktailapp.model.CocktailResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailApi {
    @GET("filter.php")
    suspend fun searchCocktails(@Query("c") name: String): CocktailResponse

    @GET("lookup.php")
    suspend fun searchCocktailById(@Query("i") id: String): CocktailResponse
}