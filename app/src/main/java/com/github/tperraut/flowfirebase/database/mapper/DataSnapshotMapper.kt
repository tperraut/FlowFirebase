package com.github.tperraut.flowfirebase.database.mapper

import com.github.tperraut.flowfirebase.database.model.FlowFirebaseChildEvent
import com.github.tperraut.flowfirebase.exceptions.FlowFirebaseDataCastException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.GenericTypeIndicator

object DataSnapshotMapper {

    /**
     * Try to convert a [DataSnapshot] to the given type T
     * @return a lambda to be use with FlowFirebaseDatabase functions
     * @throws FlowFirebaseDataCastException if the converted value is null or a [DatabaseException] is thrown
     */
    inline fun <reified T> of(): (DataSnapshot) -> T = { dataSnapshot ->
        try {
            val value = dataSnapshot.getValue(T::class.java)
            value ?: throw FlowFirebaseDataCastException("Unable to cast $dataSnapshot to ${T::class.java.simpleName}")
            value
        } catch (e: DatabaseException) {
            throw FlowFirebaseDataCastException("Unable to cast $dataSnapshot to ${T::class.java.simpleName}", e)
        }
    }

    /**
     * Try to convert a [DataSnapshot] to the given type T safely by returning null if the
     * converted value is null or when a [DatabaseException] is thrown
     * @return a lambda to be use with FlowFirebaseDatabase functions
     */
    inline fun <reified T> safeOf(): (DataSnapshot) -> T? = { dataSnapshot ->
        try {
            val value = dataSnapshot.getValue(T::class.java)
            value
        } catch (e: DatabaseException) {
            null
        }
    }

    /**
     * Try to convert [datasnapshot children][DataSnapshot.getChildren] to a [List] of the given type T
     * @return a lambda to be use with FlowFirebaseDatabase functions
     * @throws FlowFirebaseDataCastException if a converted element is null or a [DatabaseException] is thrown
     */
    inline fun <reified T> listOf(): (DataSnapshot) -> List<T> = { dataSnapshot ->
        dataSnapshot.children.map { of<T>().invoke(it) }
    }

    /**
     * Try to convert [datasnapshot children][DataSnapshot.getChildren] to a [List] of the given type T safely by
     * ignoring null elements and catching [DatabaseException]
     * @return a lambda to be use with FlowFirebaseDatabase functions
     */
    inline fun <reified T : Any> safeListOf(): (DataSnapshot) -> List<T> = { dataSnapshot ->
        dataSnapshot.children.mapNotNull { safeOf<T>().invoke(it) }
    }

    /**
     * Try to convert [datasnapshot children][DataSnapshot.getChildren] to a [MutableMap] of the given type T
     * @return a lambda to be use with FlowFirebaseDatabase functions
     * @throws FlowFirebaseDataCastException if a converted element is null or a [DatabaseException] is thrown
     */
    inline fun <reified T> mapOf(): (DataSnapshot) -> MutableMap<String, T> = { dataSnapshot ->
        dataSnapshot.children.associate { it.key!! to of<T>().invoke(it) }.toMutableMap()
    }

    /**
     * Try to convert [datasnapshot children][DataSnapshot.getChildren] to a [MutableMap] of the given type T safely by
     * ignoring null elements and catching [DatabaseException]
     * @return a lambda to be use with FlowFirebaseDatabase functions
     * @throws FlowFirebaseDataCastException if a converted element is null or a [DatabaseException] is thrown
     */
    inline fun <reified T> safeMapOf(): (DataSnapshot) -> MutableMap<String, T> = { dataSnapshot ->
        val map: MutableMap<String, T> = mutableMapOf()
        dataSnapshot.children.forEach {
            val value = safeOf<T>().invoke(it)
            value ?: return@forEach
            map[it.key!!] = value
        }
        map
    }

    /**
     * Try to convert a [DataSnapshot] to the given type T defined by the [genericTypeIndicator] param
     * @param genericTypeIndicator the [GenericTypeIndicator] to convert the [DataSnapshot]
     * @return a lambda to be use with FlowFirebaseDatabase functions
     * @throws FlowFirebaseDataCastException if the converted value is null or a [DatabaseException] is thrown
     */
    fun <T> of(genericTypeIndicator: GenericTypeIndicator<T>): (DataSnapshot) -> T = { dataSnapshot ->
        try {
            val value = dataSnapshot.getValue(genericTypeIndicator)
            value ?: throw FlowFirebaseDataCastException("Unable to cast $dataSnapshot with $genericTypeIndicator")
            value
        } catch (e: DatabaseException) {
            throw FlowFirebaseDataCastException("Unable to cast $dataSnapshot with $genericTypeIndicator", e)
        }
    }

    /**
     * Try to convert a [DataSnapshot] to a [FlowFirebaseChildEvent] of type T
     * @return a lambda to be use with FlowFirebaseDatabase functions
     * @throws FlowFirebaseDataCastException if the converted value is null or a [DatabaseException] is thrown
     */
    inline fun <reified T> ofChild(): (FlowFirebaseChildEvent<DataSnapshot>) -> FlowFirebaseChildEvent<T> {
        return { flowFirebaseChildEvent ->
            val dataSnapshot: DataSnapshot = flowFirebaseChildEvent.value!!
            FlowFirebaseChildEvent(
                dataSnapshot.key!!,
                of<T>().invoke(dataSnapshot),
                flowFirebaseChildEvent.eventType,
                flowFirebaseChildEvent.previousChild
            )
        }
    }

    /**
     * Try to convert a [DataSnapshot] to a [FlowFirebaseChildEvent] of type T safely by setting null as
     * [FlowFirebaseChildEvent.value] if the converted value is null or when a [DatabaseException] is thrown
     * @return a lambda to be use with FlowFirebaseDatabase functions
     * @throws FlowFirebaseDataCastException if the converted value is null or a [DatabaseException] is thrown
     */
    inline fun <reified T> ofChildSafe(): (FlowFirebaseChildEvent<DataSnapshot>) -> FlowFirebaseChildEvent<T> {
        return { flowFirebaseChildEvent ->
            val dataSnapshot: DataSnapshot = flowFirebaseChildEvent.value!!
            FlowFirebaseChildEvent(
                dataSnapshot.key!!,
                safeOf<T>().invoke(dataSnapshot),
                flowFirebaseChildEvent.eventType,
                flowFirebaseChildEvent.previousChild
            )
        }
    }
}