package com.catnip.foodfood.di.hilt

import android.content.Context
import com.catnip.foodfood.api.service.ApiService
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        return ChuckerInterceptor(context)
    }

    @Singleton
    @Provides
    fun provideEGroceriesApiService(chuckerInterceptor: ChuckerInterceptor): ApiService {
        return ApiService.invoke(chuckerInterceptor)
    }
}
