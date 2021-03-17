package com.learn.codingChallenge.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.JsonParseException
import com.learn.codingChallenge.CoroutinesTestRule
import com.learn.codingChallenge.models.Transactions
import com.learn.codingChallenge.network.CodingServices
import com.learn.codingChallenge.utils.DataState
import com.learn.codingChallenge.utils.genericError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RemoteRepoTest {
    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var codingServices: CodingServices

    @Mock
    lateinit var mockTransactions: Transactions

    lateinit var remoteRepo: RemoteRepo

    @Before
    fun init() {
        remoteRepo = RemoteRepo(codingServices)
    }


    @Test
    fun `getTransactions emit successData`() {
        runBlockingTest {
            Mockito.`when`(codingServices.getTransactions()).thenReturn(mockTransactions)

            val allResults: List<DataState<Transactions>> = remoteRepo.getTransactions().toList()

            Assert.assertEquals(allResults[0], DataState.Success(mockTransactions))
        }
    }

    @Test
    fun `getTransactions emit failure when a exception is thrown`() {
        runBlockingTest {
            Mockito.`when`(codingServices.getTransactions())
                .thenThrow(JsonParseException("Malformed"))

            val allResults: List<DataState<Transactions>> = remoteRepo.getTransactions().toList()

            Assert.assertEquals(allResults[0], DataState.Failure(genericError))
        }
    }

}