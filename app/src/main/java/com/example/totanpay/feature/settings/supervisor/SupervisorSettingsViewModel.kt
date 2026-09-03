package com.example.totanpay.feature.settings.supervisor

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.R
import com.example.totanpay.data.repository.DeviceRepository
import com.example.totanpay.data.repository.MainRepository
import com.example.totanpay.data.repository.configiuration.ConfigurationRepository
import com.example.totanpay.data.repository.datasource.ResponseData
import com.example.totanpay.data.repository.device.KCV
import com.example.totanpay.data.util.getPersianDate
import com.example.totanpay.data.util.timeToForm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.security.PrivateKey
import javax.inject.Inject

@HiltViewModel
class SupervisorSettingsViewModel @Inject constructor(
    private val configurationRepository: ConfigurationRepository,
    private val deviceRepository: DeviceRepository,
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SupervisorSettingsUiState())
    val uiState: StateFlow<SupervisorSettingsUiState> = _uiState


    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    taxForIrancellCharge = mainRepository.getTaxIrancellCharge(),
                    macIsEnabled = mainRepository.getIsEnableMac()
                )
            }
        }
    }

    fun configuration() {
        viewModelScope.launch {
            val keyInjected = deviceRepository.isInjectMaster()
            if (keyInjected) {
                _uiState.update { it.copy(showConfigurationLoading = true,
                    showConfigurationIsSucceed = false, configurationResult = null) }
                delay(1000)
                val configurationResult = configurationRepository.configuration()
                if (configurationResult is ResponseData.Success) {
                    _uiState.update {
                        it.copy(
                            showConfigurationIsSucceed = true, showConfigurationLoading = false,
                            configurationResult = ConfigurationResult(
                                terminalId = mainRepository.getTerminalId() ?: "",
                                merchantName = mainRepository.getMerchant()?.merchantName ?: "",
                                englishMerchantName = mainRepository.getMerchant()?.englishMerchantName
                                    ?: "",
                                merchantId = mainRepository.getMerchant()?.merchantId ?: "",
                                merchantPhone = mainRepository.getMerchant()?.merchantPhone ?: "",
                                date = getPersianDate(configurationResult.data!!.date),
                                time = configurationResult.data.time.timeToForm(),
                                posCode = configurationResult.data.posCode ?: ""
                            )
                        )
                    }
                    delay(2000)
                    _uiState.update {
                        it.copy(
                            showConfigurationIsSucceed = false,
                            showConfigurationLoading = false
                        )
                    }
                } else {
                    if (configurationResult.error != null) {
                        _uiState.update {
                            it.copy(
                                error = configurationResult.error.messageError
                                    ?: R.string.empty_message,
                                showConfigurationLoading = false
                            )
                        }
                        delay(2000)
                        _uiState.update {
                            it.copy(
                                error = R.string.empty_message,
                                showConfigurationLoading = false
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                error = configurationResult.data?.responseMessage
                                    ?: R.string.empty_message,
                                showConfigurationLoading = false
                            )
                        }
                        delay(2000)
                        _uiState.update {
                            it.copy(
                                error = R.string.empty_message,
                                showConfigurationLoading = false
                            )
                        }
                    }

                }
            } else {
                _uiState.update {
                    it.copy(
                        showKeyIsNotInjectedError = true
                    )
                }
            }
        }
    }


    fun getTerminalInfo() {
        viewModelScope.launch {
            val keyInjected = deviceRepository.isInjectMaster()
            if (keyInjected) {
                _uiState.update { it.copy(showGetTerminalInfoLoading = true,showGetTerminalInfoIsSucceed=false,
                    configurationResult=null) }
                delay(1000)
                val configurationResult = configurationRepository.init()
                if (configurationResult is ResponseData.Success) {
                    _uiState.update {
                        it.copy(
                            showGetTerminalInfoIsSucceed = true, showGetTerminalInfoLoading = false,
                            configurationResult = ConfigurationResult(
                                terminalId = mainRepository.getTerminalId() ?: "",
                                merchantName = mainRepository.getMerchant()?.merchantName ?: "",
                                englishMerchantName = mainRepository.getMerchant()?.englishMerchantName
                                    ?: "",
                                merchantId = mainRepository.getMerchant()?.merchantId ?: "",
                                merchantPhone = mainRepository.getMerchant()?.merchantPhone ?: "",
                                date = getPersianDate(configurationResult.data!!.date),
                                time = configurationResult.data.time.timeToForm(),
                                posCode = configurationResult.data.posCode ?: ""
                            )
                        )
                    }
                    delay(2000)
                    _uiState.update {
                        it.copy(
                            showGetTerminalInfoIsSucceed = false,
                            showGetTerminalInfoLoading = false
                        )
                    }
                } else {
                    if (configurationResult.error != null) {
                        _uiState.update {
                            it.copy(
                                error = configurationResult.error.messageError
                                    ?: R.string.empty_message,
                                showGetTerminalInfoLoading = false
                            )
                        }
                        delay(2000)
                        _uiState.update {
                            it.copy(
                                error = R.string.empty_message,
                                showGetTerminalInfoLoading = false
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                error = configurationResult.data?.responseMessage
                                    ?: R.string.empty_message,
                                showGetTerminalInfoLoading = false
                            )
                        }
                        delay(2000)
                        _uiState.update {
                            it.copy(
                                error = R.string.empty_message,
                                showGetTerminalInfoLoading = false
                            )
                        }
                    }

                }
            } else {
                _uiState.update {
                    it.copy(
                        showKeyIsNotInjectedError = true
                    )
                }
            }
        }
    }


    fun print(bitmap: Bitmap, context: Context, onSuccess: () -> Unit, onFailed: (String) -> Unit) {
        viewModelScope.launch {
            deviceRepository.print(
                bitmap,
                context,
                onSuccess = { onSuccess() },
                onFailed = { onFailed(it) })
        }
    }

    fun setTaxForIrancellCharge(tax: String) {
        viewModelScope.launch {
            mainRepository.setTaxIrancellCharge(tax = tax)
            _uiState.update { it.copy(taxForIrancellCharge = tax) }
        }
    }

    fun hideKeyIsNotInjectedError() {
        viewModelScope.launch {
            _uiState.update { it.copy(showKeyIsNotInjectedError = false) }
        }
    }

    fun enableMac(isEnable: Boolean) {
        viewModelScope.launch {
            mainRepository.enableMac(isEnable)
            _uiState.update {
                it.copy(macIsEnabled = isEnable)
            }
        }

    }

    fun getKcv() {
        viewModelScope.launch {
            _uiState.update { it.copy(kcv = null) }
            val kcv = deviceRepository.getKcv()
            _uiState.update { it.copy(kcv = kcv) }
        }
    }
}

data class SupervisorSettingsUiState(
    val showConfigurationIsSucceed: Boolean = false,
    val showGetTerminalInfoIsSucceed: Boolean = false,

    val error: Int = R.string.empty_message,
    val showKeyInjection: Boolean = false,
    val showConfigurationLoading: Boolean = false,
    val showGetTerminalInfoLoading: Boolean = false,

    val cardIsDetected: Boolean = false,
    val showEnterSecondPin: Boolean = false,
    var showKeyInjectionIsSucceed: Boolean = false,
    var privateKey: PrivateKey? = null,
    val configurationResult: ConfigurationResult? = null,
    val showKeyIsNotInjectedError: Boolean = false,
    val taxForIrancellCharge: String = "",
    val macIsEnabled: Boolean = true,
    val kcv: KCV? = null
)

data class ConfigurationResult(
    val terminalId: String = "",
    val merchantId: String = "",
    val merchantPhone: String = "",
    val merchantName: String = "",
    val englishMerchantName: String = "",
    val date: String = "",
    val time: String = "",
    val posCode: String = ""
)