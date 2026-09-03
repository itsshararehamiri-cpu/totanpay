package com.example.totanpay.data.repository.settings.device_settings

import java.security.PrivateKey

interface IccCardRepository {
    suspend fun detectCard(): Boolean
  suspend  fun verifySecondPinAndInjectKeys(pin: String, privateKey: PrivateKey?): Boolean
    suspend fun readPublicKey(): String?
   suspend fun readPrivateKey(): PrivateKey?
    suspend fun confirmFirstPin(pin: String): Boolean
}