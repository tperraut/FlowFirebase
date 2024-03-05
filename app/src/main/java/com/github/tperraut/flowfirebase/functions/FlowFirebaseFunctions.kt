package com.github.tperraut.flowfirebase.functions

import com.github.tperraut.flowfirebase.helpers.asFlow
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.HttpsCallableResult
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit

/**
 * Call the given [named][name] function with the given [data] and convert the created google Task to a [Flow]
 * @param name the name of the function to call
 * @param data the parameter of the function
 * @param timeOut the time out in [timeOutUnits]
 * @param timeOutUnits the [timeOut] unit
 * @return an [HttpsCallableResult] containing the result of the function called
 */
fun FirebaseFunctions.getHttpsCallable(
    name: String,
    data: Any? = null,
    timeOut: Long = 60,
    timeOutUnits: TimeUnit = TimeUnit.SECONDS
): Flow<HttpsCallableResult> {
    return getHttpsCallable(name).withTimeout(timeOut, timeOutUnits).call(data).asFlow()
}