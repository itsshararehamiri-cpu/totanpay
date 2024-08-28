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
class ReadCardViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel (){
    private val _uiState = MutableStateFlow(ReadCardUiState())
    val uiState: StateFlow<ReadCardUiState> = _uiState
    init {
        viewModelScope.launch {
            mainRepository.readCard(onSuccess = {track2-> println("track2=>$track2")
                _uiState.update { it.copy(track2 = track2) }
            }){error->
                _uiState.update { it.copy(error = error) }
            }
        }
    }
}
data class ReadCardUiState(
    val track2: String="",
    val error:String=""
)
