package com.github.tperraut.flowfirebase.helpers

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flowViaChannel

@FlowPreview
fun <TResult> Task<TResult>.asFlow() = flowViaChannel<TResult> { channel ->
    addOnSuccessListener {
        channel.offer(it)
        channel.close()
    }
    addOnFailureListener { channel.close(it) }
}