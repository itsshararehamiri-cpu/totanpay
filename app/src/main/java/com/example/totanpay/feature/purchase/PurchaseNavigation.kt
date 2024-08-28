package com.example.totanpay.feature.purchase

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation


fun NavGraphBuilder.purchaseNavigation(
    navController: NavHostController
) {
    navigation(
        route = "purchase_navigation/{amount}", startDestination = "read_card/{amount}"
    ) {
        composable(route = "read_card/{amount}") { backStack ->
            val viewModel = hiltViewModel<ReadCardViewModel>()
            val amount = backStack.arguments?.getString("amount") ?: ""
            ReadCardScreen(viewModel = viewModel,
                onGetTrack2 = { navController.navigate("get_pin/$it/$amount") }) {
                navController.navigate("main") {
                    popUpTo("main") { inclusive = true }
                }
            }
        }
        composable(
            route = "get_pin/{track2}/{amount}"
        ) { backStack ->
            val viewModel = hiltViewModel<GetPinViewModel>()
            val track2 = backStack.arguments?.getString("track2") ?: ""
            val amount = backStack.arguments?.getString("amount") ?: ""
            GetPinScreen(amount = amount,
                track2 = backStack.arguments?.getString("track2") ?: "",
                viewModel = viewModel,
                onGoLoading = { amount, pinBlock ->
                    navController.navigate("loading/$track2/$amount/$pinBlock")
                }) {
                navController.navigate("main") {
                    popUpTo("main") { inclusive = true }
                }
            }
        }
        composable(
            route = "loading/{track2}/{amount}/{pinBlock}"
        ) { backStack ->
            val viewModel = hiltViewModel<PurchaseViewModel>()
            LoadingScreen(
                track2 = backStack.arguments?.getString("track2") ?: "",
                amount = backStack.arguments?.getString("amount") ?: "",
                pinBlock = backStack.arguments?.getString("pinBlock") ?: "",
                viewModel = viewModel,
                onSuccessResult = {
                    navController.navigate("purchase_success_result/$it") },
                onErrorResult = {
                    navController.navigate("purchase_unsuccess_result/$it")}
            ) {
                navController.navigate("main"){
                    popUpTo("main"){inclusive=true}
                }
            }
        }
        composable(
            route = "purchase_success_result/{response}"
        ) { backStack ->
            PurchaseSuccessResult(
                viewModel = hiltViewModel(),
                response = backStack.arguments?.getString("response") ?: "",
            ) {
                navController.navigate("main") {
                    popUpTo("main") { inclusive = true }
                }
            }
        }
        composable(
            route = "purchase_unsuccess_result/{response}"
        ) { backStack ->
            PurchaseUnSuccessResult(
                viewModel = hiltViewModel(),
                response = backStack.arguments?.getString("response") ?: "",
            ) {
                println("hggooohgytttoooooooooo->ssssssss")
                navController.navigate("main") {
                    popUpTo("main") { inclusive = true }
                }
            }
        }
    }
}