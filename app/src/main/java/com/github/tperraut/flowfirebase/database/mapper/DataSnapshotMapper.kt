package com.github.tperraut.flowfirebase.database.mapper

import com.github.tperraut.flowfirebase.database.model.FlowFirebaseChildEvent
import com.github.tperraut.flowfirebase.exceptions.FlowFirebaseDataCastException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.GenericTypeIndicator

object DataSnapshotMapper {

    fun <T> of(clazz: Class<T>): suspend (DataSnapshot) -> T = { dataSnapshot ->
        try {
            val value = dataSnapshot.getValue(clazz)
            value ?: throw FlowFirebaseDataCastException("Unable to cast $dataSnapshot to ${clazz.simpleName}")
            value
        } catch (e: DatabaseException) {
            throw FlowFirebaseDataCastException("Unable to cast $dataSnapshot to ${clazz.simpleName}", e)
        }
    }

    fun <T> safeOf(clazz: Class<T>): suspend (DataSnapshot) -> T? = { dataSnapshot ->
        try {
            val value = dataSnapshot.getValue(clazz)
            value
        } catch (e: DatabaseException) {
            null
        }
    }

    fun <T> listOf(clazz: Class<T>): suspend (DataSnapshot) -> List<T> = { dataSnapshot ->
        dataSnapshot.children.map { of(clazz).invoke(it) }
    }

    fun <T : Any> safeListOf(clazz: Class<T>): suspend (DataSnapshot) -> List<T> = { dataSnapshot ->
        dataSnapshot.children.mapNotNull { safeOf(clazz).invoke(it) }
    }

    fun <T> mapOf(clazz: Class<T>): suspend (DataSnapshot) -> MutableMap<String, T> = { dataSnapshot ->
        dataSnapshot.children.associate { it.key!! to of(clazz).invoke(it) }.toMutableMap()
    }

    fun <T> safeMapOf(clazz: Class<T>): suspend (DataSnapshot) -> MutableMap<String, T> = { dataSnapshot ->
        val map: MutableMap<String, T> = mutableMapOf()
        dataSnapshot.children.forEach {
            val value = safeOf(clazz).invoke(it)
            value ?: return@forEach
            map[it.key!!] = value
        }
        map
    }

    fun <T> of(genericTypeIndicator: GenericTypeIndicator<T>): suspend (DataSnapshot) -> T = { dataSnapshot ->
        try {
            val value = dataSnapshot.getValue(genericTypeIndicator)
            value ?: throw FlowFirebaseDataCastException("Unable to cast $dataSnapshot with $genericTypeIndicator")
            value
        } catch (e: DatabaseException) {
            throw FlowFirebaseDataCastException("Unable to cast $dataSnapshot with $genericTypeIndicator", e)
        }
    }

    fun <T> ofChild(clazz: Class<T>): suspend (FlowFirebaseChildEvent<DataSnapshot>) -> FlowFirebaseChildEvent<T> {
        return { flowFirebaseChildEvent ->
            val dataSnapshot: DataSnapshot = flowFirebaseChildEvent.value!!
            FlowFirebaseChildEvent(
                dataSnapshot.key!!,
                of(clazz).invoke(dataSnapshot),
                flowFirebaseChildEvent.eventType,
                flowFirebaseChildEvent.previousChild
            )
        }
    }

    fun <T> ofChildSafe(clazz: Class<T>): suspend (FlowFirebaseChildEvent<DataSnapshot>) -> FlowFirebaseChildEvent<T> {
        return { flowFirebaseChildEvent ->
            val dataSnapshot: DataSnapshot = flowFirebaseChildEvent.value!!
            FlowFirebaseChildEvent(
                dataSnapshot.key!!,
                safeOf(clazz).invoke(dataSnapshot),
                flowFirebaseChildEvent.eventType,
                flowFirebaseChildEvent.previousChild
            )
        }
    }
}