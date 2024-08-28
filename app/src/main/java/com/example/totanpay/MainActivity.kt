package com.example.totanpay

//import com.example.totanpay.feature.balance.ReadCardScreen
//import com.example.totanpay.feature.settings.SettingsScreen
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.totanpay.data.repository.IDevice
import com.example.totanpay.data.util.getPersianDate
import com.example.totanpay.feature.balance.balanceNavigation
import com.example.totanpay.feature.bill.billNavigation
import com.example.totanpay.feature.purchase.purchaseNavigation
import com.example.totanpay.feature.settings.settingNavigation
import com.example.totanpay.feature.topup.topupNavigation
import com.example.totanpay.feature.voucher.voucherNavigation
import com.example.totanpay.ui.theme.TotanPayTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var deviceManager: IDevice
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
      val v=  getPersianDate("0830")
        println("ssssssssssss->$v")
//        lifecycleScope.launch {
//            withContext(Dispatchers.IO) {
//                val sharedPreferces = getSharedPreferences("shoppin_pref", Context.MODE_PRIVATE)
//               val tranLogon = TranLogon(
//                   SipaMacGeneratorMAg(5),
//                   NetworkConnection("87.107.134.136", 40800),sharedPreferces
//               )
////               var tranResp = tranLogon.doTran(TranReq(1))
////               val tranInit = TranInit(
////                   SipaMacGeneratorMAg(5),
////                   NetworkConnection("87.107.134.136", 40800),sharedPreferces
////               )
////           val     tranResp = tranInit.doTran(TranReq(1))
////               //delay(60000)
////               Log.d("TAG", "onCreate: kjhghj")
//
//            }
//        }
        setContent {
            TotanPayTheme {
                ScreenSizeInfo()
                val configuration = LocalConfiguration.current
                val density = LocalDensity.current

                // Get the screen width in pixels
                val screenWidthPx = configuration.screenWidthDp * density.density

                // Get the screen height in pixels
                val screenHeightPx = configuration.screenHeightDp * density.density
                println("yyyyyyyyyy->$screenWidthPx")
                println("yyyyyyyyyy->$screenHeightPx")

//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//
//                }
//                        SettingsScreen(viewModel = hiltViewModel())
//
                CompositionLocalProvider(LocalDeviceManager provides deviceManager) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "main"
                    ) {
//                    composable("main") {
//                        SettingsScreen(viewModel = hiltViewModel())
//                    }
                        composable("main") {
                            MainScreen(viewModel = hiltViewModel(), onPurchaseSelected = {
                                println("hhhhhonPurchaseSelectedhhhhhh$it")
                                navController.navigate("purchase_navigation/$it")
                            },
                                onBillPaySelected = { navController.navigate("bill_pay_navigation") },
                                onBalanceSelected = {
                                    println("hhhhhhhhhhhhgggg")
                                    navController.navigate("balance_navigation")
                                },
                                onVoucherSelected = { navController.navigate("voucher_navigation") },
                                onTopupSelected = { navController.navigate("topup_navigation") },
                                onSettingsClicked = {
                                    navController.navigate("settings_navigation")
                                })
                        }
                        purchaseNavigation(navController)
                        balanceNavigation(navController)
                        billNavigation(navController)
                        voucherNavigation(navController)
                        topupNavigation(navController)
                        settingNavigation(navController)

                    }
                }

            }
        }
    }

}

@Composable
fun ScreenSizeInfo() {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    // Get the screen width in pixels
    val screenWidthPx = configuration.screenWidthDp * density.density

    // Get the screen height in pixels
    val screenHeightPx = configuration.screenHeightDp * density.density

    // Convert screen size to dp
    val screenWidthDp = configuration.screenWidthDp
    val screenHeightDp = configuration.screenHeightDp

    // Convert screen size to cm
    val screenWidthCm = screenWidthPx / (density.density * 10)
    val screenHeightCm = screenHeightPx / (density.density * 10)

    println("Screen size:")
    println("- $screenWidthDp x $screenHeightDp dp")
    println("- ${"%.2f".format(screenWidthCm)} x ${"%.2f".format(screenHeightCm)} cm")
}

