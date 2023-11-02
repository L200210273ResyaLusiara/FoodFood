package com.catnip.foodfood.di.hilt

import com.catnip.foodfood.api.datasource.ApiDataSource
import com.catnip.foodfood.data.FirebaseAuthDataSource
import com.catnip.foodfood.local.database.datasource.CartDataSource
import com.catnip.foodfood.repository.CartRepository
import com.catnip.foodfood.repository.CartRepositoryImpl
import com.catnip.foodfood.repository.FoodRepository
import com.catnip.foodfood.repository.FoodRepositoryImpl
import com.catnip.foodfood.repository.UserRepository
import com.catnip.foodfood.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideCartRepository(
        cartDataSource: CartDataSource,
        foodDataSource: ApiDataSource
    ): CartRepository {
        return CartRepositoryImpl(cartDataSource, foodDataSource)
    }

    @Singleton
    @Provides
    fun provideFoodRepository(
        foodDataSource: ApiDataSource
    ): FoodRepository {
        return FoodRepositoryImpl(foodDataSource)
    }

    @Singleton
    @Provides
    fun provideUserRepository(
        userDataSource: FirebaseAuthDataSource
    ): UserRepository {
        return UserRepositoryImpl(userDataSource)
    }
}
