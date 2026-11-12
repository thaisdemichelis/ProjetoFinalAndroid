package com.petcusto.app.data.model

/** [apiPath] é o caminho da raça na Dog CEO API (ex.: "poodle/toy"). */
data class Breed(
    val id: String,
    val name: String,
    val group: String,
    val apiPath: String
)

object BreedCatalog {
    val all = listOf(
        Breed("labrador", "Labrador", "retriever", "labrador"),
        Breed("beagle", "Beagle", "hound", "beagle"),
        Breed("poodle", "Poodle", "toy", "poodle/toy"),
        Breed("bulldog", "Bulldog", "french", "bulldog/french"),
        Breed("husky", "Husky", "siberian", "husky"),
        Breed("pug", "Pug", "", "pug")
    )

    fun byId(id: String): Breed? = all.firstOrNull { it.id == id }
}
