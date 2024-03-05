package com.github.tperraut.flowfirebase.helpers

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

/**
 * Convert a google [Task] to a flow by adding a success listener and
 * failure listener and emitting only once when the Task succeed before completing or cancelling
 * if the Task failed
 */
fun <TResult> Task<TResult>.asFlow() = callbackFlow<TResult> {
    addOnSuccessListener {
        safeOffer(it)
        close()
    }
    addOnFailureListener { e -> cancel(e.message ?: "", e) }
    awaitClose()
}