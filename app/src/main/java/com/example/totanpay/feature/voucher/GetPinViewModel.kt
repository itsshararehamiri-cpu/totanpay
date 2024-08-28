package com.example.totanpay.feature.voucher


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetPinViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow(GetPinUiState())
    val uiState: StateFlow<GetPinUiState> = _uiState

    init {
        println("init GetPinViewModel")
    }

    fun getPin(track2: String) {
        viewModelScope.launch {
            val string: List<String> = track2.split("=")
            val pan = string[0]
            mainRepository.getPinBlock(
                pan,
                onError = { println("onError->$it") },
                onInput = { println("onInput->$it") },
                onConfirm = {pinBlock->
                    println("ssssssssssss->${track2}")
                    println("ssssssssssss->$pan")

                    viewModelScope.launch {
                        //mainRepository.purchase("1000",pan, track2, it)
                        _uiState.update { it.copy(getPin = true,pinBlock=pinBlock) }
                    }
                },
                onCanecl = { println("onCanecl->") },
                onTimeOut = { println("onTimeOut->") })
        }
    }
}

data class GetPinUiState(
    val getPin: Boolean = false,val pinBlock:String=""
)