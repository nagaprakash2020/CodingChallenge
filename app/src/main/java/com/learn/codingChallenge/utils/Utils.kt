package com.learn.codingChallenge.utils

sealed class DataState<out T> {
    data class Success<out T>(val data : T) : DataState<T>()
    data class Failure(val errorMessage : String) : DataState<Nothing>()
}