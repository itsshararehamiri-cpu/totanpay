package com.example.totanpay.feature.voucher.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.totanpay.MainRoute
import com.example.totanpay.feature.balance.ReadCardViewModel
import com.example.totanpay.feature.voucher.GetPinScreen
import com.example.totanpay.feature.voucher.LoadingScreen
import com.example.totanpay.feature.voucher.ReadCardScreen
import com.example.totanpay.feature.voucher.VoucherSuccessResult
import com.example.totanpay.feature.voucher.VoucherUnSuccessResult
import com.example.totanpay.feature.voucher.MainVoucherScreen


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
                    navController.navigate(MainRoute.MenuRoute.route) {
                        popUpTo(MainRoute.MenuRoute.route) { inclusive = true }
                    }
                }
            )
        }
        composable(route = "voucher_read_card/{amount}/{operator}") { backStack ->
            val viewModel = hiltViewModel<ReadCardViewModel>()
            val amount = backStack.arguments?.getString("amount") ?: ""
            val operator = backStack.arguments?.getString("operator") ?: ""
            ReadCardScreen(
                viewModel = viewModel,
                amount=amount,
                onGetTrack2 = {
                    navController.navigate("voucher_get_pin/$it/$amount/$operator")}
            ) {
                navController.navigate(MainRoute.MenuRoute.route) {
                    popUpTo(MainRoute.MenuRoute.route) { inclusive = true }
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
                onGoLoading = {
                        pinBlock ->
                    navController.navigate("voucher_loading/$track2/$amount/$pinBlock/$operator")
                }
            ) {
                navController.navigate(MainRoute.MenuRoute.route){
                    popUpTo(MainRoute.MenuRoute.route){inclusive=true}
                }
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
                    navController.navigate("voucher_unsuccess_result/$it")},
                onBackButtonClicked = {
                    navController.navigate(MainRoute.MenuRoute.route){
                        popUpTo(MainRoute.MenuRoute.route){inclusive=true}
                    }
                }
            )
        }
        composable(
            route = "voucher_success_result/{response}"
        ) { backStack ->
            VoucherSuccessResult(
                viewModel = hiltViewModel(),
                response = backStack . arguments ?. getString ("response") ?: "",
            ){
                navController.navigate(MainRoute.MenuRoute.route){
                    popUpTo(MainRoute.MenuRoute.route){inclusive=true}
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
                navController.navigate(MainRoute.MenuRoute.route) {
                    popUpTo(MainRoute.MenuRoute.route) { inclusive = true }
                }
            }
        }
    }
}

sealed class VoucherRoutes(val route:String){
    data object Start: VoucherRoutes("voucher_navigation")
}

