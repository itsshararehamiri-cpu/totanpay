package com.example.totanpay.feature.voucher

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation


fun NavGraphBuilder.voucherNavigation(
    navController: NavHostController
) {
    navigation(
        route = "voucher_navigation",
        startDestination = "main_voucher"
    ) {
        composable(route = "main_voucher") {
            MainVoucherScreen(
                onConfirm = {amount,operator->
                   navController.navigate("voucher_read_card/$amount/$operator")
                }, onBackButtonClicked = {
                    navController.navigate("main") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            )
        }
        composable(route = "voucher_read_card/{amount}/{operator}") { backStack ->
            val viewModel = hiltViewModel<ReadCardViewModel>()
            val amount = backStack.arguments?.getString("amount") ?: ""
            val operator = backStack.arguments?.getString("operator") ?: ""
            ReadCardScreen(
                viewModel = viewModel, onGetTrack2 = {println("track2->$it")
                    navController.navigate("voucher_get_pin/$it/$amount/$operator")}
            ) {
                navController.navigate("main") {
                    popUpTo("main") { inclusive = true }
                }
            }
        }
        composable(
            route = "voucher_get_pin/{track2}/{amount}/{operator}"
        ) { backStack ->
            val track2 = backStack.arguments?.getString("track2") ?: ""
            val amount = backStack.arguments?.getString("amount") ?: ""
            val operator = backStack.arguments?.getString("operator") ?: ""
          GetPinScreen(
                amount = amount,
                track2 = track2,
                operator=operator,
                viewModel = hiltViewModel(),
            ) { pinBlock ->
                navController.navigate("voucher_loading/$track2/$amount/$pinBlock/101")
            }
        }
        composable(
            route = "voucher_loading/{track2}/{amount}/{pinBlock}/{operator}"
        ) { backStack ->
            LoadingScreen(
                track2 = backStack.arguments?.getString("track2") ?: "",
                amount = backStack.arguments?.getString("amount") ?: "",
                pinBlock = backStack.arguments?.getString("pinBlock") ?: "",
                operator = backStack.arguments?.getString("operator") ?: "",
                viewModel = hiltViewModel(),
                onSuccessResult = { navController.navigate("voucher_success_result/$it") },
                onErrorResult = {
                    navController.navigate("voucher_unsuccess_result/$it")}
            )
        }
        composable(
            route = "voucher_success_result/{response}"
        ) { backStack ->
            VoucherSuccessResult(
                viewModel = hiltViewModel(),
                response = backStack . arguments ?. getString ("response") ?: "",
            ){
                navController.navigate("main"){
                    popUpTo("main"){inclusive=true}
                }
            }
        }
        composable(
            route = "voucher_unsuccess_result/{response}"
        ) { backStack ->
            VoucherUnSuccessResult(
                viewModel = hiltViewModel(),
                response = backStack.arguments?.getString("response") ?: "",
            ) {
                navController.navigate("main") {
                    popUpTo("main") { inclusive = true }
                }
            }
        }
    }
}
