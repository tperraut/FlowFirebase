package com.github.tperraut.flowfirebase.database

import com.github.tperraut.flowfirebase.database.mapper.DataSnapshotMapper
import com.github.tperraut.flowfirebase.database.model.FlowFirebaseChildEvent
import com.github.tperraut.flowfirebase.exceptions.FlowFirebaseDataException
import com.google.firebase.database.*
import com.google.firebase.database.core.view.Event
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map

@ExperimentalCoroutinesApi
object FlowFirebaseDatabase {

    fun collectValueEvent(query: Query): Flow<DataSnapshot> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                cancel(error.message, FlowFirebaseDataException(error))
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                offer(dataSnapshot)
            }
        }
        query.addValueEventListener(listener)
        awaitClose { query.removeEventListener(listener) }
    }

    fun <T> collectValueEvent(query: Query, mapper: suspend (DataSnapshot) -> T): Flow<T> {
        return collectValueEvent(query).map(mapper)
    }

    fun <T> collectValueEvent(query: Query, clazz: Class<T>): Flow<T> {
        return collectValueEvent(query, DataSnapshotMapper.of(clazz))
    }

    fun collectSingleValueEvent(query: Query): Flow<DataSnapshot> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                cancel(error.message, FlowFirebaseDataException(error))
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                offer(dataSnapshot)
                close()
            }
        }
        query.addListenerForSingleValueEvent(listener)
        awaitClose { query.removeEventListener(listener) }
    }

    fun <T> collectSingleValueEvent(query: Query, mapper: suspend (DataSnapshot) -> T): Flow<T> {
        return collectSingleValueEvent(query).map(mapper)
    }

    fun <T> collectSingleValueEvent(query: Query, clazz: Class<T>): Flow<T> {
        return collectSingleValueEvent(query, DataSnapshotMapper.of(clazz))
    }

    fun collectChildEvent(query: Query): Flow<FlowFirebaseChildEvent<DataSnapshot>> = callbackFlow {
        val listener = object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {
                cancel(error.message, FlowFirebaseDataException(error))
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                offer(
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
                offer(
                    FlowFirebaseChildEvent(
                        dataSnapshot.key!!,
                        dataSnapshot,
                        Event.EventType.CHILD_CHANGED,
                        previousChildName
                    )
                )
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                offer(
                    FlowFirebaseChildEvent(
                        dataSnapshot.key!!,
                        dataSnapshot,
                        Event.EventType.CHILD_ADDED,
                        previousChildName
                    )
                )
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                offer(
                    FlowFirebaseChildEvent(
                        dataSnapshot.key!!,
                        dataSnapshot,
                        Event.EventType.CHILD_REMOVED
                    )
                )
            }
        }
        query.addChildEventListener(listener)
        awaitClose { query.removeEventListener(listener) }
    }

    fun <T> collectChildEvent(
        query: Query,
        mapper: suspend (FlowFirebaseChildEvent<DataSnapshot>) -> FlowFirebaseChildEvent<T>
    ): Flow<FlowFirebaseChildEvent<T>> {
        return collectChildEvent(query).map(mapper)
    }

    fun <T> collectChildEvent(query: Query, clazz: Class<T>): Flow<FlowFirebaseChildEvent<T>> {
        return collectChildEvent(query, DataSnapshotMapper.ofChild(clazz))
    }
}