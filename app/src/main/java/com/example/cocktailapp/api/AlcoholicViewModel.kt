package com.example.cocktailapp.api

import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cocktailapp.model.Cocktail
import kotlinx.coroutines.launch


class AlcoholicViewModel : ViewModel(), CocktailViewModel {

    private val _cocktailList = mutableStateOf<List<Cocktail>>(emptyList())
    override val cocktailList: State<List<Cocktail>> = _cocktailList

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    init {
        viewModelScope.launch {
            try {
                val alcoholicResponse = RetrofitClient.api.searchByAlcoholic("Alcoholic")
                _cocktailList.value = alcoholicResponse.drinks!!
            } catch (e: Exception) {
                _cocktailList.value = emptyList()
                _toastMessage.value = "No Internet or other API problem.."
            }

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