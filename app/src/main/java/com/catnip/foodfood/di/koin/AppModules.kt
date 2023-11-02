package com.catnip.foodfood.di.koin

import com.catnip.foodfood.api.datasource.ApiDataSource
import com.catnip.foodfood.api.datasource.FoodApiDataSource
import com.catnip.foodfood.api.service.ApiService
import com.catnip.foodfood.data.FirebaseAuthDataSource
import com.catnip.foodfood.data.FirebaseAuthDataSourceImpl
import com.catnip.foodfood.local.database.AppDatabase
import com.catnip.foodfood.local.database.datasource.CartDataSource
import com.catnip.foodfood.local.database.datasource.CartDatabaseDataSource
import com.catnip.foodfood.repository.CartRepository
import com.catnip.foodfood.repository.CartRepositoryImpl
import com.catnip.foodfood.repository.FoodRepository
import com.catnip.foodfood.repository.FoodRepositoryImpl
import com.catnip.foodfood.repository.UserRepository
import com.catnip.foodfood.repository.UserRepositoryImpl
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module
import com.catnip.foodfood.presentation.fragmentcart.CartViewModel
import com.catnip.foodfood.presentation.checkout.CheckoutViewModel
import com.catnip.foodfood.presentation.detail.DetailViewModel
import com.catnip.foodfood.presentation.editprofile.EditProfileViewModel
import com.catnip.foodfood.presentation.fragmenthome.HomeViewModel
import com.catnip.foodfood.presentation.fragmentprofile.ProfileViewModel
import com.catnip.foodfood.presentation.login.LoginViewModel
import com.catnip.foodfood.presentation.register.RegisterViewModel

object AppModules {

    private val localModule = module {
        single { AppDatabase.getInstance(androidContext()) }
        single { get<AppDatabase>().cartDao() }
    }

    private val networkModule = module {
        single { ChuckerInterceptor(androidContext()) }
        single { ApiService.invoke(get()) }
    }

    private val firebaseModule = module {
        single { FirebaseAuth.getInstance() }
    }

    private val dataSourceModule = module {
        single<CartDataSource> { CartDatabaseDataSource(get()) }
        single<FirebaseAuthDataSource> { FirebaseAuthDataSourceImpl(get()) }
        single<ApiDataSource> { FoodApiDataSource(get()) }
    }

    private val repositoryModule = module {
        single<CartRepository> { CartRepositoryImpl(get(), get()) }
        single<FoodRepository> { FoodRepositoryImpl(get()) }
        single<UserRepository> { UserRepositoryImpl(get()) }
    }

    private val viewModelModule = module {
        viewModelOf(::CheckoutViewModel)
        viewModelOf(::DetailViewModel)
        viewModelOf(::EditProfileViewModel)
        viewModelOf(::CartViewModel)
        viewModelOf(::HomeViewModel)
        viewModelOf(::ProfileViewModel)
        viewModelOf(::LoginViewModel)
        viewModelOf(::RegisterViewModel)
    }

    val modules: List<Module> = listOf(
        localModule,
        networkModule,
        firebaseModule,
        dataSourceModule,
        repositoryModule,
        viewModelModule
    )
}
