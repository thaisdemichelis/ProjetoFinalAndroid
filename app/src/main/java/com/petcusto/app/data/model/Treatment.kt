package com.petcusto.app.data.model

enum class Treatment(val label: String, val basePrice: Double) {
    BANHO("Banho", 40.0),
    CONSULTA("Consulta", 120.0),
    VACINA("Vacina", 90.0)
}
