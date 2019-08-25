package com.github.tperraut.flowfirebase.config

import com.github.tperraut.flowfirebase.helpers.asFlow
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
object FlowFirebaseConfig {

    fun fetch(config: FirebaseRemoteConfig): Flow<Void> = config.fetch().asFlow()

    fun fetch(config: FirebaseRemoteConfig, cacheExpirationSeconds: Long): Flow<Void> {
        return config.fetch(cacheExpirationSeconds).asFlow()
    }
}