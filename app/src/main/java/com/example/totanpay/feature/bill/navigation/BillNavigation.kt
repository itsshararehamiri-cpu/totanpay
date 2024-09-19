package com.example.totanpay.feature.bill.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.totanpay.MainRoute
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.feature.balance.ReadCardViewModel
import com.example.totanpay.feature.bill.BillInquiryResultScreen
import com.example.totanpay.feature.bill.BillPaymentSuccessResult
import com.example.totanpay.feature.bill.BillPaymentUnSuccessResult
import com.example.totanpay.feature.bill.GetPinScreen
import com.example.totanpay.feature.bill.GetPinViewModel
import com.example.totanpay.feature.bill.Loading2Screen
import com.example.totanpay.feature.bill.LoadingScreen
import com.example.totanpay.feature.bill.MainBillScreen
import com.example.totanpay.feature.purchase.ReadCardScreen

const val BILL_PAYMENT_NAVIGATION = "bill_payment_navigation"
const val LOADING = "loading"
const val BILL_INQUIRY_RESULT = "bill_inquiry_result"
const val BILL_INQUIRY_UNSUCCESS_RESULT = "bill_inquiry_unsuccess_result"
const val READ_CARD_BILL = "read_card_bill"
const val GET_PIN_BILL = "get_pin_bill"
const val BILL_SUCCESS_RESULT = "bill_success_result"
const val BILL_UNSUCCESS_RESULT = "bill_unsuccess_result"

