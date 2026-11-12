package com.petcusto.app.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.NavDestination.Companion.hasRoute
import com.petcusto.app.ui.navigation.BreedsRoute
import com.petcusto.app.ui.navigation.CalculateRoute
import com.petcusto.app.ui.navigation.SavedRoute

data class BottomItem(val label: String, val route: Any, val icon: ImageVector)

val bottomItems = listOf(
    BottomItem("Raças", BreedsRoute, Icons.Default.Pets),
    BottomItem("Calcular", CalculateRoute, Icons.Default.Calculate),
    BottomItem("Salvos", SavedRoute, Icons.Default.Bookmark)
)

fun NavDestination?.isBottomTab(): Boolean =
    this?.hierarchy?.any { dest -> bottomItems.any { dest.hasRoute(it.route::class) } } == true

@Composable
fun PetBottomBar(navController: NavHostController, destination: NavDestination?) {
    NavigationBar {
        bottomItems.forEach { item ->
            val selected = destination?.hierarchy?.any { it.hasRoute(item.route::class) } == true
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo<BreedsRoute> { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}
