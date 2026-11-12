package com.petcusto.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.petcusto.app.ui.screens.BreedDetailScreen
import com.petcusto.app.ui.screens.BreedsScreen
import com.petcusto.app.ui.screens.CalculateScreen
import com.petcusto.app.ui.screens.LoginScreen
import com.petcusto.app.ui.screens.RegisterScreen
import com.petcusto.app.ui.screens.SavedScreen
import com.petcusto.app.ui.screens.SplashScreen
import com.petcusto.app.viewmodel.AppViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    vm: AppViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = SplashRoute,
        modifier = modifier
    ) {
        composable<SplashRoute> {
            val loggedIn by vm.isLoggedIn.collectAsStateWithLifecycle()
            SplashScreen(isLoggedIn = loggedIn) { isLogged ->
                navController.navigate(if (isLogged) BreedsRoute else LoginRoute) {
                    popUpTo<SplashRoute> { inclusive = true }
                }
            }
        }

        composable<LoginRoute> {
            LoginScreen(
                vm = vm,
                onLoggedIn = {
                    navController.navigate(BreedsRoute) {
                        popUpTo<LoginRoute> { inclusive = true }
                    }
                },
                onGoRegister = { navController.navigate(RegisterRoute) }
            )
        }

        composable<RegisterRoute> {
            RegisterScreen(
                vm = vm,
                onRegistered = {
                    navController.navigate(BreedsRoute) {
                        popUpTo<LoginRoute> { inclusive = true }
                    }
                },
                onGoLogin = { navController.popBackStack() }
            )
        }

        composable<BreedsRoute> {
            BreedsScreen(
                vm = vm,
                onBreedClick = { breed -> navController.navigate(BreedDetailRoute(breed.id)) }
            )
        }

        composable<BreedDetailRoute> { entry ->
            val args = entry.toRoute<BreedDetailRoute>()
            BreedDetailScreen(
                vm = vm,
                breedId = args.breedId,
                onBack = { navController.popBackStack() },
                onCalculate = {
                    navController.navigate(CalculateRoute) { launchSingleTop = true }
                }
            )
        }

        composable<CalculateRoute> {
            CalculateScreen(
                vm = vm,
                onSaved = {
                    navController.navigate(SavedRoute) {
                        popUpTo<BreedsRoute> { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        composable<SavedRoute> {
            SavedScreen(
                vm = vm,
                onLogout = {
                    vm.logout {
                        navController.navigate(LoginRoute) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }
                }
            )
        }
    }
}
