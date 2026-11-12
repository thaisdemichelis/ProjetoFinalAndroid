package com.petcusto.app.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val ptBr = Locale("pt", "BR")

fun formatCurrency(value: Double): String =
    NumberFormat.getCurrencyInstance(ptBr).format(value)

fun formatDayMonth(millis: Long): String =
    SimpleDateFormat("dd/MM", ptBr).format(Date(millis))
