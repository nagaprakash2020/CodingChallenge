package com.learn.codingChallenge.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learn.codingChallenge.models.Transaction
import com.learn.codingChallenge.repository.CodingRepo
import com.learn.codingChallenge.utils.DataState
import com.learn.codingChallenge.utils.noTransactions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(private val repo: CodingRepo) : ViewModel() {

    sealed class TransactionStates {
        object Loading : TransactionStates()
        data class Message(val message: String) : TransactionStates()
        data class DisplayList(val transactions: ArrayList<Transaction>) : TransactionStates()
    }

    private var _transactionStates = MutableLiveData<TransactionStates>()
    val transactionStates: LiveData<TransactionStates> = _transactionStates

    init {
        getTransactions()
    }


    private fun getTransactions() {
        viewModelScope.launch {
            _transactionStates.value = TransactionStates.Loading
            repo.getTransactions().collect {
                when (it) {
                    is DataState.Success -> {
                        if (it.data.transactions.isNullOrEmpty()) _transactionStates.value =
                            TransactionStates.Message(noTransactions)
                        else _transactionStates.value =
                            TransactionStates.DisplayList(it.data.transactions)

                    }
                    is DataState.Failure -> _transactionStates.value =
                        TransactionStates.Message(it.errorMessage)
                }
            }
        }

    }
}