package com.petcusto.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = PetGreen,
    onPrimary = Color.White,
    secondary = PetOrange,
    background = PetCream,
    onBackground = PetText,
    surface = Color.White,
    onSurface = PetText,
    secondaryContainer = PetGreenLight,
    onSecondaryContainer = PetGreen,
    error = PetRed
)

@Composable
fun PetCustoTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = LightColors, content = content)
}
