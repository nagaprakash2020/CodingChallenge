package com.learn.codingChallenge.transactions

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.learn.codingChallenge.CoroutinesTestRule
import com.learn.codingChallenge.models.Transaction
import com.learn.codingChallenge.models.Transactions
import com.learn.codingChallenge.repository.CodingRepo
import com.learn.codingChallenge.transactions.TransactionsViewModel.TransactionStates
import com.learn.codingChallenge.utils.DataState
import com.learn.codingChallenge.utils.genericError
import com.learn.codingChallenge.utils.noTransactions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TransactionsViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var transaction1: Transaction

    @Mock
    lateinit var transaction2: Transaction

    @Mock
    lateinit var transaction3: Transaction

    @Mock
    lateinit var transaction4: Transaction

    @Mock
    lateinit var mockRepo: CodingRepo

    lateinit var viewModel: TransactionsViewModel

    @Test
    fun `transactionStates is set to MessageState with genericError message when repo returns a failure`() {
        `when`(mockRepo.getTransactions()).thenReturn(flow {
            emit(DataState.Failure(genericError))
        })

        viewModel = TransactionsViewModel(mockRepo)

        assertEquals(viewModel.transactionStates.value, TransactionStates.Message(genericError))
    }

    @Test
    fun `transactionStates is set to MessageState with NoTransactions message when repo returns a empty list`() {
        `when`(mockRepo.getTransactions()).thenReturn(flow {
            emit(DataState.Success(Transactions(arrayListOf())))
        })

        viewModel = TransactionsViewModel(mockRepo)

        assertEquals(viewModel.transactionStates.value, TransactionStates.Message(noTransactions))
    }

    @Test
    fun `transactionStates is set to DisplayList with transactions list when repo returns a list`() {
        val mockTransactionList = arrayListOf(transaction1, transaction2)
        `when`(mockRepo.getTransactions()).thenReturn(flow {
            emit(DataState.Success(Transactions(mockTransactionList)))
        })

        viewModel = TransactionsViewModel(mockRepo)

        assertEquals(
            viewModel.transactionStates.value,
            TransactionStates.DisplayList(mockTransactionList)
        )
    }

    @Test
    fun `createMapWithOccurrence creates map `() {

        createViewModel()
        `when`(transaction1.merchantName).thenReturn("1")
        `when`(transaction2.merchantName).thenReturn("1")
        `when`(transaction3.merchantName).thenReturn("2")
        `when`(transaction4.merchantName).thenReturn("2")
        val transactionList =
            arrayListOf(transaction1, transaction2, transaction3, transaction4)

        val result = viewModel.createMapWithOccurrence(transactionList)
        // result = { "1null" -> 2, "2null" -> 2}
        assertEquals(2, result.size)
    }

    private fun createViewModel() {
        // This is only needed to get rid of exception from flow
        `when`(mockRepo.getTransactions()).thenReturn(flow {
            emit(DataState.Failure(genericError))
        })

        viewModel = TransactionsViewModel(mockRepo)
    }

    @Test
    fun `getRecurringKey returns key`() {
        val merchantName = "Netflix"
        val amount = 330L
        val expected = merchantName + amount
        createViewModel()
        `when`(transaction1.merchantName).thenReturn(merchantName)
        `when`(transaction1.amountCents).thenReturn(amount)

        assertEquals(viewModel.getRecurringKey(transaction1), expected)
    }

    @Test
    fun `getUpdatedTransactions updates transactions`() {
        val merchantName = "Netflix"
        val amount = 330L

        val mockTransaction1 = Transaction(
            uuid = "",
            merchantUuid = "",
            merchantName = merchantName,
            currencyCode = "",
            amountCents = amount,
            timestamp = 2L,
            isRecurring = false
        )

        val mockTransaction2 = Transaction(
            uuid = "",
            merchantUuid = "",
            merchantName = merchantName,
            currencyCode = "",
            amountCents = amount,
            timestamp = 2L,
            isRecurring = false
        )
        val mockTransaction3 = Transaction(
            uuid = "",
            merchantUuid = "",
            merchantName = "",
            currencyCode = "",
            amountCents = 1L,
            timestamp = 2L,
            isRecurring = false
        )
        createViewModel()
        val updatedTransactions = viewModel.getUpdatedTransactions(
            arrayListOf(
                mockTransaction1,
                mockTransaction2,
                mockTransaction3
            )
        )
        val recurringTransactions = updatedTransactions.filter { it.isRecurring }

        assertEquals(recurringTransactions.size, 2)
    }
}