package com.example.cocktailapp.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class CocktailResponse(
    @JsonProperty("drinks") val drinks: List<Cocktail>? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Cocktail(
    @JsonProperty("idDrink") val id: String,
    @JsonProperty("strDrink") val name: String,
    @JsonProperty("strDrinkThumb") val imageUrl: String,
    @JsonProperty("strInstructions") val instructions: String? = null,
)
