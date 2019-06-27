package com.github.tperraut.flowfirebase.exceptions

class FlowFirebaseDataCastException : Exception {
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, throwable: Throwable) : super(message, throwable)
    constructor(throwable: Throwable) : super(throwable)
}