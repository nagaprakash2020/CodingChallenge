package com.learn.codingChallenge.models

data class Transaction(val uuid:String, val merchantUuid:String, val merchantName:String,
                       val currencyCode:String,val amountCents:Long, val timestamp:Long,
                        var isRecurring:Boolean = false)

data class Transactions(val transactions: ArrayList<Transaction>)