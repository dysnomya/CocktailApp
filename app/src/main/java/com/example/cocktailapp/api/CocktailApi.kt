package com.example.cocktailapp.api

import com.example.cocktailapp.model.CocktailResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailApi {
    @GET("filter.php")
    suspend fun searchByCategory(@Query("c") name: String): CocktailResponse

    @GET("filter.php")
    suspend fun searchByAlcoholic(@Query("a") name: String): CocktailResponse


    @GET("lookup.php")
    suspend fun searchCocktailById(@Query("i") id: String): CocktailResponse
}