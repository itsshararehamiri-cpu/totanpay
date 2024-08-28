package com.example.totanpay.feature.bill

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.totanpay.feature.purchase.PurchaseSuccessResult
import com.example.totanpay.feature.purchase.PurchaseViewModel
import com.example.totanpay.feature.purchase.ReadCardScreen


fun NavGraphBuilder.billNavigation(
    navController: NavHostController
) {
    navigation(
        route = "bill_pay_navigation",
        startDestination = "main_bill"
    ) {
        composable(route = "main_bill") {
            MainBillScreen(hiltViewModel(), onConfirmBillAndPayId = { billId, payId ->
                navController.navigate("read_card")
            }, onBackClicked = {   navController.navigate("main") {
                popUpTo("main") { inclusive = true }
            } })
        }
        composable(route = "read_card/{amount}") { backStack ->
            val viewModel = hiltViewModel<com.example.totanpay.feature.purchase.ReadCardViewModel>()
            val amount = backStack.arguments?.getString("amount") ?: ""
            println("gggggggggg->$amount")
            ReadCardScreen(
                viewModel = viewModel, onGetTrack2 = { println("track2->$it")

                    navController.navigate("get_pin/$it/$amount")}
            ) {
                navController.navigate("main") {
                    popUpTo("main") { inclusive = true }
                }            }
        }
        composable(
            route = "get_pin/{track2}/{amount}"
        ) { backStack ->

            val viewModel = hiltViewModel<com.example.totanpay.feature.purchase.GetPinViewModel>()
            val track2 = backStack.arguments?.getString("track2") ?: ""
            val amount = backStack.arguments?.getString("amount") ?: ""
            println("sssssssssssss->$track2")
            println("sssssssssssss->$amount")

            com.example.totanpay.feature.purchase.GetPinScreen(
                amount = amount,
                track2 = backStack.arguments?.getString("track2") ?: "",
                viewModel = viewModel, onGoLoading = { amount, pinBlock ->
                    navController.navigate("loading/$track2/$amount/$pinBlock")}
            ) {
                navController.navigate("main") {
                    popUpTo("main") { inclusive = true }
                }            }
        }
        composable(
            route = "loading/{track2}/{amount}/{pinBlock}"
        ) { backStack ->

            val viewModel = hiltViewModel<PurchaseViewModel>()

//            LoadingScreen(
//                track2 = backStack.arguments?.getString("track2") ?: "",
//                amount = backStack.arguments?.getString("amount") ?: "",
//                pinBlock = backStack.arguments?.getString("pinBlock") ?: "",
//                viewModel = viewModel,
//            ) {
//                println("sssssssssssiuy->ssssssss")
//                navController.navigate("purchase_success_result/$it")
//            }
        }
        composable(
            route = "purchase_success_result/{response}"
        ) { backStack ->
            println("sssshhhhhhhhhsssssssiuiuy->ssssssss")

            PurchaseSuccessResult(
                viewModel = hiltViewModel(),
                response = backStack . arguments ?. getString ("response") ?: "",
            ){
                navController.navigate("main") {
                    popUpTo("main") { inclusive = true }
                }
            }
        }
    }
}
