package com.github.tperraut.flowfirebase.auth

import com.github.tperraut.flowfirebase.helpers.asFlow
import com.google.firebase.auth.*
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@FlowPreview
object FlowFirebaseAuth {

    fun signInAnonymously(firebaseAuth: FirebaseAuth): Flow<AuthResult> = firebaseAuth.signInAnonymously().asFlow()

    fun signInWithEmailAndPassword(firebaseAuth: FirebaseAuth, email: String, password: String): Flow<AuthResult> {
        return firebaseAuth.signInWithEmailAndPassword(email, password).asFlow()
    }

    fun signInWithCredential(firebaseAuth: FirebaseAuth, credential: AuthCredential): Flow<AuthResult> {
        return firebaseAuth.signInWithCredential(credential).asFlow()
    }

    fun signInWithCustomToken(firebaseAuth: FirebaseAuth, token: String): Flow<AuthResult> {
        return firebaseAuth.signInWithCustomToken(token).asFlow()
    }

    fun createUserWithEmailAndPassword(firebaseAuth: FirebaseAuth, email: String, password: String): Flow<AuthResult> {
        return firebaseAuth.createUserWithEmailAndPassword(email, password).asFlow()
    }

    fun fetchSignInMethodForEmail(firebaseAuth: FirebaseAuth, email: String): Flow<SignInMethodQueryResult> {
        return firebaseAuth.fetchSignInMethodsForEmail(email).asFlow()
    }

    fun sendPasswordResetEmail(firebaseAuth: FirebaseAuth, email: String): Flow<Void> {
        return firebaseAuth.sendPasswordResetEmail(email).asFlow()
    }

    fun collectAuthState(firebaseAuth: FirebaseAuth): Flow<FirebaseUser?> = flow {
        firebaseAuth.addAuthStateListener { suspend { emit(it.currentUser) } }
    }
}