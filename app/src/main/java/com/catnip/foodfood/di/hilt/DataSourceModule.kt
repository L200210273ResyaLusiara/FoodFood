package com.catnip.foodfood.di.hilt

import com.catnip.foodfood.api.datasource.ApiDataSource
import com.catnip.foodfood.api.datasource.FoodApiDataSource
import com.catnip.foodfood.api.service.ApiService
import com.catnip.foodfood.data.FirebaseAuthDataSource
import com.catnip.foodfood.data.FirebaseAuthDataSourceImpl
import com.catnip.foodfood.local.database.dao.CartDao
import com.catnip.foodfood.local.database.datasource.CartDataSource
import com.catnip.foodfood.local.database.datasource.CartDatabaseDataSource
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Singleton
    @Provides
    fun provideCartDataSource(cartDao: CartDao): CartDataSource {
        return CartDatabaseDataSource(cartDao)
    }

    @Singleton
    @Provides
    fun provideFoodApiDataSource(service: ApiService): ApiDataSource {
        return FoodApiDataSource(service)
    }

    @Singleton
    @Provides
    fun provideFirebaseAuthDataSource(firebaseAuth: FirebaseAuth): FirebaseAuthDataSource {
        return FirebaseAuthDataSourceImpl(firebaseAuth)
    }
}
