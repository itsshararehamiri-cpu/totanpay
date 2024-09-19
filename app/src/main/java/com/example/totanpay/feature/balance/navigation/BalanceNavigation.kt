package com.example.totanpay.feature.balance.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.totanpay.MainRoute
import com.example.totanpay.feature.balance.BalanceSuccessResultScreen
import com.example.totanpay.feature.balance.BalanceUnSuccessResultScreen
import com.example.totanpay.feature.balance.BalanceViewModel
import com.example.totanpay.feature.balance.GetPinScreen
import com.example.totanpay.feature.balance.LoadingScreen
import com.example.totanpay.feature.balance.ReadCardScreen

fun NavGraphBuilder.balanceNavigation(
    navController: NavHostController
) {
    navigation(
        route = "balance_navigation",
        startDestination = "balance_read_card"
    ) {
        composable(route = "balance_read_card") {
            ReadCardScreen(
                viewModel = hiltViewModel(),
                onGetTrack2 = { navController.navigate("balance_get_pin/$it") }
            ) {
                navController.navigate(MainRoute.MenuRoute.route) {
                    popUpTo(MainRoute.MenuRoute.route) { inclusive = true }
                }
            }
        }
        composable(
            route = "balance_get_pin/{track2}"
        ) { backStack ->

            val track2 = backStack.arguments?.getString("track2") ?: ""
            GetPinScreen(
                track2 = backStack.arguments?.getString("track2") ?: "",
                viewModel = hiltViewModel(), onGoLoading = { pinBlock ->
                    navController.navigate("balance_loading/$track2/$pinBlock")
                }
            ) {
                navController.navigate(MainRoute.MenuRoute.route) {
                    popUpTo(MainRoute.MenuRoute.route) { inclusive = true }
                }
            }
        }
        composable(
            route = "balance_loading/{track2}/{pinBlock}"
        ) { backStack ->

            val viewModel = hiltViewModel<BalanceViewModel>()
            LoadingScreen(
                track2 = backStack.arguments?.getString("track2") ?: "",
                pinBlock = backStack.arguments?.getString("pinBlock") ?: "",
                viewModel = viewModel,
                onSuccessResult = {
                    navController.navigate("balance_success_result/$it")
                },
                onErrorResult = {
                    navController.navigate("balance_unsuccess_result/$it")
                },
                onBackButtonClicked = {
                    navController.navigate(MainRoute.MenuRoute.route) {
                        popUpTo(MainRoute.MenuRoute.route) { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = "balance_success_result/{response}"
        ) { backStack ->
            BalanceSuccessResultScreen(
                viewModel = hiltViewModel(),
                response = backStack.arguments?.getString("response") ?: "",
            ) {
                navController.navigate(MainRoute.MenuRoute.route) {
                    popUpTo(MainRoute.MenuRoute.route) { inclusive = true }
                }
            }
        }
        composable(
            route = "balance_unsuccess_result/{response}"
        ) { backStack ->
            BalanceUnSuccessResultScreen(
                viewModel = hiltViewModel(),
                response = backStack.arguments?.getString("response") ?: "",
            ) {
                navController.navigate(MainRoute.MenuRoute.route) {
                    popUpTo(MainRoute.MenuRoute.route) { inclusive = true }
                }
            }
        }
    }
}

sealed class BalanceRoutes(val route: String) {
    data object Start : BalanceRoutes("balance_navigation")

}
