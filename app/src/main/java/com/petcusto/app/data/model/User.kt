package com.petcusto.app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val name: String,
    val email: String,
    val passwordHash: String
)
