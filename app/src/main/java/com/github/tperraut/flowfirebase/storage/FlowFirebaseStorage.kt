package com.github.tperraut.flowfirebase.storage

import android.net.Uri
import com.github.tperraut.flowfirebase.helpers.asFlow
import com.google.firebase.storage.*
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.io.InputStream

@FlowPreview
object FlowFirebaseStorage {

    fun getBytes(storageRef: StorageReference, maxDownloadSize: Long): Flow<ByteArray> {
        return storageRef.getBytes(maxDownloadSize).asFlow()
    }

    fun getDownloadUrl(storageRef: StorageReference): Flow<Uri> = storageRef.downloadUrl.asFlow()

    fun getFile(storageRef: StorageReference, destinationFile: File): Flow<FileDownloadTask.TaskSnapshot> {
        return storageRef.getFile(destinationFile).asFlow()
    }

    fun getFile(storageRef: StorageReference, destinationUri: Uri): Flow<FileDownloadTask.TaskSnapshot> {
        return storageRef.getFile(destinationUri).asFlow()
    }

    fun getMetadata(storageRef: StorageReference): Flow<StorageMetadata> = storageRef.metadata.asFlow()

    fun getStream(storageRef: StorageReference): Flow<StreamDownloadTask.TaskSnapshot> = storageRef.stream.asFlow()

    fun getStream(
        storageRef: StorageReference,
        processor: StreamDownloadTask.StreamProcessor
    ): Flow<StreamDownloadTask.TaskSnapshot> = storageRef.getStream(processor).asFlow()

    fun putBytes(storageRef: StorageReference, byteArray: ByteArray): Flow<UploadTask.TaskSnapshot> {
        return storageRef.putBytes(byteArray).asFlow()
    }

    fun putBytes(
        storageRef: StorageReference,
        byteArray: ByteArray,
        metadata: StorageMetadata
    ): Flow<UploadTask.TaskSnapshot> = storageRef.putBytes(byteArray, metadata).asFlow()

    fun putFile(
        storageRef: StorageReference,
        uri: Uri
    ): Flow<UploadTask.TaskSnapshot> = storageRef.putFile(uri).asFlow()

    fun putFile(
        storageRef: StorageReference,
        uri: Uri,
        metadata: StorageMetadata
    ): Flow<UploadTask.TaskSnapshot> = storageRef.putFile(uri, metadata).asFlow()

    fun putFile(
        storageRef: StorageReference,
        uri: Uri,
        metadata: StorageMetadata,
        existingUploadUri: Uri
    ): Flow<UploadTask.TaskSnapshot> = storageRef.putFile(uri, metadata, existingUploadUri).asFlow()

    fun putStream(
        storageRef: StorageReference,
        stream: InputStream
    ): Flow<UploadTask.TaskSnapshot> = storageRef.putStream(stream).asFlow()

    fun putStream(
        storageRef: StorageReference,
        stream: InputStream,
        metadata: StorageMetadata
    ): Flow<UploadTask.TaskSnapshot> = storageRef.putStream(stream, metadata).asFlow()

    fun updateMetadata(storageRef: StorageReference, metadata: StorageMetadata): Flow<StorageMetadata> {
        return storageRef.updateMetadata(metadata).asFlow()
    }

    fun delete(storageRef: StorageReference): Flow<Void> = storageRef.delete().asFlow()
}