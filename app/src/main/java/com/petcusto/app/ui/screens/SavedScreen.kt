package com.petcusto.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.petcusto.app.data.model.Budget
import com.petcusto.app.ui.theme.PetRed
import com.petcusto.app.util.formatCurrency
import com.petcusto.app.util.formatDayMonth
import com.petcusto.app.viewmodel.AppViewModel

@Composable
fun SavedScreen(vm: AppViewModel, onLogout: () -> Unit) {
    val budgets by vm.budgets.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Orçamentos salvos",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            OutlinedButton(
                onClick = onLogout,
                border = BorderStroke(1.dp, PetRed),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = PetRed)
            ) { Text("Sair") }
        }

        if (budgets.isEmpty()) {
            Text("Nenhum orçamento salvo ainda.", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(budgets, key = { it.id }) { budget ->
                    BudgetItem(budget, onDelete = { vm.deleteBudget(budget.id) })
                }
            }
        }
    }
}

@Composable
private fun BudgetItem(budget: Budget, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "${budget.breedName} · ${budget.treatmentLabel}",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    formatCurrency(budget.total),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                "${budget.weightKg} kg · ${formatDayMonth(budget.createdAt)}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(6.dp))
            OutlinedButton(
                onClick = onDelete,
                border = BorderStroke(1.dp, PetRed),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = PetRed),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                modifier = Modifier.height(32.dp)
            ) { Text("Excluir", style = MaterialTheme.typography.labelMedium) }
        }
    }
}
