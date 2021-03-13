package com.learn.codingChallenge.repository

import com.learn.codingChallenge.models.Transactions
import com.learn.codingChallenge.network.CodingServices
import com.learn.codingChallenge.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

const val genericError = "Please Try Again Later"
class RemoteRepo @Inject constructor(private val codingServices: CodingServices){

    fun getTransactions(): Flow<DataState<Transactions>> = flow {
        try {
            val response = codingServices.getTransactions()
            emit(DataState.Success(response))
        }catch (e:Exception){
            e.printStackTrace()
            emit(DataState.Failure(genericError))
        }
    }

}
