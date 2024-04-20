package com.example.news

sealed interface Error

sealed interface Result<out D, out E : Error> {
    data class SuccessResult<out D, out E : Error>(val data: D) : Result<D, E>
    data class ErrorResult<out D, out E : Error>(val error: E) : Result<D, E>
}

sealed interface DataError : Error {
    enum class NetworkError : DataError {
        UNKNOWN,
        REQUEST_TIME_OUT,
        SERVER_DOWN
    }

    enum class DatabaseError : DataError {
        NOT_FOUND,
        UNKNOWN
    }
}