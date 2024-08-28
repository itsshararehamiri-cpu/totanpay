package com.example.totanpay.feature.bill

import androidx.lifecycle.ViewModel
import com.example.totanpay.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jpos.iso.ISOUtil

@HiltViewModel
class BillViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {
    //    init {
//        viewModelScope.launch {
//            mainRepository.readCard(onSuccess = { println("track2=>$it") }){
//                println("error->$it")
//            }
//        }
//    }
    init {
        println("init GetPingggViewModel")
    }
    fun billInquery(billId:String,payId:String) {
        viewModelScope.launch {
         mainRepository.billPay("12345666","888888")
        }
    }
}