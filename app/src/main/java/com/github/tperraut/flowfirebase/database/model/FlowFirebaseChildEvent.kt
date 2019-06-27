package com.github.tperraut.flowfirebase.database.model

import com.google.firebase.database.core.view.Event

data class FlowFirebaseChildEvent<T>(
    val key: String,
    val value: T?,
    val eventType: Event.EventType,
    val previousChild: String? = null
) {
    val hasError: Boolean get() = value == null
}