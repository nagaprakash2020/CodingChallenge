package com.learn.codingChallenge.dependency.module

import com.google.gson.Gson
import com.learn.codingChallenge.network.CodingServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

const val BASE_URL = "storage.googleapis.com/nelo-assignment/api/"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun providesGsonBuilder(): Gson = Gson()

    @Singleton
    @Provides
    fun providesOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Singleton
    @Provides
    fun providesRetrofitBuilder(okHttpClient: OkHttpClient, gson: Gson): Retrofit.Builder {

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
    }

    @Singleton
    @Provides
    fun provideCodingServices(retrofitBuilder: Retrofit.Builder): CodingServices {
        return retrofitBuilder.build().create(CodingServices::class.java)
    }
}