package com.example.totanpay

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.totanpay.data.repository.device.IDevice
import com.example.totanpay.feature.SplashScreen
import com.example.totanpay.feature.balance.navigation.BalanceRoutes
import com.example.totanpay.feature.balance.navigation.balanceNavigation
import com.example.totanpay.feature.bill.navigation.BILL_PAYMENT_NAVIGATION
import com.example.totanpay.feature.bill.navigation.billNavigation
import com.example.totanpay.feature.charge.navigation.chargeNavigation
import com.example.totanpay.feature.purchase.navigation.PurchaseRoutes
import com.example.totanpay.feature.purchase.navigation.purchaseNavigation
import com.example.totanpay.feature.settings.SettingsRoutes
import com.example.totanpay.feature.settings.settingNavigation
import com.example.totanpay.feature.voucher.navigation.VoucherRoutes
import com.example.totanpay.ui.theme.TotanPayTheme
import com.example.totanpay.util.PermissionUtil
import com.example.totanpay.util.RuntimePermissionManager
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val TIME_TO_FINISH_SUCCESS_RESULT = 100000
const val TIME_TO_FINISH_TAKE_CARD = 30000


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var deviceManager: IDevice
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deviceManager.disableHome()
        val receivedAmount: String? = intent.getStringExtra("amount")
        val packageName: String? = intent.getStringExtra("packageName")
        enableEdgeToEdge()
        setContent {
            val sharedViewModel: MainViewModel = viewModel()
            val dataFlow by sharedViewModel.dataFlow.collectAsStateWithLifecycle()
            TotanPayTheme(darkTheme = dataFlow) {
                val systemUiController = rememberSystemUiController()
                if (dataFlow) {
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent
                    )
                } else {
                    systemUiController.setSystemBarsColor(
                        color = Color.White
                    )
                }
                CompositionLocalProvider(LocalDeviceManager provides deviceManager) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = if (receivedAmount.isNullOrEmpty()) {
                            MainRoute.SplashRoute.route
                        } else {
                            PurchaseRoutes.Start(
                                amount = receivedAmount,
                                purchaseId = receivedAmount,
                                packageName = packageName
                            ).route
                        }
                    ) {
                        composable(route = MainRoute.SplashRoute.route) {
                            SplashScreen {
                                navController.navigate(MainRoute.MenuRoute.route) {
                                    popUpTo(MainRoute.MenuRoute.route) { inclusive = false }
                                }
                            }
                        }
                        composable(MainRoute.MenuRoute.route) {
                            MenuScreen(viewModel = hiltViewModel(),
                                onPurchaseSelected = {
                                    navController.navigate("purchase_navigation", navOptions {
                                        launchSingleTop = true
                                    })
                                },
                                onBillPaySelected = {
                                    navController.navigate(BILL_PAYMENT_NAVIGATION, navOptions {
                                        launchSingleTop = true
                                    })
                                },
                                onBalanceSelected = {
                                    navController.navigate(
                                        BalanceRoutes.Start.route,
                                        navOptions {
                                            launchSingleTop = true
                                        })
                                },
                                onVoucherSelected = {
                                    navController.navigate(
                                        VoucherRoutes.Start.route,
                                        navOptions {
                                            launchSingleTop = true
                                        })
                                },
                                onTopUpSelected = {
                                    navController.navigate("charge_navigation", navOptions {
                                        launchSingleTop = true
                                    })
                                },
                                onSettingsClicked = {
                                    navController.navigate(
                                        SettingsRoutes.Start.route,
                                        navOptions {
                                            launchSingleTop = true
                                        })
                                },
                                onBackClicked = {
                                    finish()
                                })
                        }
                        purchaseNavigation(navController)
                        balanceNavigation(navController)
                        billNavigation(navController)
                        chargeNavigation(navController)
                        settingNavigation(navController)
                    }
                }
            }
        }
        requestPermission()
    }

    private var mRuntimePermissionManager: RuntimePermissionManager? = null
    private fun requestPermission() {
        mRuntimePermissionManager = RuntimePermissionManager(this)
        executeRequestPermissionTask(
            PermissionUtil.Permissions,
            object : RuntimePermissionManager.RequestPermissionCallback {
                override fun onCallback(
                    permisssions: Array<String?>?,
                    grantResults: IntArray?,
                    allGranted: Boolean
                ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if (!Environment.isExternalStorageManager()) {
                            val intent =
                                Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                            intent.setData(Uri.parse("package:$packageName"))
                            startActivityForResult(intent, 1024)
                            return
                        }
                    }
                    //finish()
                }// TODO:
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1024 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                finish()
            } else {
                finish()
            }
        }
    }

    fun executeRequestPermissionTask(
        permissions: Array<String?>?,
        callback: RuntimePermissionManager.RequestPermissionCallback?
    ) {
        mRuntimePermissionManager?.executeRequestPermissionTask(permissions, null, null, callback)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mRuntimePermissionManager?.handleReuqestPermissionResult(
            requestCode,
            permissions,
            grantResults
        )
    }
}

sealed class MainRoute(val route: String) {
    data object SplashRoute : MainRoute("splash")
    data object MenuRoute : MainRoute("menu")
}

