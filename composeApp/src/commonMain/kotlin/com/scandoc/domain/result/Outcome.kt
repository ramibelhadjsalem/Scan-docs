package com.scandoc.domain.result

sealed interface Outcome<out T> {
    data class Success<T>(val value: T) : Outcome<T>
    data class Failure(val error: Throwable) : Outcome<Nothing>
}

fun <T> Result<T>.toOutcome(): Outcome<T> =
    fold(
        onSuccess = { Outcome.Success(it) },
        onFailure = { Outcome.Failure(it) },
    )