fun NavGraphBuilder.billNavigation(
    navController: NavHostController
) {
    navigation(
        route = BILL_PAYMENT_NAVIGATION,
        startDestination = BillRoute.StartRoute.route
    ) {
        composable(route = BillRoute.StartRoute.route) {
            MainBillScreen(
                viewModel = hiltViewModel(),
                onConfirmBillAndPaymentId = { billId, payId ->
                    navController.navigate("${LOADING}/$billId/$payId")
                },
                onBackClicked = {
                    navController.navigate(MainRoute.MenuRoute.route) {
                        popUpTo(MainRoute.MenuRoute.route) { inclusive = true }
                    }
                })
        }
        composable(route = BillRoute.LoadingRoute.route) { backStack ->
            val billId = backStack.arguments?.getString("billId") ?: ""
            val paymentId = backStack.arguments?.getString("paymentId") ?: ""
            LoadingScreen(
                hiltViewModel(), billId = billId, paymentId = paymentId,
                onSuccessResult = {
                    navController.navigate("$BILL_INQUIRY_RESULT/$it/$billId/$paymentId")
                }) {
                navController.navigate("$BILL_INQUIRY_UNSUCCESS_RESULT/$it")
            }
        }
        composable(route = BillRoute.BillInquiryResultRoute.route) { backStack ->
            val billInquiryResult = backStack.arguments?.getString("bill_inquiry_result") ?: ""
            val billId = backStack.arguments?.getString("billId") ?: ""
            val paymentId = backStack.arguments?.getString("paymentId") ?: ""
            BillInquiryResultScreen(
                hiltViewModel(), billInquiryResult = billInquiryResult,
                billId = billId, paymentId = paymentId,
                onPayment = { _, _, amount ->
                    navController.navigate("$READ_CARD_BILL/$amount/$billId/$paymentId")
                }) {
//                navController.navigate(MainRoute.MenuRoute.route) {
//                    popUpTo(MainRoute.MenuRoute.route) { inclusive = true }
//                }
                navController.popBackStack(MainRoute.MenuRoute.route, inclusive = false)

            }
        }
        composable(route = BillRoute.ReadCardBillRoute.route) { backStack ->
            val viewModel = hiltViewModel<ReadCardViewModel>()
            val amount = backStack.arguments?.getString("amount") ?: ""
            val billId = backStack.arguments?.getString("billId") ?: ""
            val paymentId = backStack.arguments?.getString("paymentId") ?: ""
            ReadCardScreen(
                viewModel = viewModel,
                amount = amount,
                type = TransactionType.BILL_PAY,
                operator = null,
                onGetTrack2 = {
                    navController.navigate("$GET_PIN_BILL/$it/$amount/$billId/$paymentId")
                }) {
                navController.popBackStack(MainRoute.MenuRoute.route, inclusive = false)
            }
        }
        composable(
            route = BillRoute.GetPinBillRoute.route
        ) { backStack ->
            val viewModel = hiltViewModel<GetPinViewModel>()
            val track2 = backStack.arguments?.getString("track2") ?: ""
            val amount = backStack.arguments?.getString("amount") ?: ""
            val billId = backStack.arguments?.getString("billId") ?: ""
            val paymentId = backStack.arguments?.getString("paymentId") ?: ""
            GetPinScreen(
                track2 = backStack.arguments?.getString("track2") ?: "",
                viewModel = viewModel, onGoLoading = { pinBlock ->
                    navController.navigate("$LOADING/$track2/$amount/$pinBlock/$billId/$paymentId")
                }) {
                navController.navigate(MainRoute.MenuRoute.route) {
                    popUpTo(MainRoute.MenuRoute.route) { inclusive = true }
                }
            }
        }
        composable(
            route = BillRoute.LoadingBillPayRoute.route
        ) { backStack ->
            Loading2Screen(
                track2 = backStack.arguments?.getString("track2") ?: "",
                amount = backStack.arguments?.getString("amount") ?: "",
                pinBlock = backStack.arguments?.getString("pinBlock") ?: "",
                billId = backStack.arguments?.getString("billId") ?: "",
                paymentId = backStack.arguments?.getString("paymentId") ?: "",
                viewModel = hiltViewModel(),
                onSuccessResult = { navController.navigate("$BILL_SUCCESS_RESULT/$it") }) {
                navController.navigate("$BILL_UNSUCCESS_RESULT/$it")
            }
        }
        composable(
            route = BillRoute.BillSuccessResultRoute.route
        ) { backStack ->
            BillPaymentSuccessResult(
                viewModel = hiltViewModel(),
                response = backStack.arguments?.getString("response") ?: "",
            ) {
                navController.navigate(MainRoute.MenuRoute.route) {
                    popUpTo(MainRoute.MenuRoute.route) { inclusive = true }
                }
            }
        }
        composable(
            route = BillRoute.BillUnSuccessResultRoute.route
        ) { backStack ->
            BillPaymentUnSuccessResult(
                viewModel = hiltViewModel(),
                response = backStack.arguments?.getString("response") ?: ""
            ) {
                navController.navigate(MainRoute.MenuRoute.route) {
                    popUpTo(MainRoute.MenuRoute.route) { inclusive = true }
                }
            }
        }
        composable(
            route = BillRoute.BillInquiryUnSuccessResultRoute.route
        ) { backStack ->
            BillPaymentUnSuccessResult(
                viewModel = hiltViewModel(),
                response = backStack.arguments?.getString("response") ?: ""
            ) {
                navController.navigate(MainRoute.MenuRoute.route) {
                    popUpTo(MainRoute.MenuRoute.route) { inclusive = true }
                }
            }
        }
    }
}

sealed class BillRoute(val route: String) {
    data object StartRoute : BillRoute("start")
    data object LoadingRoute : BillRoute("$LOADING/{billId}/{paymentId}")
    data object BillInquiryResultRoute :
        BillRoute("$BILL_INQUIRY_RESULT/{bill_inquiry_result}/{billId}/{paymentId}")

    data object ReadCardBillRoute : BillRoute("$READ_CARD_BILL/{amount}/{billId}/{paymentId}")
    data object GetPinBillRoute : BillRoute("$GET_PIN_BILL/{track2}/{amount}/{billId}/{paymentId}")
    data object LoadingBillPayRoute :
        BillRoute("loading/{track2}/{amount}/{pinBlock}/{billId}/{paymentId}")

    data object BillSuccessResultRoute : BillRoute("$BILL_SUCCESS_RESULT/{response}")
    data object BillUnSuccessResultRoute : BillRoute("$BILL_UNSUCCESS_RESULT/{response}")
    data object BillInquiryUnSuccessResultRoute :
        BillRoute("$BILL_INQUIRY_UNSUCCESS_RESULT/{response}")
}
