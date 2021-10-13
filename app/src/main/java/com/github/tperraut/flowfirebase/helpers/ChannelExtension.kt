package com.github.tperraut.flowfirebase.helpers

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.SendChannel

/**
 * Check if the channel is not closed and try to emit a value, catching [CancellationException] if the corresponding
 * has been cancelled. This extension is used in call callbackFlow.
 */
@ExperimentalCoroutinesApi
fun <E> SendChannel<E>.safeOffer(value: E): Boolean {
    if (isClosedForSend) return false
    return try {
        trySend(value).isSuccess
    } catch (e: CancellationException) {
        false
    }
}