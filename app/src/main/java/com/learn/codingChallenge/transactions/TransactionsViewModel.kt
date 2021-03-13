package com.learn.codingChallenge.transactions

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learn.codingChallenge.repository.CodingRepo
import com.learn.codingChallenge.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(val repo: CodingRepo) : ViewModel() {

    fun getTransactions() {
        viewModelScope.launch {
            repo.getTransactions().collect {
                when (it) {
                    is DataState.Success -> {
                        Log.d("Prakash", it.data.toString())
                    }
                    is DataState.Failure -> {
                        Log.d("Prakash", it.errorMessage)
                    }
                }

            }
        }

    }
}