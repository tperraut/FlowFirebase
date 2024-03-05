package com.github.tperraut.flowfirebase.user

import com.github.tperraut.flowfirebase.helpers.asFlow
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.Flow

val FFUser = FlowFirebaseUser

object FlowFirebaseUser {

    fun updateEmail(firebaseUser: FirebaseUser, email: String): Flow<Void> =
        firebaseUser.verifyBeforeUpdateEmail(email).asFlow()

    fun updatePassword(firebaseUser: FirebaseUser, password: String): Flow<Void> {
        return firebaseUser.updatePassword(password).asFlow()
    }

    fun updateProfile(firebaseUser: FirebaseUser, request: UserProfileChangeRequest): Flow<Void> {
        return firebaseUser.updateProfile(request).asFlow()
    }

    fun delete(firebaseUser: FirebaseUser): Flow<Void> = firebaseUser.delete().asFlow()

    fun reauthenticate(firebaseUser: FirebaseUser, credential: AuthCredential): Flow<Void> {
        return firebaseUser.reauthenticate(credential).asFlow()
    }

    fun linkWithCredential(
        firebaseUser: FirebaseUser,
        credential: AuthCredential
    ): Flow<AuthResult> {
        return firebaseUser.linkWithCredential(credential).asFlow()
    }
}