package com.github.tperraut.flowfirebase.storage

import android.net.Uri
import com.github.tperraut.flowfirebase.helpers.asFlow
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StreamDownloadTask
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.io.InputStream

/**
 * Get bytes from this [StorageReference] with a [maximum][maxDownloadSize] and convert the Google Task to
 * a [Flow] emitting the [ByteArray] of data if the task succeed.
 * The Flow is cancelled if the Task failed.
 * @param maxDownloadSize the maximum [Byte] to be downloaded, if the result size exceed [maxDownloadSize] the flow task
 * will fail with [IndexOutOfBoundsException] cause
 */
fun StorageReference.getBytesAsFlow(maxDownloadSize: Long): Flow<ByteArray> {
    return getBytes(maxDownloadSize).asFlow()
}

/**
 * Get the data [Uri] from this [StorageReference] and convert the Google Task to a [Flow] emitting the
 * data [Uri] if the task succeed.
 * The Flow is cancelled if the Task failed.
 */
fun StorageReference.getDownloadUrlAsFlow(): Flow<Uri> = downloadUrl.asFlow()

/**
 * Download a file from this [StorageReference] by writing it in the given [destinationFile] and convert the
 * Google Task to a [Flow] emitting a [FileDownloadTask.TaskSnapshot] that contains the info of the download if the
 * task succeed.
 * The Flow is cancelled if the Task failed.
 */
fun StorageReference.getFileAsFlow(destinationFile: File): Flow<FileDownloadTask.TaskSnapshot> {
    return getFile(destinationFile).asFlow()
}

/**
 * Download a file from this [StorageReference] by writing it in the given [destinationUri] and convert the
 * Google Task to a [Flow] emitting a [FileDownloadTask.TaskSnapshot] that contains the info of the download if the
 * task succeed.
 * The Flow is cancelled if the Task failed.
 */
fun StorageReference.getFileAsFlow(destinationUri: Uri): Flow<FileDownloadTask.TaskSnapshot> {
    return getFile(destinationUri).asFlow()
}

/**
 * Retrieve the metadata of data from this [StorageReference] and convert the Google Task to a [Flow] emitting
 * a [StorageMetadata] that contains the metadata of the pointed file if the task succeed.
 * The Flow is cancelled if the Task failed.
 */
fun StorageReference.getMetadataAsFlow(): Flow<StorageMetadata> = metadata.asFlow()

/**
 * Download data from this [StorageReference] as a [InputStream] and convert the Google Task to a [Flow] emitting a
 * [StreamDownloadTask.TaskSnapshot] that contains a stream you can obtain by using
 * [StreamDownloadTask.TaskSnapshot.getStream] if the task succeed.
 * The Flow is cancelled if the Task failed.
 */
fun StorageReference.getStreamAsFlow(): Flow<StreamDownloadTask.TaskSnapshot> = stream.asFlow()

/**
 * Download data from this [StorageReference] as a Stream and convert the Google Task to a [Flow] emitting a
 * [StreamDownloadTask.TaskSnapshot] that contains a stream you can obtain by using
 * [StreamDownloadTask.TaskSnapshot.getStream] if the task succeed before completing or cancelling if it failed.
 * @param processor the [StreamDownloadTask.StreamProcessor] that is responsible from reading the data from the
 * [InputStream]
 */
fun StorageReference.getStreamAsFlow(
    processor: StreamDownloadTask.StreamProcessor
): Flow<StreamDownloadTask.TaskSnapshot> = getStream(processor).asFlow()

/**
 * Put data to this [StorageReference] and convert the Google Task to a [Flow] emitting an
 * [UploadTask.TaskSnapshot] that contains the upload info if the task succeed.
 * The Flow is cancelled if the Task failed.
 * @param byteArray the data as a [ByteArray] to store on Firebase
 */
fun StorageReference.putBytesAsFlow(byteArray: ByteArray): Flow<UploadTask.TaskSnapshot> {
    return putBytes(byteArray).asFlow()
}

/**
 * Put data to this [StorageReference] with custom metadata and convert the Google Task to a [Flow] emitting an
 * [UploadTask.TaskSnapshot] that contains the upload info if the task succeed.
 * The Flow is cancelled if the Task failed.
 * @param byteArray the data as a [ByteArray] to store on Firebase
 * @param metadata the custom metadata as a [StorageMetadata] object
 */
fun StorageReference.putBytesAsFlow(
    byteArray: ByteArray,
    metadata: StorageMetadata
): Flow<UploadTask.TaskSnapshot> = putBytes(byteArray, metadata).asFlow()

/**
 * Put a file to this [StorageReference] and convert the Google Task to a [Flow] emitting an
 * [UploadTask.TaskSnapshot] that contains the upload info if the task succeed.
 * The Flow is cancelled if the Task failed.
 * @param uri the [Uri] of the file to upload
 */
fun StorageReference.putFileAsFlow(
    uri: Uri
): Flow<UploadTask.TaskSnapshot> = putFile(uri).asFlow()

/**
 * Put a file to this [StorageReference] with custom metadata and convert the Google Task to a [Flow] emitting an
 * [UploadTask.TaskSnapshot] that contains the upload info if the task succeed.
 * The Flow is cancelled if the Task failed.
 * @param uri the [Uri] of the file to upload
 * @param metadata the custom metadata as a [StorageMetadata] object
 */
fun StorageReference.putFileAsFlow(
    uri: Uri,
    metadata: StorageMetadata
): Flow<UploadTask.TaskSnapshot> = putFile(uri, metadata).asFlow()

/**
 * Put a file to this [StorageReference] with custom metadata and convert the Google Task to a [Flow] emitting an
 * [UploadTask.TaskSnapshot] that contains the upload info if the task succeed.
 * The Flow is cancelled if the Task failed.
 * @param uri the [Uri] of the file to upload
 * @param metadata the custom metadata as a [StorageMetadata] object
 * @param existingUploadUri If set, an attempt is made to resume an existing upload session
 */
fun StorageReference.putFileAsFlow(
    uri: Uri,
    metadata: StorageMetadata,
    existingUploadUri: Uri
): Flow<UploadTask.TaskSnapshot> = putFile(uri, metadata, existingUploadUri).asFlow()

/**
 * Upload a stream of data to this [StorageReference] and convert the Google Task to a [Flow] emitting an
 * [UploadTask.TaskSnapshot] that contains the upload info if the task succeed.
 * The Flow is cancelled if the Task failed.
 * @param stream the stream of data to upload
 */
fun StorageReference.putStreamAsFlow(
    stream: InputStream
): Flow<UploadTask.TaskSnapshot> = putStream(stream).asFlow()

/**
 * Upload a stream of data to this [StorageReference] with custom metadata and convert the Google Task to a [Flow]
 * emitting an [UploadTask.TaskSnapshot] that contains the upload info if the task succeed.
 * The Flow is cancelled if the Task failed.
 * @param stream the stream of data to upload
 * @param metadata the custom metadata as a [StorageMetadata] object
 */
fun StorageReference.putStreamAsFlow(
    stream: InputStream,
    metadata: StorageMetadata
): Flow<UploadTask.TaskSnapshot> = putStream(stream, metadata).asFlow()


fun StorageReference.updateMetadataAsFlow(metadata: StorageMetadata): Flow<StorageMetadata> {
    return updateMetadata(metadata).asFlow()
}

/**
 * Delete data pointed by this [StorageReference] and convert the Google Task to a [Flow] emitting [Void]
 * The Flow is cancelled if the Task failed.
 */
fun StorageReference.deleteAsFlow(): Flow<Void> = delete().asFlow()