package com.learn.codingChallenge.models

data class Transaction(val uuid:String, val merchantUuid:String, val merchantName:String,
                       val currencyCode:String,val amountCents:Long, val timestamp:Long,
                        val isRecurring:Boolean = false)

data class Transactions(val transactions: ArrayList<Transaction>)