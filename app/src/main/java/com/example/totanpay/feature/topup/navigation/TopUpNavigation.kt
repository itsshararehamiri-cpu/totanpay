package com.example.totanpay.feature.topup.navigation


import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.totanpay.MainRoute
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.feature.balance.ReadCardViewModel
import com.example.totanpay.feature.purchase.ReadCardScreen
import com.example.totanpay.feature.topup.GetPinScreen
import com.example.totanpay.feature.topup.LoadingScreen
import com.example.totanpay.feature.topup.MainTopUpScreen
import com.example.totanpay.feature.topup.TopUpSuccessResult
import com.example.totanpay.feature.topup.TopUpUnSuccessResult


fun NavGraphBuilder.topUpNavigation(
    navController: NavHostController
) {
    navigation(
        route = "topup_navigation",
        startDestination = "topup"
    ) {
        composable(route = "topup") {
            MainTopUpScreen(
                onConfirm = {
                        phoneNumberValue,amountValue,operatorTypeValue->
                    navController.navigate("topup_read_card/$amountValue/$operatorTypeValue/$phoneNumberValue")

                }, onBackButton = {
                    navController.navigate(MainRoute.MenuRoute.route) {
                        popUpTo(MainRoute.MenuRoute.route) { inclusive = true }
                    }            })
        }
        composable(route = "topup_read_card/{amount}/{operator}/{mobile}") { backStack ->
            val amount = backStack.arguments?.getString("amount") ?: ""
            val operator = backStack.arguments?.getString("operator") ?: ""
            val mobile = backStack.arguments?.getString("mobile") ?: ""
            ReadCardScreen(
                viewModel = hiltViewModel<ReadCardViewModel>()
                ,amount=amount,
                type = TransactionType.TOPUP,
                operator=operator,
                onGetTrack2 = {  navController.navigate("get_pin/$it/$amount/$operator/$mobile")}){
                navController.navigate(MainRoute.MenuRoute.route) {
                    popUpTo(MainRoute.MenuRoute.route) { inclusive = true }
                }            }
        }
        composable(
            route = "get_pin/{track2}/{amount}/{operator}/{mobile}"
        ) { backStack ->

            val track2 = backStack.arguments?.getString("track2") ?: ""
            val amount = backStack.arguments?.getString("amount") ?: ""
            val operator = backStack.arguments?.getString("operator") ?: ""
            val mobile = backStack.arguments?.getString("mobile") ?: ""

            GetPinScreen(track2 = backStack.arguments?.getString("track2")?:"", amount = amount,
                viewModel = hiltViewModel(), onGoLoading = {a,pb->navController.navigate("topup_loading/$track2/$amount/$pb/$operator/$mobile")}
            ){
                navController.navigate(MainRoute.MenuRoute.route) {
                    popUpTo(MainRoute.MenuRoute.route) { inclusive = true }
                }
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
                },
                onBackButtonClicked = {
                    navController.navigate(MainRoute.MenuRoute.route){
                        popUpTo(MainRoute.MenuRoute.route){inclusive=true}
                    }
                }
            )
        }
        composable(
            route = "topup_success_result/{response}"
        ) { backStack ->
            TopUpSuccessResult(
                viewModel = hiltViewModel(),
                response = backStack . arguments ?. getString ("response") ?: "",
            ){
                navController.navigate(MainRoute.MenuRoute.route){
                    popUpTo(MainRoute.MenuRoute.route){inclusive=true}
                }
            }
        }
        composable(
            route = "topup_unsuccess_result/{response}"
        ) { backStack ->
            TopUpUnSuccessResult(
                viewModel = hiltViewModel(),
                response = backStack . arguments ?. getString ("response") ?: "",
            ){
                navController.navigate(MainRoute.MenuRoute.route){
                    popUpTo(MainRoute.MenuRoute.route){inclusive=true}
                }
            }
        }
    }
}
sealed class TopUpRoutes(val route:String){
    data object Start: TopUpRoutes("topup_navigation")
}

