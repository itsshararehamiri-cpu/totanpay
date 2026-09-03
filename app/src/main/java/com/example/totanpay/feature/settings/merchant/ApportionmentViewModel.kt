package com.example.totanpay.feature.settings.merchant


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.data.repository.settings.merchant.MerchantSettingsRepository
import com.example.totanpay.data.repository.datasource.transaction.request.Apportionment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ApportionmentViewModel @Inject constructor(
    private val repository: MerchantSettingsRepository
) :
    ViewModel() {
    private val _uiState = MutableStateFlow(ApportionmentUiState())
    val uiState: StateFlow<ApportionmentUiState> = _uiState


    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    apportionments = repository.getApportionments().toMutableList()
                )
            }
        }
    }

    fun confirmApportionment() {
        viewModelScope.launch {
            var temp: Int = 0
            uiState.value.apportionments.forEach {
                if (it.amount.isNotEmpty())
                    temp += it.amount.toInt()
            }
            if (temp == 100) {
                repository.storeApportionments(uiState.value.apportionments)
                _uiState.update { it.copy(isFinish = true) }
            } else {
                _uiState.update { it.copy(showErrorInPercent = true) }
                delay(3000)
                _uiState.update { it.copy(showErrorInPercent = false) }
            }
        }
    }

    fun addApportionment(newApportionment: Apportionment, amount: String) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    apportionments = currentState.apportionments.toMutableList().apply {
                        val index = indexOfFirst { it.IBAN == newApportionment.IBAN }
                        if (index != -1) {
                            val temp = newApportionment.copy(amount = amount)
                            set(index, temp)
                        } else {
                            add(newApportionment)
                        }
                    }
                )
            }
        }
    }
}

data class ApportionmentUiState(
    val apportionments: MutableList<Apportionment> = mutableListOf(),
    val isFinish: Boolean = false,
    val showErrorInPercent: Boolean = false
)

