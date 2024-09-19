package com.example.totanpay.data.repository

interface SupervisorSettingsRepository {
  suspend  fun supervisorPasswordIsValid(pass: String):Boolean
}