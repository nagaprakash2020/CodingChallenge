package com.learn.codingChallenge.repository

import com.learn.codingChallenge.models.Transactions
import com.learn.codingChallenge.utils.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CodingRepo @Inject constructor(private val remoteRepo:RemoteRepo){
    fun getTransactions(): Flow<DataState<Transactions>> = remoteRepo.getTransactions()
}