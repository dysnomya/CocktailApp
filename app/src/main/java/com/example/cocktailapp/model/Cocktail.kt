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

    @JsonProperty("strIngredient1") val strIngredient1: String? = null,
    @JsonProperty("strIngredient2") val strIngredient2: String? = null,
    @JsonProperty("strIngredient3") val strIngredient3: String? = null,
    @JsonProperty("strIngredient4") val strIngredient4: String? = null,
    @JsonProperty("strIngredient5") val strIngredient5: String? = null,
    @JsonProperty("strIngredient6") val strIngredient6: String? = null,
    @JsonProperty("strIngredient7") val strIngredient7: String? = null,
    @JsonProperty("strIngredient8") val strIngredient8: String? = null,
    @JsonProperty("strIngredient9") val strIngredient9: String? = null,
    @JsonProperty("strIngredient10") val strIngredient10: String? = null,
    @JsonProperty("strIngredient11") val strIngredient11: String? = null,
    @JsonProperty("strIngredient12") val strIngredient12: String? = null,
    @JsonProperty("strIngredient13") val strIngredient13: String? = null,
    @JsonProperty("strIngredient14") val strIngredient14: String? = null,
    @JsonProperty("strIngredient15") val strIngredient15: String? = null,

    @JsonProperty("strMeasure1") val strMeasure1: String? = null,
    @JsonProperty("strMeasure2") val strMeasure2: String? = null,
    @JsonProperty("strMeasure3") val strMeasure3: String? = null,
    @JsonProperty("strMeasure4") val strMeasure4: String? = null,
    @JsonProperty("strMeasure5") val strMeasure5: String? = null,
    @JsonProperty("strMeasure6") val strMeasure6: String? = null,
    @JsonProperty("strMeasure7") val strMeasure7: String? = null,
    @JsonProperty("strMeasure8") val strMeasure8: String? = null,
    @JsonProperty("strMeasure9") val strMeasure9: String? = null,
    @JsonProperty("strMeasure10") val strMeasure10: String? = null,
    @JsonProperty("strMeasure11") val strMeasure11: String? = null,
    @JsonProperty("strMeasure12") val strMeasure12: String? = null,
    @JsonProperty("strMeasure13") val strMeasure13: String? = null,
    @JsonProperty("strMeasure14") val strMeasure14: String? = null,
    @JsonProperty("strMeasure15") val strMeasure15: String? = null,
) : Parcelable {

    val ingredients: List<Pair<String, String>> by lazy {
        listOf(
            strIngredient1 to strMeasure1,
            strIngredient2 to strMeasure2,
            strIngredient3 to strMeasure3,
            strIngredient4 to strMeasure4,
            strIngredient5 to strMeasure5,
            strIngredient6 to strMeasure6,
            strIngredient7 to strMeasure7,
            strIngredient8 to strMeasure8,
            strIngredient9 to strMeasure9,
            strIngredient10 to strMeasure10,
            strIngredient11 to strMeasure11,
            strIngredient12 to strMeasure12,
            strIngredient13 to strMeasure13,
            strIngredient14 to strMeasure14,
            strIngredient15 to strMeasure15
        ).filter { it.first?.isNotBlank() == true }
            .map { (ingredient, measure) ->
                ingredient!!.trim() to (measure?.trim() ?: "")
            }
    }
}