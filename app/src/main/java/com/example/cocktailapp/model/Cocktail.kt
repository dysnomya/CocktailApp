package com.example.cocktailapp.model

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.parcelize.Parcelize

@JsonIgnoreProperties(ignoreUnknown = true)
data class CocktailResponse(
    @JsonProperty("drinks") val drinks: List<Cocktail>? = null
)

@Parcelize
@JsonIgnoreProperties(ignoreUnknown = true)
data class Cocktail(
    @JsonProperty("idDrink") val id: String,
    @JsonProperty("strDrink") val name: String,
    @JsonProperty("strDrinkThumb") val imageUrl: String,
    @JsonProperty("strInstructions") val instructions: String? = null,
) : Parcelable