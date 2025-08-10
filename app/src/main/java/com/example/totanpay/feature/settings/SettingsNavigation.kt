package com.example.totanpay.feature.settings

import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import androidx.navigation.navOptions
import com.example.totanpay.MainRoute
import com.example.totanpay.R
import com.example.totanpay.feature.reports.DetailsTransactionReportScreen
import com.example.totanpay.feature.reports.LastTransactionReportScreen
import com.example.totanpay.feature.reports.MenuReportScreen
import com.example.totanpay.feature.reports.NotFoundResultScreen
import com.example.totanpay.feature.reports.ResultDetailsTransactionScreen
import com.example.totanpay.feature.reports.TransactionBasedOnTraceReportScreen
import com.example.totanpay.feature.settings.merchant.ApportionmentScreen
import com.example.totanpay.feature.settings.merchant.ChangeMerchantPasswordScreen
import com.example.totanpay.feature.settings.merchant.MerchantSettingsScreen
import com.example.totanpay.feature.settings.merchant.PrintSettingsScreen
import com.example.totanpay.feature.settings.supervisor.ConnectionSettingsScreen
import com.example.totanpay.feature.settings.supervisor.KeyInjectionScreen
import com.example.totanpay.feature.settings.supervisor.SupervisorSettingsScreen


fun NavGraphBuilder.settingNavigation(
    navController: NavHostController
) {
    navigation(
        route = "settings_navigation",
        startDestination = "menu_settings"
    ) {
        composable("menu_settings") {
            SettingsScreen(
                hiltViewModel(),
                onBackClicked = {
                    navController.navigate(MainRoute.MenuRoute.route) {
                        popUpTo("menu_settings") {
                            inclusive = true
                        }
                    }
                },
                onMerchantSettingsClicked = {
                    navController.navigate("merchantSettings") {
                        popUpTo("menu_settings") {
                            inclusive = true
                        }
                    }
                },
                onSupervisorSettingsClicked = {
                    navController.navigate("supervisorSettings") {
                        popUpTo("menu_settings") {
                            inclusive = true
                        }
                    }
                },
                onReportsClicked = {
                    navController.navigate(
                        "menuReport"
                    ) {
                        popUpTo("menu_settings") {
                            inclusive = true
                        }
                    }
                })
        }
        composable(route = "supervisorSettings") {
            SupervisorSettingsScreen(viewModel = hiltViewModel(),
                onBackClicked = {
                    navController.navigate("menu_settings") {
                        popUpTo("supervisorSettings") {
                            inclusive = true
                        }
                    }
                },
                onConnectionSettingsClicked = {
                    navController.navigate(
                        "connectionSettings",
                    ) {
                        popUpTo("supervisorSettings") {
                            inclusive = true
                        }
                    }
                },
                onKeyInjectionSettingsClicked = {
                    navController.navigate(
                        "keyInjection",
                    ) {
                        popUpTo("supervisorSettings") {
                            inclusive = true
                        }
                    }
                })
        }
        composable(route = "keyInjection") {
            KeyInjectionScreen(hiltViewModel(),
                onBackClicked = {
                    navController.navigate(
                        "supervisorSettings",
                    ) {
                        popUpTo("keyInjection") {
                            inclusive = true
                        }
                    }
                })
        }
        composable(route = "connectionSettings") {
            ConnectionSettingsScreen(
                hiltViewModel(),
                onBackClicked = {
                    navController.navigate("supervisorSettings") {
                        popUpTo("connectionSettings") {
                            inclusive = true
                        }
                    }
                })
        }
        composable(route = "merchantSettings") {
            MerchantSettingsScreen(
                hiltViewModel(),
                onBackClicked = {
                    navController.navigate(
                        "menu_settings"
                    ) {
                        popUpTo("merchantSettings") {
                            inclusive = true
                        }
                    }
                },
                onA = {
                    navController.navigate(
                        "apportionment"
                    ) {
                        popUpTo("merchantSettings") {
                            inclusive = true
                        }
                    }
                },
                onPrinterSettings = {
                    navController.navigate(
                        "print_settings"
                    ) {
                        popUpTo("merchantSettings") {
                            inclusive = true
                        }
                    }
                },
                onChangeMerchantPassword = {
                    navController.navigate(
                        "change_merchant_password"
                    ) {
                        popUpTo("merchantSettings") {
                            inclusive = true
                        }
                    }
                })

        }
        composable(route = "apportionment") {
            ApportionmentScreen(hiltViewModel()) {
                navController.navigate(
                    "merchantSettings"
                ) {
                    popUpTo("apportionment") {
                        inclusive = true
                    }
                }
            }
        }
        composable(route = "change_merchant_password") {
            ChangeMerchantPasswordScreen(
                hiltViewModel(), onBackClicked = {
                    navController.navigate(
                        "merchantSettings"
                    ) {
                        popUpTo("change_merchant_password") {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(route = "menuReport") {
            MenuReportScreen(
                onBackClicked = {
                    navController.navigate("menu_settings", navOptions {
                        launchSingleTop = true
                    })
                },
                onLastTransactionClicked = {
                    navController.navigate("lastTransactionReport", navOptions {
                        launchSingleTop = true
                    })
                },
                onDetailsOfTransactionsClicked = {
                    navController.navigate("detailsTransactionReport", navOptions {
                        launchSingleTop = true
                    })
                }) {
                navController.navigate("transactionBasedOnTransactionReport", navOptions {
                    launchSingleTop = true
                })
            }
        }
        composable(route = "lastTransactionReport") {
            LastTransactionReportScreen(
                viewModel = hiltViewModel(),
                onBackButtonClicked = { navController.popBackStack() })
        }
        composable(route = "detailsTransactionReport") {
            DetailsTransactionReportScreen(
                hiltViewModel(),
                onBackClicked = { navController.popBackStack() },
                onShowNotFound = {
                    navController.navigate("not_found_result", navOptions {
                        launchSingleTop = true
                    })
                }) { fromDate, toDate, fromAmount, toAmount, selectedTransaction ->
                navController.navigate("resultDetailsTransaction?fromDate=$fromDate&toDate=$toDate&fromAmount=$fromAmount&toAmount=$toAmount&selectedTransaction=$selectedTransaction",
                    navOptions {
                        launchSingleTop = true
                    })
            }
        }
        composable(route = "print_settings") {
            PrintSettingsScreen(hiltViewModel()) {
                navController.navigate(
                    "merchantSettings"
                ) {
                    popUpTo("print_settings") {
                        inclusive = true
                    }
                }
            }
        }
        composable(
            route = "resultDetailsTransaction?fromDate={fromDate}&toDate={toDate}&fromAmount={fromAmount}&toAmount={toAmount}&selectedTransaction={selectedTransaction}",
            arguments = listOf(
                navArgument("fromDate") {
                    type = androidx.navigation.NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument("toDate") {
                    type = androidx.navigation.NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument("fromAmount") {
                    type = androidx.navigation.NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument("toAmount") {
                    type = androidx.navigation.NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument("selectedTransaction") {
                    type = androidx.navigation.NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )) { backStack ->
            val fromDate: String? = backStack.arguments?.getString("fromDate")
            val toDate: String? = backStack.arguments?.getString("toDate")
            val fromAmount: String? = backStack.arguments?.getString("fromAmount")
            val toAmount: String? = backStack.arguments?.getString("toAmount")
            val selectedTransaction: String? = backStack.arguments?.getString("selectedTransaction")
            ResultDetailsTransactionScreen(
                hiltViewModel(),
                fromDate,
                toDate,
                fromAmount,
                toAmount,
                selectedTransaction
            ) {
                navController.popBackStack()
            }
        }
        composable(route = "transactionBasedOnTransactionReport") {
            TransactionBasedOnTraceReportScreen(
                hiltViewModel(), onBackButtonClicked = { navController.popBackStack() }
            )
        }
        composable(route = "not_found_result") {
            NotFoundResultScreen(title = stringResource(id = R.string.details_of_transactions),onBackClicked = {
                navController.navigate(
                    "transactionBasedOnTransactionReport"
                ) {
                    popUpTo("not_found_result") {
                        inclusive = true
                    }
                }
            })
        }
    }
}

sealed class SettingsRoutes(val route: String) {
    data object Start : SettingsRoutes("settings_navigation")
}


