package com.example.totanpay.feature.charge.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.totanpay.MainRoute
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.feature.balance.ReadCardViewModel
import com.example.totanpay.feature.charge.AmountScreen
import com.example.totanpay.feature.charge.SelectChargeTypeScreen
import com.example.totanpay.feature.charge.SelectOperatorScreen
import com.example.totanpay.feature.purchase.ReadCardScreen
import com.example.totanpay.feature.topup.GetPinScreen
import com.example.totanpay.feature.topup.LoadingScreen
import com.example.totanpay.feature.topup.TopUpSuccessResult
import com.example.totanpay.feature.topup.TopUpUnSuccessResult
import com.example.totanpay.feature.voucher.VoucherSuccessResult
import com.example.totanpay.feature.voucher.VoucherUnSuccessResult


fun NavGraphBuilder.chargeNavigation(
    navController: NavHostController
) {
    navigation(
        route = "charge_navigation", startDestination = "selectChargeType"
    ) {
        composable(route = "selectChargeType") {
            SelectChargeTypeScreen(onConfirm = { isTopUpSelected, mobile ->
                navController.navigate("selectOperator/$isTopUpSelected/$mobile")
            }, onBackButton = {
                navController.navigate(MainRoute.MenuRoute.route) {
                    popUpTo(MainRoute.MenuRoute.route) { inclusive = true }
                }
            })
        }
        composable(
            route = "selectOperator/{isTopUp}/{mobile}",
            arguments = listOf(navArgument("isTopUp") { type = NavType.BoolType },
                navArgument("mobile") {
                    type = NavType.StringType
                    nullable = true
                })
        ) {
            val mobile: String? = it.arguments?.getString("mobile")
            val isTopUp: Boolean = it.arguments?.getBoolean("isTopUp")!!
            SelectOperatorScreen(onConfirm = { operator ->
                navController.navigate("amount_route/$operator/$mobile/$isTopUp")

            }, onBackButton = {
                navController.navigate(MainRoute.MenuRoute.route) {
                    popUpTo(MainRoute.MenuRoute.route) { inclusive = true }
                }
            })
        }
        composable(
            route = "amount_route/{selectedOperator}/{mobile}/{isTopUp}",
            arguments = listOf(
                navArgument("selectedOperator") { type = NavType.StringType },
                navArgument("mobile") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("isTopUp") {
                    type = NavType.BoolType
                    nullable = false
                })
        ) {
            val mobile: String? = it.arguments?.getString("mobile")
            val operator = it.arguments?.getString("selectedOperator") ?: ""
            val isTopUp: Boolean = it.arguments?.getBoolean("isTopUp")!!
            AmountScreen(
                onConfirm = { amount ->
                    if (isTopUp)
                        navController.navigate("topup_read_card/$amount/$operator/$mobile")
                    else navController.navigate("voucher_read_card/$amount/$operator")
                },
                onBackButton = {
                    navController.navigate(MainRoute.MenuRoute.route) {
                        popUpTo(MainRoute.MenuRoute.route) { inclusive = true }
                    }
                })
        }
        composable(route = "voucher_read_card/{amount}/{operator}") { backStack ->
            val viewModel = hiltViewModel<ReadCardViewModel>()
            val amount = backStack.arguments?.getString("amount") ?: ""
            val operator = backStack.arguments?.getString("operator") ?: ""
            com.example.totanpay.feature.voucher.ReadCardScreen(
                viewModel = viewModel,
                amount = amount,
                onGetTrack2 = {
                    navController.navigate("voucher_get_pin/$it/$amount/$operator")
                }
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
            com.example.totanpay.feature.voucher.GetPinScreen(
                amount = amount,
                track2 = track2,
                operator = operator,
                viewModel = hiltViewModel(),
                onGoLoading = { pinBlock ->
                    navController.navigate("voucher_loading/$track2/$amount/$pinBlock/$operator")
                }
            ) {
                navController.navigate(MainRoute.MenuRoute.route) {
                    popUpTo(MainRoute.MenuRoute.route) { inclusive = true }
                }
            }
        }
        composable(
            route = "voucher_loading/{track2}/{amount}/{pinBlock}/{operator}"
        ) { backStack ->
            com.example.totanpay.feature.voucher.LoadingScreen(
                track2 = backStack.arguments?.getString("track2") ?: "",
                amount = backStack.arguments?.getString("amount") ?: "",
                pinBlock = backStack.arguments?.getString("pinBlock") ?: "",
                operator = backStack.arguments?.getString("operator") ?: "",
                viewModel = hiltViewModel(),
                onSuccessResult = { navController.navigate("voucher_success_result/$it") },
                onErrorResult = {
                    navController.navigate("voucher_unsuccess_result/$it")
                },
                onBackButtonClicked = {
                    navController.navigate(MainRoute.MenuRoute.route) {
                        popUpTo(MainRoute.MenuRoute.route) { inclusive = true }
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
        composable(
            route = "topup_read_card/{amount}/{operator}/{mobile}",
            arguments = listOf(navArgument("amount") { type = NavType.StringType },
                navArgument("operator") { type = NavType.StringType },
                navArgument("mobile") {
                    type = NavType.StringType
                    nullable = true
                })
        ) { backStack ->
            val amount = backStack.arguments?.getString("amount") ?: ""
            val operator = backStack.arguments?.getString("operator") ?: ""
            val mobile = backStack.arguments?.getString("mobile") ?: ""
            ReadCardScreen(viewModel = hiltViewModel<ReadCardViewModel>(),
                amount = amount,
                type = TransactionType.TOPUP,
                operator = operator,
                onGetTrack2 = { navController.navigate("get_pin/$it/$amount/$operator/$mobile") }) {
                navController.navigate(MainRoute.MenuRoute.route) {
                    popUpTo(MainRoute.MenuRoute.route) { inclusive = true }
                }
            }
        }
        composable(
            route = "get_pin/{track2}/{amount}/{operator}/{mobile}"
        ) { backStack ->

            val track2 = backStack.arguments?.getString("track2") ?: ""
            val amount = backStack.arguments?.getString("amount") ?: ""
            val operator = backStack.arguments?.getString("operator") ?: ""
            val mobile = backStack.arguments?.getString("mobile") ?: ""

            GetPinScreen(track2 = backStack.arguments?.getString("track2") ?: "",
                amount = amount,
                viewModel = hiltViewModel(),
                onGoLoading = { a, pb -> navController.navigate("topup_loading/$track2/$amount/$pb/$operator/$mobile") }) {
                navController.navigate(MainRoute.MenuRoute.route) {
                    popUpTo(MainRoute.MenuRoute.route) { inclusive = true }
                }
            }
        }
        composable(
            route = "topup_loading/{track2}/{amount}/{pinBlock}/{operator}/{mobile}"
        ) { backStack ->


            LoadingScreen(track2 = backStack.arguments?.getString("track2") ?: "",
                amount = backStack.arguments?.getString("amount") ?: "",
                pinBlock = backStack.arguments?.getString("pinBlock") ?: "",
                operator = backStack.arguments?.getString("operator") ?: "",
                mobile = backStack.arguments?.getString("mobile") ?: "",
                viewModel = hiltViewModel(),
                onSuccessResult = {
                    navController.navigate("topup_success_result/$it")
                },
                onErrorResult = {
                    navController.navigate("topup_unsuccess_result/$it")
                },
                onBackButtonClicked = {
                    navController.navigate(MainRoute.MenuRoute.route) {
                        popUpTo(MainRoute.MenuRoute.route) { inclusive = true }
                    }
                })
        }
        composable(
            route = "topup_success_result/{response}"
        ) { backStack ->
            TopUpSuccessResult(
                viewModel = hiltViewModel(),
                response = backStack.arguments?.getString("response") ?: "",
            ) {
                navController.navigate(MainRoute.MenuRoute.route) {
                    popUpTo(MainRoute.MenuRoute.route) { inclusive = true }
                }
            }
        }
        composable(
            route = "topup_unsuccess_result/{response}"
        ) { backStack ->
            TopUpUnSuccessResult(
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