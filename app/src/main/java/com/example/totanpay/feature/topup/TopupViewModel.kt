package com.example.totanpay.feature.topup



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.data.Operator
import com.example.totanpay.data.OperatorContainer
import com.example.totanpay.data.repository.MainRepository
import com.example.totanpay.data.repository.datasource.ResponseData
import com.example.totanpay.feature.purchase.PurchaseUiState
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TopupViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow(PurchaseUiState())
    val uiState: StateFlow<PurchaseUiState> = _uiState

    fun topup( track2: String, amount: String,mobile:String,operator: String, pinBlock: String) {
        val temp: Operator =Gson().fromJson(operator,Operator::class.java)

        viewModelScope.launch {
            val result = mainRepository.topup(amount=amount, mobile=mobile, productCode =temp.code.toString(), track2 =  track2,pinBlock= pinBlock)
            println("gggggggggghhhhhhhhh->${Gson().toJson(result)}")
            println("gggggggggghhhhumhhhhh->${Gson().toJson(result)}")
            if(result is ResponseData.Success){
                println("ssssssdddssssu")
                _uiState.update { it.copy(response = Gson().toJson(result.data), isSuccessful = true) }
            }
            else{
                println("ssssssdddsgsssu")
                _uiState.update { it.copy(response = Gson().toJson(result.data), isUnSuccessful = true) }
                println("ssssssdddsgsssuy->${uiState.value.isUnSuccessful}")
                println("ssssssdddsgsssuy->${uiState.value.response}")


            }
        }
    }
}
