package com.example.totanpay.feature.purchase.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.totanpay.MainRoute
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.feature.purchase.GetPinScreen
import com.example.totanpay.feature.purchase.GetPinViewModel
import com.example.totanpay.feature.purchase.LoadingScreen
import com.example.totanpay.feature.purchase.PurchaseIdScreen
import com.example.totanpay.feature.purchase.PurchaseScreen
import com.example.totanpay.feature.purchase.PurchaseSuccessResult
import com.example.totanpay.feature.purchase.PurchaseUnSuccessResult
import com.example.totanpay.feature.purchase.PurchaseViewModel
import com.example.totanpay.feature.purchase.ReadCardScreen

fun NavGraphBuilder.purchaseNavigation(
    navController: NavHostController
) {
    navigation(
        route = "purchase_navigation?packageName={packageName}",
        startDestination = "purchase_route",
        arguments = listOf(
            navArgument("packageName") {
                type = NavType.StringType
                nullable = true
            }

        )
    ) {
        composable(route = "purchase_route?packageName={packageName}",
            arguments = listOf(
                navArgument("packageName") {
                    type = NavType.StringType
                    nullable = true
                }
            )) { backStack ->
            val packageName = backStack.arguments?.getString("packageName")
            PurchaseScreen(onPurchaseSelected = { amount, purchaseIdEnabled ->
                if (purchaseIdEnabled) {
                    navController.navigate("purchase_id_route?amount=$amount&packageName=$packageName")
                } else {
                    navController.navigate("read_card?amount=$amount&packageName=$packageName")

                }
            }) {
                navController.navigate(MainRoute.MenuRoute.route) {
                    popUpTo(MainRoute.MenuRoute.route) { inclusive = true }
                }
            }
        }
        composable(route = "purchase_id_route?amount={amount}&packageName={packageName}",
            arguments = listOf(
                navArgument("amount") {
                    type = NavType.StringType
                    nullable = false
                },
                navArgument("packageName") {
                    type = NavType.StringType
                    nullable = true
                }
            )) { backStack ->
            val packageName = backStack.arguments?.getString("packageName")
            val amount = backStack.arguments?.getString("amount")
            PurchaseIdScreen(onBackButtonClicked = {
                navController.navigate(MainRoute.MenuRoute.route) {
                    popUpTo(MainRoute.MenuRoute.route) { inclusive = true }
                }
            }, onConfirmButtonClicked = {
                navController.navigate("read_card?amount=$amount&purchaseId=$it&packageName=$packageName")
            })
        }
        composable(route = "read_card?amount={amount}&c={purchaseId}&packageName={packageName}") { backStack ->
            val amount = backStack.arguments?.getString("amount") ?: ""
            val purchaseId = backStack.arguments?.getString("purchaseId") ?: ""
            val packageName = backStack.arguments?.getString("packageName")
            ReadCardScreen(viewModel = hiltViewModel(),
                amount = amount,
                operator = null,
                type = TransactionType.PURCHASE,
                onGetTrack2 = { navController.navigate("get_pin?amount=$amount&purchaseId=$purchaseId&track2=$it&packageName=$packageName") }) {
                navController.navigate(MainRoute.MenuRoute.route) {
                    popUpTo(MainRoute.MenuRoute.route) { inclusive = true }
                }
            }
        }
        composable(
            route = "get_pin?amount={amount}&purchaseId={purchaseId}&track2={track2}&packageName={packageName}",
            arguments = listOf(
                navArgument("amount") {
                    type = NavType.StringType
                    nullable = false
                },
                navArgument("purchaseId") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("track2") {
                    type = NavType.StringType
                    nullable = false
                },
                navArgument("packageName") {
                    type = NavType.StringType
                    nullable = true
                },

                )
        ) { backStack ->
            val viewModel = hiltViewModel<GetPinViewModel>()
            val track2 = backStack.arguments?.getString("track2") ?: ""
            val amount = backStack.arguments?.getString("amount") ?: ""
            val purchaseId = backStack.arguments?.getString("purchaseId") ?: ""
            val packageName = backStack.arguments?.getString("packageName")
            GetPinScreen(amount = amount,
                track2 = backStack.arguments?.getString("track2") ?: "",
                viewModel = viewModel,
                onGoLoading = { _, pinBlock ->
                    navController.navigate("loading?track2=$track2&amount=$amount&pinBlock=$pinBlock&purchaseId=$purchaseId&packageName=$packageName")
                }) {
                navController.navigate(MainRoute.MenuRoute.route) {
                    popUpTo(MainRoute.MenuRoute.route) { inclusive = true }
                }
            }
        }
        composable(route = "loading?amount={amount}&purchaseId={purchaseId}&track2={track2}&pinBlock={pinBlock}&packageName={packageName}",
            arguments = listOf(navArgument("amount") {
                type = NavType.StringType
                nullable = false
            }, navArgument("purchaseId") {
                type = NavType.StringType
                nullable = true
            }, navArgument("track2") {
                type = NavType.StringType
                nullable = false
            }, navArgument("pinBlock") {
                type = NavType.StringType
                nullable = false
            }, navArgument("packageName") {
                type = NavType.StringType
                nullable = true
            })
        ) { backStack ->
            val viewModel = hiltViewModel<PurchaseViewModel>()
            val packageName = backStack.arguments?.getString("packageName") ?: ""
            LoadingScreen(track2 = backStack.arguments?.getString("track2") ?: "",
                amount = backStack.arguments?.getString("amount") ?: "",
                pinBlock = backStack.arguments?.getString("pinBlock") ?: "",
                purchaseId = backStack.arguments?.getString("purchaseId") ?: "",
                viewModel = viewModel,
                onSuccessResult = {
                    navController.navigate("purchase_success_result?response=$it&packageName=$packageName")
                },
                onErrorResult = {
                    navController.navigate("purchase_unsuccess_result?response=$it&packageName=$packageName")
                }) {
                navController.navigate(MainRoute.MenuRoute.route) {
                    popUpTo(MainRoute.MenuRoute.route) { inclusive = true }
                }
            }
        }
        composable(route = "purchase_success_result?response={response}&packageName={packageName}",
            arguments = listOf(navArgument("response") {
                type = NavType.StringType
                nullable = false
            }, navArgument("packageName") {
                type = NavType.StringType
                nullable = true
            })
        ) { backStack ->
            PurchaseSuccessResult(
                viewModel = hiltViewModel(),
                response = backStack.arguments?.getString("response") ?: "",
                packageName = backStack.arguments?.getString("packageName"),
            ) {
                navController.navigate(MainRoute.MenuRoute.route) {
                    popUpTo(MainRoute.MenuRoute.route) { inclusive = true }
                }
            }
        }
        composable(route = "purchase_unsuccess_result?response={response}&packageName={packageName}",
            arguments = listOf(navArgument("response") {
                type = NavType.StringType
                nullable = false
            }, navArgument("packageName") {
                type = NavType.StringType
                nullable = true
            })
        ) { backStack ->
            PurchaseUnSuccessResult(
                viewModel = hiltViewModel(),
                response = backStack.arguments?.getString("response") ?: "",
                packageName = backStack.arguments?.getString("packageName"),
            ) {
                navController.navigate(MainRoute.MenuRoute.route) {
                    popUpTo(MainRoute.MenuRoute.route) { inclusive = true }
                }
            }
        }
    }
}

sealed class PurchaseRoutes(val route: String) {
    class Start(val amount: String, val purchaseId: String?, val packageName: String?) :
        PurchaseRoutes("purchase_navigation?amount=$amount&purchaseId=$purchaseId&packageName=$packageName")
}
