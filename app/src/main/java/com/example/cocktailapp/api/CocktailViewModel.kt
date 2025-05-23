package com.example.cocktailapp.api

import androidx.compose.runtime.State
import com.example.cocktailapp.model.Cocktail

interface CocktailViewModel {
    val cocktailList: State<List<Cocktail>>
    val isLoading: State<Boolean>

    fun fetchCocktailDetail(cocktailId: String)
}