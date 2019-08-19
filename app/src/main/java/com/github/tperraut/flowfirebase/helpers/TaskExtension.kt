package com.github.tperraut.flowfirebase.helpers

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flow

@FlowPreview
fun <TResult> Task<TResult>.asFlow() = flow {
    addOnSuccessListener { result ->
        suspend {
            when (result) {
                is Void -> emit(Unit)
                else -> emit(result)
            }
        }
    }
    addOnFailureListener { throw it }
}