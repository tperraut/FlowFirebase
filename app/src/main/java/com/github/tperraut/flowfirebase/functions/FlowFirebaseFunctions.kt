package com.github.tperraut.flowfirebase.functions

import com.github.tperraut.flowfirebase.helpers.asFlow
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.HttpsCallableResult
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit

@FlowPreview
object FlowFirebaseFunctions {

    fun getHttpsCallable(
        functions: FirebaseFunctions,
        name: String,
        data: Any? = null,
        timeOut: Long = 60,
        timeOutUnits: TimeUnit = TimeUnit.SECONDS
    ): Flow<HttpsCallableResult> {
        return functions.getHttpsCallable(name).withTimeout(timeOut, timeOutUnits).call(data).asFlow()
    }
}