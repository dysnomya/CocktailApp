package com.example.cocktailapp.api

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.cocktailapp.model.Cocktail

interface CocktailViewModel {
    val cocktailList: State<List<Cocktail>>
    val isLoading: State<Boolean>

    fun fetchCocktailDetail(cocktailId: String)
}