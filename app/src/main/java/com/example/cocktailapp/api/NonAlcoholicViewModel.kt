package com.example.cocktailapp.api

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cocktailapp.model.Cocktail
import kotlinx.coroutines.launch

class NonAlcoholicViewModel : ViewModel(), CocktailViewModel {

    private val _cocktailList = mutableStateOf<List<Cocktail>>(emptyList())
    override val cocktailList: State<List<Cocktail>> = _cocktailList


    init {
        viewModelScope.launch {
            val alcoholicResponse = RetrofitClient.api.searchByAlcoholic("Non_Alcoholic")
            _cocktailList.value = alcoholicResponse.drinks!!

        }
    }

    private val _isLoading = mutableStateOf(false)
    override val isLoading: State<Boolean> = _isLoading


    override fun fetchCocktailDetail(cocktailId: String) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val response = RetrofitClient.api.searchCocktailById(cocktailId)
                val cocktailDetail = response.drinks?.firstOrNull()

                if (cocktailDetail != null) {
                    _cocktailList.value = _cocktailList.value.map { cocktail ->
                        if (cocktail.id == cocktailId) cocktailDetail else cocktail
                    }
                }
            } catch (e: Exception) {} finally {
                _isLoading.value = false
            }
        }
    }
}