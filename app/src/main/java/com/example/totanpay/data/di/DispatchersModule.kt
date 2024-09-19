package com.example.totanpay.data.di
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.SupervisorJob
//import javax.inject.Singleton
//import javax.inject.Qualifier
//
//@Qualifier
//@Retention(AnnotationRetention.BINARY)
//annotation class ExternalScope
//
//@Qualifier
//@Retention(AnnotationRetention.BINARY)
//annotation class IoDispatcherScope
//
//@Module
//@InstallIn(SingletonComponent::class)
//object DispatchersModule {
//
//    @Provides
//    @Singleton
//    @ExternalScope
//    fun providesExternalScope(): CoroutineScope {
//        return CoroutineScope(SupervisorJob() + Dispatchers.Main)
//    }
//
//    @Provides
//    @Singleton
//    @IoDispatcherScope
//    fun providesIoDispatcher(): CoroutineScope {
//        return CoroutineScope( Dispatchers.IO)
//    }
//}
//

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {
    @Provides
    @Singleton
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
    @Provides
    @Singleton
    fun providesCoroutineScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
}
