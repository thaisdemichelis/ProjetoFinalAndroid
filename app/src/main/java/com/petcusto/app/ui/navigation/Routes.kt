package com.petcusto.app.ui.navigation

import kotlinx.serialization.Serializable

/** Rotas type-safe (Navigation Compose 2.8+). */
@Serializable data object SplashRoute
@Serializable data object LoginRoute
@Serializable data object RegisterRoute
@Serializable data object BreedsRoute
@Serializable data class BreedDetailRoute(val breedId: String)
@Serializable data object CalculateRoute
@Serializable data object SavedRoute
