package com.github.tperraut.flowfirebase.helpers

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
fun <TResult> Task<TResult>.asFlow() = callbackFlow<TResult> {
    addOnSuccessListener {
        offer(it)
        close()
    }
    addOnFailureListener { e -> cancel(e.message ?: "", e) }
    awaitClose()
}