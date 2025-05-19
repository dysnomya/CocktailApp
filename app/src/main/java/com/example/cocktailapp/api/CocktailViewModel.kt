package com.example.cocktailapp.api

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cocktailapp.model.Cocktail
import kotlinx.coroutines.launch

class CocktailViewModel: ViewModel() {

    private val _cocktailList = mutableStateOf<List<Cocktail>>(emptyList())
    val cocktailList: State<List<Cocktail>> = _cocktailList

    init {
        viewModelScope.launch {
            val response = RetrofitClient.api.searchCocktails("Cocktail")
            _cocktailList.value = response.drinks!!
        }
    }

    fun fetchCocktailDetail(cocktailId: String) {
        viewModelScope.launch {

            val response = RetrofitClient.api.searchCocktailById(cocktailId)
            val cocktailDetail = response.drinks?.firstOrNull()

            cocktailDetail?.let { newDetail ->
                _cocktailList.value = _cocktailList.value.map { cocktail ->
                    if (cocktail.id == cocktailId) {
                        newDetail
                    } else {
                        cocktail
                    }
                }
            }
        }
    }
}