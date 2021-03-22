package com.learn.codingChallenge.transactions

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.learn.codingChallenge.databinding.ActivityMainBinding
import com.learn.codingChallenge.models.Transaction
import com.learn.codingChallenge.transactions.TransactionsViewModel.TransactionStates.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: TransactionsViewModel by viewModels()
    private lateinit var adapter:TransactionsAdapter

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createTransactionsListView()
        binding.search.doOnTextChanged { text, _, _, _ ->
            viewModel.filterTransactions(text.toString())
        }
        lifecycleScope.launchWhenStarted {
            observeData()
        }

    }

    private fun createTransactionsListView() {
        adapter = TransactionsAdapter()
        binding.transactionsRV.layoutManager = LinearLayoutManager(this@TransactionsActivity, LinearLayoutManager.VERTICAL, false)
        binding.transactionsRV.adapter = adapter
    }

    private fun observeData() {
        viewModel.transactionStates.observe(this, Observer {
            when(it){
                is Loading -> showLoadingState()
                is DisplayList -> showListState(it.transactions)
                is Message -> showMessageState(it.message)
            }
        })
        viewModel.searchedTransactions.observe(this, Observer {
            adapter.transactionList = it
        })
        viewModel.noSearchResults.observe(this, Observer {
            if(it){
                binding.messageTV.visibility = View.VISIBLE
                binding.messageTV.text = "No transactions found"
            }else {
                binding.messageTV.visibility = View.INVISIBLE
            }
        })
    }

    private fun showListState(transactions: ArrayList<Transaction>) {
        binding.loadingPI.visibility = View.GONE
        binding.messageTV.visibility = View.GONE
        adapter.transactionList = transactions
    }

    private fun showMessageState(message: String) {
        binding.loadingPI.visibility = View.GONE
        binding.transactionsRV.visibility = View.GONE
        binding.messageTV.visibility = View.VISIBLE
        binding.messageTV.text = message
    }

    private fun showLoadingState() {
        binding.loadingPI.visibility = View.VISIBLE
    }
}