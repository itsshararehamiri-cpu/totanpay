package com.example.totanpay.data.repository.settings.supervisor

interface SupervisorSettingsRepository {
  suspend  fun supervisorPasswordIsValid(pass: String):Boolean
}