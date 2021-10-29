package com.example.matatumanageradmin.di

import com.example.matatumanageradmin.data.FirebaseRepository
import com.example.matatumanageradmin.data.KtorRepository
import com.example.matatumanageradmin.data.MainRepository
import com.example.matatumanageradmin.data.MatManagerApi
import com.example.util.BASE_URL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirestore() = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideMainRepository(firestore: FirebaseFirestore, auth: FirebaseAuth, uId: String?): MainRepository = FirebaseRepository(firestore, auth, uId)

    @Singleton
    @Provides
    fun provideMatManagerApi(): MatManagerApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MatManagerApi::class.java)



    @Singleton
    @Provides
    fun provideMainRepository(api: MatManagerApi, auth: FirebaseAuth): MainRepository = KtorRepository(auth, api)

}