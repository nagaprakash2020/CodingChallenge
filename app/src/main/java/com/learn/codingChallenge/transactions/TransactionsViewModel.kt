package com.learn.codingChallenge.transactions

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.VisibleForTesting
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

@RequiresApi(Build.VERSION_CODES.N)
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


    @VisibleForTesting
    fun getTransactions() {
        viewModelScope.launch {
            _transactionStates.value = TransactionStates.Loading
            repo.getTransactions().collect {
                when (it) {
                    is DataState.Success -> {
                        if (it.data.transactions.isNullOrEmpty()) _transactionStates.value =
                            TransactionStates.Message(noTransactions)
                        else _transactionStates.value =
                            TransactionStates.DisplayList(getUpdatedTransactions(it.data.transactions))

                    }
                    is DataState.Failure -> _transactionStates.value =
                        TransactionStates.Message(it.errorMessage)
                }
            }
        }

    }


    @VisibleForTesting
    fun getUpdatedTransactions(transactions: ArrayList<Transaction>): ArrayList<Transaction> {
        val map = createMapWithOccurrence(transactions)
        transactions.forEach {
            it.isRecurring = map.getOrDefault(getRecurringKey(it), 0) > 1
        }
        return transactions
    }

    @VisibleForTesting
    fun createMapWithOccurrence(transactions: ArrayList<Transaction>): Map<String, Int> {
        // Put objects into HashMap with count
        val map = mutableMapOf<String, Int>()
        transactions.forEach {
            map[getRecurringKey(it)] = map.getOrDefault(getRecurringKey(it), 0) + 1
        }

        return map
    }

    @VisibleForTesting
    fun getRecurringKey(transaction: Transaction): String =
        transaction.merchantName + transaction.amountCents
}