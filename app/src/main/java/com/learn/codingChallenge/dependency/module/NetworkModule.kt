package com.learn.codingChallenge.dependency.module

import android.util.Log
import com.google.gson.Gson
import com.learn.codingChallenge.network.CodingServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.ihsanbal.logging.LoggingInterceptor
import javax.inject.Qualifier

const val BASE_URL = "https://storage.googleapis.com/nelo-assignment/api/"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class LoggingInterceptorOkHttp

    @Singleton
    @Provides
    fun providesGsonBuilder(): Gson = Gson()

    @LoggingInterceptorOkHttp
    @Provides
    fun provideHttpLogInterceptor(): Interceptor = LoggingInterceptor.Builder()
        .setLevel(com.ihsanbal.logging.Level.BASIC)
        .log(Log.VERBOSE)
        .build()

    @Singleton
    @Provides
    fun providesOkHttpClient(@LoggingInterceptorOkHttp loggingInterceptor: Interceptor): OkHttpClient {
       return OkHttpClient.Builder()
           .addInterceptor(loggingInterceptor)
           .build()
    }

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