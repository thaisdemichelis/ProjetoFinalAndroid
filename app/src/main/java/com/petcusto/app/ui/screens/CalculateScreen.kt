package com.petcusto.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.petcusto.app.data.model.Treatment
import com.petcusto.app.domain.PriceCalculator
import com.petcusto.app.ui.components.PrimaryButton
import com.petcusto.app.ui.theme.PetOrange
import com.petcusto.app.util.formatCurrency
import com.petcusto.app.viewmodel.AppViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculateScreen(vm: AppViewModel, onSaved: () -> Unit) {
    val calc by vm.calc.collectAsStateWithLifecycle()
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "Calcular orçamento",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        // Raça
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
            OutlinedTextField(
                value = calc.breed.name,
                onValueChange = {},
                readOnly = true,
                label = { Text("Raça") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                vm.breeds.forEach { breed ->
                    DropdownMenuItem(
                        text = { Text(breed.name) },
                        onClick = {
                            vm.selectBreed(breed)
                            expanded = false
                        }
                    )
                }
            }
        }

        // Tratamento
        Text("Tratamento", style = MaterialTheme.typography.labelLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Treatment.entries.forEach { t ->
                FilterChip(
                    selected = calc.treatment == t,
                    onClick = { vm.setTreatment(t) },
                    label = { Text(t.label) }
                )
            }
        }

        // Peso
        Text("Peso", style = MaterialTheme.typography.labelLarge)
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Slider(
                value = calc.weightKg.toFloat(),
                onValueChange = { vm.setWeight(it.roundToInt()) },
                valueRange = 1f..60f,
                modifier = Modifier.weight(1f)
            )
            OutlinedCard(shape = RoundedCornerShape(10.dp)) {
                Text("${calc.weightKg} kg", modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp))
            }
        }

        // Valor estimado
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = PetOrange)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text("Valor estimado", color = Color.Black)
                Text(
                    formatCurrency(calc.total),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
                Text(
                    PriceCalculator.explanation(calc.treatment, calc.weightKg),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black
                )
            }
        }

        Spacer(Modifier.height(4.dp))
        PrimaryButton("Salvar orçamento", onClick = { vm.saveBudget(onSaved) })
    }
}
