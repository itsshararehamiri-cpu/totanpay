package com.example.totanpay.data.repository


import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.totanpay.data.repository.datasource.LanguageLocalDataSource
import com.example.totanpay.data.repository.datasource.model.Merchant
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.repository.datasource.settings.ConnectionSettingsDataSource
import com.example.totanpay.data.repository.datasource.settings.LocalTaxDataSource
import com.example.totanpay.data.repository.datasource.settings.MacEnableLocalDataSource
import com.example.totanpay.data.repository.datasource.settings.MerchantLocalDataSource
import com.example.totanpay.data.repository.datasource.settlereverse.SettleReverseRemoteDataSource
import com.example.totanpay.data.repository.datasource.transaction.response.AccountMerchant
import com.example.totanpay.data.repository.datasource.transaction.toResponseTransaction
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val coroutineScope: CoroutineScope,
    private val settleReverseRemoteDataSource: SettleReverseRemoteDataSource,
    private val connectionSettingsDataSource: ConnectionSettingsDataSource,
    private val merchantLocalDataSource: MerchantLocalDataSource,
    private val deviceRepository: DeviceRepository,
    private val localTaxDataSource: LocalTaxDataSource,
    private val macEnableLocalDataSource: MacEnableLocalDataSource,
    private val context: Context
) : MainRepository {
    override suspend fun isConfigured(): Boolean {
        return withContext(ioDispatcher) {
            var flag = true
            val merchant = merchantLocalDataSource.getMerchant()
            if (merchant.merchantName.isNullOrEmpty())
                flag = false
            flag
        }
    }

    override suspend fun print(
        bitmap: Bitmap,
        context: Context,
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit
    ) {
        withContext(ioDispatcher) {
            deviceRepository.print(
                bitmap,
                context,
                onSuccess = {
                    onSuccess()
                },
                onFailed = { onFailed(it) }
            )
        }
    }

    override suspend fun settlementReverse() {
        coroutineScope.launch {
            settleReverseRemoteDataSource.settlementReverse(
                terminalLanguage = merchantLocalDataSource.getTerminalLanguage(),
                terminalConnectionType = connectionSettingsDataSource.getTerminalConnectionType(),
                terminalType = connectionSettingsDataSource.getTerminalType()
            )
        }.join()
    }

    override suspend fun changePrintStatusOfTransactionInQueue(
        dateOfTransaction: String,
        timeOfTransaction: String
    ) {
        coroutineScope.launch {
            val transactionInQueue =
                settleReverseRemoteDataSource.getTransactionInQueue(
                    dateOfTransaction,
                    timeOfTransaction
                )
            settleReverseRemoteDataSource.updatePrintStatusOfTransactionInQueue(transactionInQueue)
        }.join()
    }

    override suspend fun getMerchant(): Merchant? {
        return merchantLocalDataSource.getMerchant()
    }

    override suspend fun getTerminalId(): String? {
        return merchantLocalDataSource.getTerminalId()
    }


    override suspend fun getNotPrintLastTransactionInQueue(): ResponseTransaction? {
        return withContext(ioDispatcher) {
            val lastTransaction = settleReverseRemoteDataSource.getLastTxnIsNotPrinted()
            lastTransaction?.toResponseTransaction()
        }
    }

    override suspend fun getAllAccountMerchants(): List<AccountMerchant>? {
        return withContext(ioDispatcher) {
            merchantLocalDataSource.getAllAccountMerchants()
        }
    }

    @SuppressLint("NewApi")
    override fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    override fun setTaxIrancellCharge(tax: String) {
        localTaxDataSource.setTax(tax)
    }

    override fun getTaxIrancellCharge(): String {
        return localTaxDataSource.getTax()
    }

    override fun getIsEnableMac(): Boolean {
        return macEnableLocalDataSource.getEnableMac()
    }

    override fun enableMac(isEnable: Boolean) {
        macEnableLocalDataSource.enableMac(isEnable)
    }

    override  fun hasTransactionInQueue(): Flow<Boolean> {
      return  flow {
            while (true) {
                emit( settleReverseRemoteDataSource.hasTransactionInQueue())
                delay(3000)
            }
        }.flowOn(Dispatchers.IO)
    }

}


public fun Context.getAppVersion(): String {
    return try {
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        packageInfo.versionName ?: "Unknown"
    } catch (e: PackageManager.NameNotFoundException) {
        "Unknown"
    }
}

