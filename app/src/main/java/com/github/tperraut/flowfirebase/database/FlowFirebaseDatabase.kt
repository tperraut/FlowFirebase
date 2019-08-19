package com.github.tperraut.flowfirebase.database

import com.github.tperraut.flowfirebase.database.mapper.DataSnapshotMapper
import com.github.tperraut.flowfirebase.database.model.FlowFirebaseChildEvent
import com.github.tperraut.flowfirebase.exceptions.FlowFirebaseDataException
import com.google.firebase.database.*
import com.google.firebase.database.core.view.Event
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

@FlowPreview
object FlowFirebaseDatabase {

    fun collectValueEvent(query: Query): Flow<DataSnapshot> = flow {
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                throw FlowFirebaseDataException(error)
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                runBlocking { emit(dataSnapshot) }
            }

        })
    }

    fun <T> collectValueEvent(query: Query, mapper: suspend (DataSnapshot) -> T): Flow<T> {
        return collectValueEvent(query).map(mapper)
    }

    fun <T> collectValueEvent(query: Query, clazz: Class<T>): Flow<T> {
        return collectValueEvent(query, DataSnapshotMapper.of(clazz))
    }

    fun collectSingleValueEvent(query: Query): Flow<DataSnapshot> = flow {
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                throw FlowFirebaseDataException(error)
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                runBlocking {
                    emit(dataSnapshot)
                }
                query.removeEventListener(this)
            }
        })
    }

    fun <T> collectSingleValueEvent(query: Query, mapper: suspend (DataSnapshot) -> T): Flow<T> {
        return collectSingleValueEvent(query).map(mapper)
    }

    fun <T> collectSingleValueEvent(query: Query, clazz: Class<T>): Flow<T> {
        return collectSingleValueEvent(query, DataSnapshotMapper.of(clazz))
    }

    fun collectChildEvent(query: Query): Flow<FlowFirebaseChildEvent<DataSnapshot>> = flow {
        query.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {
                throw FlowFirebaseDataException(error)
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                runBlocking {
                    emit(
                        FlowFirebaseChildEvent(
                            dataSnapshot.key!!,
                            dataSnapshot,
                            Event.EventType.CHILD_MOVED,
                            previousChildName
                        )
                    )
                }
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                runBlocking {
                    emit(
                        FlowFirebaseChildEvent(
                            dataSnapshot.key!!,
                            dataSnapshot,
                            Event.EventType.CHILD_CHANGED,
                            previousChildName
                        )
                    )
                }
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                runBlocking {
                    emit(
                        FlowFirebaseChildEvent(
                            dataSnapshot.key!!,
                            dataSnapshot,
                            Event.EventType.CHILD_ADDED,
                            previousChildName
                        )
                    )
                }
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                runBlocking {
                    emit(
                        FlowFirebaseChildEvent(
                            dataSnapshot.key!!,
                            dataSnapshot,
                            Event.EventType.CHILD_REMOVED
                        )
                    )
                }
            }
        })
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