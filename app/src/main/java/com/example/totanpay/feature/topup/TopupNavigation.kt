package com.example.totanpay.feature.topup

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.totanpay.feature.balance.GetPinScreen
import com.example.totanpay.feature.balance.GetPinViewModel
import com.example.totanpay.feature.purchase.ReadCardScreen


fun NavGraphBuilder.topupNavigation(
    navController: NavHostController
) {
    navigation(
        route = "topup_navigation",
        startDestination = "topup"
    ) {
        composable(route = "topup") {
            MainTopupScreen(
            onConfirm = {phoneNumberValue,amountValue,operatorTypeValue->
                println("ooooooooooooooghhgg")
                navController.navigate("topup_read_card/$amountValue/$operatorTypeValue/$phoneNumberValue")

            }, onBackButton = {
                    navController.navigate("main") {
                        popUpTo("main") { inclusive = true }
                    }            })
        }
        composable(route = "topup_read_card/{amount}/{operator}/{mobile}") { backStack ->
            val amount = backStack.arguments?.getString("amount") ?: ""
            val operator = backStack.arguments?.getString("operator") ?: ""
            val mobile = backStack.arguments?.getString("mobile") ?: ""
            println("oooooooooooooohghhgg")
            ReadCardScreen(
                viewModel = hiltViewModel<com.example.totanpay.feature.purchase.ReadCardViewModel>()
            , onGetTrack2 = {  navController.navigate("get_pin/$it/$amount/$operator/$mobile")}){
                navController.navigate("main") {
                    popUpTo("main") { inclusive = true }
                }            }
        }
        composable(
            route = "get_pin/{track2}/{amount}/{operator}/{mobile}"
        ) { backStack ->

            val viewModel = hiltViewModel<GetPinViewModel>()
            val track2 = backStack.arguments?.getString("track2") ?: ""
            val amount = backStack.arguments?.getString("amount") ?: ""
            val operator = backStack.arguments?.getString("operator") ?: ""
            val mobile = backStack.arguments?.getString("mobile") ?: ""

            GetPinScreen(track2 = backStack.arguments?.getString("track2")?:"",
                viewModel = viewModel,
            ){
                navController.navigate("topup_loading/$track2/$amount/$it/$operator/$mobile")
            }
        }
        composable(
            route = "topup_loading/{track2}/{amount}/{pinBlock}/{operator}/{mobile}"
        ) { backStack ->


            LoadingScreen(
                track2 = backStack.arguments?.getString("track2") ?: "",
                amount = backStack.arguments?.getString("amount") ?: "",
                pinBlock = backStack.arguments?.getString("pinBlock") ?: "",
                operator = backStack.arguments?.getString("operator") ?: "",
                mobile = backStack.arguments?.getString("mobile") ?: "",
                viewModel = hiltViewModel(), onSuccessResult = {
                    navController.navigate("topup_success_result/$it")
                },
                onErrorResult = {
                    navController.navigate("topup_unsuccess_result/$it")
                }
            )
        }
        composable(
            route = "topup_success_result/{response}"
        ) { backStack ->
            println("sssshhhhhhhhhsssssssiukkky->ssssssss")

            TopupSuccessResult(
                viewModel = hiltViewModel(),
                response = backStack . arguments ?. getString ("response") ?: "",
            ){
                navController.navigate("main"){
                    popUpTo("main"){inclusive=true}
                }
            }
        }
        composable(
            route = "topup_unsuccess_result/{response}"
        ) { backStack ->
            println("sssshhhhhhhhhsssssssiukkky->ssssssss")

            TopupUnSuccessResult(
                viewModel = hiltViewModel(),
                response = backStack . arguments ?. getString ("response") ?: "",
            ){
                navController.navigate("main"){
                    popUpTo("main"){inclusive=true}
                }
            }
        }
    }
}
