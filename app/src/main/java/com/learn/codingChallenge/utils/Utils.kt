package com.learn.codingChallenge.utils

import android.text.format.DateFormat
import java.text.NumberFormat
import java.util.*


const val genericError = "Please Try Again Later"
const val noTransactions = "No Transactions Found"


sealed class DataState<out T> {
    data class Success<out T>(val data: T) : DataState<T>()
    data class Failure(val errorMessage: String) : DataState<Nothing>()
}

fun getDate(timestamp: Long) :String {
    val calendar = Calendar.getInstance(Locale.ENGLISH)
    calendar.timeInMillis = timestamp * 1000L
    return DateFormat.format("MMM dd yyyy", calendar).toString()
}

fun getDollarAmount(cents: Long): String {
    val numberFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale.US)
    return numberFormat.format(cents / 100.0)
}