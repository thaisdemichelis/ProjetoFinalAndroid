package com.petcusto.app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Budget(
    val id: Long,
    val breedName: String,
    val treatment: String, // nome do enum Treatment
    val weightKg: Int,
    val total: Double,
    val createdAt: Long
) {
    val treatmentLabel: String
        get() = runCatching { Treatment.valueOf(treatment).label }.getOrDefault(treatment)
}
