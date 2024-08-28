package com.example.totanpay.feature.settings

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.totanpay.feature.reports.DetailsTransactionReportScreen
import com.example.totanpay.feature.reports.LastTransactionReportScreen
import com.example.totanpay.feature.reports.MeunReportScreen
import com.example.totanpay.feature.reports.TransactionBasedOnTraceReportContentPreview
import com.example.totanpay.feature.reports.TransactionBasedOnTraceReportScreen


fun NavGraphBuilder.settingNavigation(
    navController: NavHostController
) {
    navigation(
        route = "settings_navigation",
        startDestination = "menu_settings"
    ) {
        composable("menu_settings") {
            SettingsScreen(
                onBackClicked = {navController.popBackStack()  },
                onMerchantSettingsClicked = { navController.navigate("merchantSettings") },
                onSupervisorSettingsClicked = { navController.navigate("supervisorSettings")})
        }
        composable(route = "supervisorSettings") {
            SupervisorSettingsScreen(viewModel = hiltViewModel(),
                onBackClicked = { navController.popBackStack() },
                onConnectionSettingsClicked = { navController.navigate("connectionSettings") })
        }
        composable(route = "connectionSettings") {
            ConnectionSettingsScreen(hiltViewModel(),onBackClicked = {navController.popBackStack()})
        }
        composable(route = "merchantSettings") {
            MerchantSettingsScreen(onBackClicked = {navController.popBackStack()}, onReportClicked = {
                navController.navigate("menuReport")
            })
        }

        composable(route = "menuReport") {
            MeunReportScreen(
                onBackClicked = { navController.popBackStack() },
                onLastTransactionClicked = { navController.navigate("lastTransactionReport") },
                onDetailsOfTransactionsClicked = { navController.navigate("detailsTransactionReport") }) {
                navController.navigate("transactionBasedOnTransactionReport")
            }
        }
        composable(route = "lastTransactionReport") {
            LastTransactionReportScreen(viewModel = hiltViewModel(), onBackButtonClicked = {navController.popBackStack()})
        }
        composable(route = "detailsTransactionReport") {
            DetailsTransactionReportScreen(onBackClicked = {navController.popBackStack()})
        }
        composable(route = "transactionBasedOnTransactionReport") {
            TransactionBasedOnTraceReportScreen(
                hiltViewModel(), onBackClicked = {navController.popBackStack()}
            )
        }
    }
}
