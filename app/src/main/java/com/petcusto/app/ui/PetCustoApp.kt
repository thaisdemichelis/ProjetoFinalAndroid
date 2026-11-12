package com.petcusto.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.petcusto.app.ui.components.PetBottomBar
import com.petcusto.app.ui.components.isBottomTab
import com.petcusto.app.ui.navigation.AppNavHost
import com.petcusto.app.viewmodel.AppViewModel

@Composable
fun PetCustoApp(vm: AppViewModel = viewModel()) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val destination = backStackEntry?.destination

    Scaffold(
        bottomBar = {
            // Bottom Navigation só nas 3 abas (Raças, Calcular, Salvos)
            if (destination.isBottomTab()) {
                PetBottomBar(navController, destination)
            }
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            vm = vm,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
