package com.example.totanpay.feature.settings.supervisor

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.R
import com.example.totanpay.data.repository.log.LogRepository
import com.example.totanpay.data.repository.log.LogType
import com.example.totanpay.data.repository.settings.device_settings.IccCardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.security.PrivateKey
import javax.inject.Inject

@HiltViewModel
class KeyInjectionViewModel @Inject constructor(private val iccCardRepository: IccCardRepository,private val logRepository: LogRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(KeyInjectionUiState())
    val uiState: StateFlow<KeyInjectionUiState> = _uiState
val TAG="ssss"
    fun verifyFirstPin(pin: String,context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(showEnterFirstCard = true, showEnterFirstPin = false) }
            val cardIsDetected = iccCardRepository.detectCard()
            if (cardIsDetected) {
                val verifyFirstPinResult = iccCardRepository.confirmFirstPin(pin)
                if (verifyFirstPinResult) {
                    val readPublicKeyResult = iccCardRepository.readPublicKey()
                    if (readPublicKeyResult != null) {
                        val privateKey = iccCardRepository.readPrivateKey()
                        if (privateKey == null) {
                            _uiState.update { it.copy(errorInKeyInjection=context.getString(R.string.error_in_reading_icc)) }
                            delay(3000)
                            _uiState.update { it.copy(errorInKeyInjection = "", isFinished = true) }
                        } else {
                            _uiState.update {
                                it.copy(
                                    showEnterFirstCard = false,
                                    showRemoveFirstCard = true,
                                    cardIsDetected = false,
                                    privateKey = privateKey
                                )
                            }
                            delay(3000)
                            _uiState.update {
                                it.copy(
                                    showEnterSecondPin = true,
                                    showRemoveFirstCard = false,
                                    showEnterFirstCard = false
                                )
                            }
                        }
                    } else {
                        _uiState.update { it.copy(errorInKeyInjection=context.getString(R.string.error_in_reading_key_is_failed)) }
                        delay(3000)
                        _uiState.update { it.copy(errorInKeyInjection = "", isFinished = true) }
                    }
                } else {
                    logRepository.addLog(LogType.KEY_CARD_INCORRECT)
                    _uiState.update { it.copy(errorInKeyInjection = context.getString(R.string.first_pin_is_incorrect)) }
                    delay(3000)
                    _uiState.update { it.copy(errorInKeyInjection = "", isFinished = true) }
                }
            } else {
                _uiState.update { it.copy(isFinished = true) }
            }

        }
    }

    fun verifySecondPin(pin: String,context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(showEnterSecondCard = true, showEnterSecondPin = false) }
            val cardIsDetected = iccCardRepository.detectCard()
            if (cardIsDetected) {
                val verifyPinResult =
                    iccCardRepository.verifySecondPinAndInjectKeys(pin, uiState.value.privateKey)
                if (!verifyPinResult) {
                    logRepository.addLog(LogType.KEY_CARD_INCORRECT)
                    _uiState.update { it.copy(errorInKeyInjection = context.getString(R.string.second_pin_is_incorrect)) }
                    delay(3000)
                    _uiState.update { it.copy(errorInKeyInjection = "", isFinished = true) }
                } else {
                    _uiState.update {
                        it.copy(
                            showEnterSecondCard = false,
                            cardIsDetected = false,
                            showEnterSecondPin = false,
                            showKeyInjectionIsSucceed = true
                        )
                    }
                    delay(3000)
                    _uiState.update {
                        it.copy(
                            showKeyInjectionIsSucceed = false,
                            isFinished = true
                        )
                    }
                }
                //  }
            } else {
                _uiState.update { it.copy(isFinished = true) }
            }
        }
    }
}

data class KeyInjectionUiState(
    val showEnterFirstPin: Boolean = true,
    val showEnterSecondPin: Boolean = false,
    val showEnterFirstCard: Boolean = false,
    val showEnterSecondCard: Boolean = false,
    val showConfigurationLoading: Boolean = false,
    val cardIsDetected: Boolean = false,
    var showKeyInjectionIsSucceed: Boolean = false,
    var privateKey: PrivateKey? = null,
    var isFinished: Boolean = false,
    val showRemoveFirstCard: Boolean = false,
    val showRemoveSecondCard: Boolean = false,
    val errorInKeyInjection: String = ""
)
