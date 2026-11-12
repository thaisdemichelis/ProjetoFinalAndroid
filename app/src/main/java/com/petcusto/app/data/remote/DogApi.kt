package com.petcusto.app.data.remote

import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Path

@Serializable
data class DogImageResponse(
    val message: String,
    val status: String
)

interface DogApi {
    // encoded = true mantém a "/" de raças com sub-raça (poodle/toy)
    @GET("breed/{path}/images/random")
    suspend fun randomImage(
        @Path(value = "path", encoded = true) path: String
    ): DogImageResponse
}
