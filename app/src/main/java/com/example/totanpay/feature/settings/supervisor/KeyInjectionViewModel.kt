package com.example.totanpay.feature.settings.supervisor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.data.repository.IccCardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.security.PrivateKey
import javax.inject.Inject

@HiltViewModel
class KeyInjectionViewModel @Inject constructor(private val iccCardRepository: IccCardRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(KeyInjectionUiState())
    val uiState: StateFlow<KeyInjectionUiState> = _uiState
val TAG="ssss"
    fun verifyFirstPin(pin: String) {
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
                            _uiState.update { it.copy(errorInKeyInjection="خطا در خواندن کلید از کارت") }
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
                        _uiState.update { it.copy(errorInKeyInjection="خطا در خواندن کلید از کارت") }
                        delay(3000)
                        _uiState.update { it.copy(errorInKeyInjection = "", isFinished = true) }
                    }
                } else {
                    _uiState.update { it.copy(errorInKeyInjection = "رمز اول اشتباه است.") }
                    delay(3000)
                    _uiState.update { it.copy(errorInKeyInjection = "", isFinished = true) }
                }
            } else {
                _uiState.update { it.copy(isFinished = true) }
            }

        }
    }

    fun verifySecondPin(pin: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(showEnterSecondCard = true, showEnterSecondPin = false) }
            val cardIsDetected = iccCardRepository.detectCard()
            if (cardIsDetected) {
                val verifyPinResult =
                    iccCardRepository.verifySecondPinAndInjectKeys(pin, uiState.value.privateKey)
                if (!verifyPinResult) {
                    _uiState.update { it.copy(errorInKeyInjection = "رمز دوم اشتباه است.") }
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
