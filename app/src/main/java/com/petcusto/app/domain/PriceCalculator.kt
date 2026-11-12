package com.petcusto.app.domain

import com.petcusto.app.data.model.Treatment

object PriceCalculator {

    fun multiplier(weightKg: Int): Double = when {
        weightKg < 10 -> 1.0
        weightKg <= 25 -> 1.5
        else -> 2.0
    }

    fun rangeLabel(weightKg: Int): String = when {
        weightKg < 10 -> "menos de 10 kg"
        weightKg <= 25 -> "10 a 25 kg"
        else -> "acima de 25 kg"
    }

    fun total(treatment: Treatment, weightKg: Int): Double =
        treatment.basePrice * multiplier(weightKg)

    /** Ex.: "R$ 40 (banho) × 1,5 (10 a 25 kg)" */
    fun explanation(treatment: Treatment, weightKg: Int): String {
        val mult = multiplier(weightKg).toString().replace('.', ',')
        return "R$ ${treatment.basePrice.toInt()} (${treatment.label.lowercase()}) × $mult (${rangeLabel(weightKg)})"
    }
}
