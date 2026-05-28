package com.scandoc.core.extension

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import com.scandoc.core.result.Outcome

fun <T> Flow<T>.asOutcomeFlow(): Flow<Outcome<T>> =
    map<T, Outcome<T>> { Outcome.Success(it) }
        .catch { emit(Outcome.Failure(it)) }
