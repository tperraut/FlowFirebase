package com.github.tperraut.flowfirebase.exceptions

import com.google.firebase.database.DatabaseError

data class FlowFirebaseDataException(val error: DatabaseError) : Exception() {

    override fun toString(): String {
        return "${this.javaClass.simpleName}{error=$error}"
    }
}