package com.github.tperraut.flowfirebase.helpers

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flow

@FlowPreview
fun <TResult> Task<TResult>.asFlow() = flow {
    emit(Tasks.await(this@asFlow))
}