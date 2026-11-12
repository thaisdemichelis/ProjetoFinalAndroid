package com.petcusto.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.petcusto.app.ui.components.PrimaryButton
import com.petcusto.app.ui.theme.PetGreenLight
import com.petcusto.app.viewmodel.AppViewModel

@Composable
fun BreedDetailScreen(
    vm: AppViewModel,
    breedId: String,
    onBack: () -> Unit,
    onCalculate: () -> Unit
) {
    val breed = vm.breedById(breedId) ?: return
    val images by vm.breedImages.collectAsStateWithLifecycle()
    val imageUrl = images[breed.id]

    Column(Modifier.fillMaxSize()) {
        TextButton(onClick = onBack, modifier = Modifier.padding(horizontal = 8.dp)) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            Text("  Voltar")
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(PetGreenLight),
            contentAlignment = Alignment.Center
        ) {
            if (imageUrl != null) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = breed.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text("Foto da raça (API)")
            }
        }

        Column(Modifier.padding(24.dp)) {
            Text(breed.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text("Foto e dados vindos da Dog CEO API.", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(Modifier.weight(1f))

        PrimaryButton(
            text = "Fazer orçamento para esta raça",
            onClick = {
                vm.selectBreed(breed)
                onCalculate()
            },
            modifier = Modifier.padding(24.dp)
        )
    }
}
