package com.github.tperraut.flowfirebase.database

import android.annotation.SuppressLint
import com.github.tperraut.flowfirebase.database.mapper.DataSnapshotMapper
import com.github.tperraut.flowfirebase.database.model.FlowFirebaseChildEvent
import com.github.tperraut.flowfirebase.exceptions.FlowFirebaseDataException
import com.github.tperraut.flowfirebase.helpers.safeOffer
import com.google.firebase.database.*
import com.google.firebase.database.core.view.Event
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map

/**
 * Observe a query as a [Flow] emitting a value on every changes
 */
@ExperimentalCoroutinesApi
fun Query.collectDefaultValueEvent(): Flow<DataSnapshot> = callbackFlow {
    val listener = object : ValueEventListener {
        override fun onCancelled(error: DatabaseError) {
            cancel(error.message, FlowFirebaseDataException(error))
        }

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            safeOffer(dataSnapshot)
        }
    }
    addValueEventListener(listener)
    awaitClose { removeEventListener(listener) }
}

/**
 * Observe a query as a [Flow] emitting a value on every changes mapping the result with the given mapper
 * @param mapper the function to transform a [DataSnapshot] to a type [T]
 */
@ExperimentalCoroutinesApi
fun <T> Query.collectValueEvent(mapper: (DataSnapshot) -> T): Flow<T> {
    return collectDefaultValueEvent().map { mapper(it) }
}

/**
 * Observe a query as a [Flow] emitting a value on every changes mapping the result in a suspending way with the given
 * mapper
 * @param mapper the suspend function to transform a [DataSnapshot] to a type [T]
 */
@ExperimentalCoroutinesApi
fun <T> Query.collectValueEvent(mapper: suspend (DataSnapshot) -> T): Flow<T> {
    return collectDefaultValueEvent().map(mapper)
}

/**
 * Observe a query as a [Flow] emitting a value on every changes mapping the result with the given type [T]
 */
@ExperimentalCoroutinesApi
inline fun <reified T> Query.collectValueEvent(): Flow<T> {
    return collectValueEvent(DataSnapshotMapper.of())
}

/**
 * Get the value of a query as a [Flow] emitting a value only once before completing
 */
@ExperimentalCoroutinesApi
fun Query.collectDefaultSingleValueEvent(): Flow<DataSnapshot> = callbackFlow {
    val listener = object : ValueEventListener {
        override fun onCancelled(error: DatabaseError) {
            cancel(error.message, FlowFirebaseDataException(error))
        }

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            safeOffer(dataSnapshot)
            close()
        }
    }
    addListenerForSingleValueEvent(listener)
    awaitClose { removeEventListener(listener) }
}

/**
 * Get the value of a query as a [Flow] emitting a value only once before completing and mapping the result with the
 * given mapper
 */
@ExperimentalCoroutinesApi
fun <T> Query.collectSingleValueEvent(mapper: (DataSnapshot) -> T): Flow<T> {
    return collectDefaultSingleValueEvent().map { mapper(it) }
}

/**
 * Get the value of a query as a [Flow] emitting a value only once before completing and mapping the result in a
 * suspending way with the given mapper
 */
@ExperimentalCoroutinesApi
fun <T> Query.collectSingleValueEvent(mapper: suspend (DataSnapshot) -> T): Flow<T> {
    return collectDefaultSingleValueEvent().map(mapper)
}

/**
 * Get the value of a query as a [Flow] emitting a value only once before completing and mapping the result with the
 * given type [T]
 */
@ExperimentalCoroutinesApi
inline fun <reified T> Query.collectSingleValueEvent(): Flow<T> {
    return collectSingleValueEvent(DataSnapshotMapper.of())
}

/**
 * Observe a query children as a [Flow] emitting a value on every child event and wrapping the result in a
 * [FlowFirebaseChildEvent]
 */
@ExperimentalCoroutinesApi
@SuppressLint("RestrictedApi")
fun Query.collectDefaultChildEvent(): Flow<FlowFirebaseChildEvent<DataSnapshot>> = callbackFlow {
    val listener = object : ChildEventListener {
        override fun onCancelled(error: DatabaseError) {
            cancel(error.message, FlowFirebaseDataException(error))
        }

        override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
            safeOffer(
                FlowFirebaseChildEvent(
                    dataSnapshot.key!!,
                    dataSnapshot,
                    Event.EventType.CHILD_MOVED,
                    previousChildName
                )
            )
        }

        override fun onChildChanged(
            dataSnapshot: DataSnapshot,
            previousChildName: String?
        ) {
            safeOffer(
                FlowFirebaseChildEvent(
                    dataSnapshot.key!!,
                    dataSnapshot,
                    Event.EventType.CHILD_CHANGED,
                    previousChildName
                )
            )
        }

        override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
            safeOffer(
                FlowFirebaseChildEvent(
                    dataSnapshot.key!!,
                    dataSnapshot,
                    Event.EventType.CHILD_ADDED,
                    previousChildName
                )
            )
        }

        override fun onChildRemoved(dataSnapshot: DataSnapshot) {
            safeOffer(
                FlowFirebaseChildEvent(
                    dataSnapshot.key!!,
                    dataSnapshot,
                    Event.EventType.CHILD_REMOVED
                )
            )
        }
    }
    addChildEventListener(listener)
    awaitClose { removeEventListener(listener) }
}

/**
 * Observe a query children as a [Flow] emitting a value on every child event and wrapping the result in a
 * [FlowFirebaseChildEvent] and mapping the result with the given mapper
 */
@ExperimentalCoroutinesApi
fun <T> Query.collectChildEvent(
    mapper: (FlowFirebaseChildEvent<DataSnapshot>) -> FlowFirebaseChildEvent<T>
): Flow<FlowFirebaseChildEvent<T>> {
    return collectDefaultChildEvent().map { mapper(it) }
}

/**
 * Observe a query children as a [Flow] emitting a value on every child event and wrapping the result in a
 * [FlowFirebaseChildEvent] and mapping the result in a suspending way with the given mapper
 */
@ExperimentalCoroutinesApi
fun <T> Query.collectChildEvent(
    mapper: suspend (FlowFirebaseChildEvent<DataSnapshot>) -> FlowFirebaseChildEvent<T>
): Flow<FlowFirebaseChildEvent<T>> {
    return collectDefaultChildEvent().map(mapper)
}

/**
 * Observe a query children as a [Flow] emitting a value on every child event and wrapping the result in a
 * [FlowFirebaseChildEvent] and mapping the result with the given type [T]
 */
@ExperimentalCoroutinesApi
inline fun <reified T> Query.collectChildEvent(): Flow<FlowFirebaseChildEvent<T>> {
    return collectChildEvent(DataSnapshotMapper.ofChild())
}