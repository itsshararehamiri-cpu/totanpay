package com.example.totanpay.domain

import com.example.totanpay.data.repository.settings.CurrentLanguageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class GetCurrentLanguageUseCase @Inject constructor(
    private val currentLanguageRepository: CurrentLanguageRepository
) {
    operator fun invoke(): Flow<Boolean> = currentLanguageRepository.getLanguage()
        .map {
         it?.isFarsi?:true
        }
}