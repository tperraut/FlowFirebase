package com.github.tperraut.flowfirebase.config

import com.github.tperraut.flowfirebase.helpers.asFlow
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.flow.Flow

/**
 * Fetch [FirebaseRemoteConfig] and convert the Google Task to a [Flow] emitting [Void] if the task succeed before
 * completing or cancelling if it failed
 */

fun FirebaseRemoteConfig.fetchAsFlow(): Flow<Void> = fetch().asFlow()

/**
 * Fetch [FirebaseRemoteConfig] with cache expiration time in second and convert the Google Task to a [Flow] emitting
 * [Void] if the task succeed before completing or cancelling if it failed
 * @param cacheExpirationSeconds the cache expiration time in second
 */

fun FirebaseRemoteConfig.fetchAsFlow(cacheExpirationSeconds: Long): Flow<Void> =
    fetch(cacheExpirationSeconds).asFlow()