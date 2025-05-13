package com.example.cocktailapp.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val rainbowColorsBrush = Brush.sweepGradient(
    listOf(
        Color(0xFF9575CD),
        Color(0xFFBA68C8),
        Color(0xFFE57373),
        Color(0xFFFFB74D),
        Color(0xFFFFF176),
        Color(0xFFAED581),
        Color(0xFF4DD0E1),
        Color(0xFF9575CD)
    )
)

val backgroundColorsBrush = Brush.verticalGradient(
    listOf(
        Color(0xFF7BD5F5),
        Color(0xFF787FF6)
    )
)

val backgroundColorsBrushHorizontal = Brush.horizontalGradient(
    listOf(
        Color(0xFF7BD5F5),
        Color(0xFF787FF6)
    )
)