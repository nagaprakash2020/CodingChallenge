package com.learn.codingChallenge.network

import com.learn.codingChallenge.models.Transactions
import retrofit2.http.GET

interface CodingServices {

    @GET("transactions.json")
    suspend fun getTransactions(): Transactions
}
