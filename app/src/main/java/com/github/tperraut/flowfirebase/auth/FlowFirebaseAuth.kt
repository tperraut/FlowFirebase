package com.github.tperraut.flowfirebase.auth

import com.github.tperraut.flowfirebase.helpers.asFlow
import com.github.tperraut.flowfirebase.helpers.safeOffer
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.SignInMethodQueryResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Sign in anonymously on Firebase and convert the Google Task to a [Flow] emitting [AuthResult] if the sign in
 * succeed before completing or cancelling if it failed
 */
fun FirebaseAuth.signInAnonymouslyAsFlow(): Flow<AuthResult> = signInAnonymously().asFlow()

/**
 * Sign in with the given email and password on Firebase and convert the Google Task to a [Flow] emitting [AuthResult]
 * if the sign in succeed before completing or cancelling if it failed
 * @param email the user email
 * @param password the user password
 */
fun FirebaseAuth.signInWithEmailAndPasswordAsFlow(
    email: String,
    password: String
): Flow<AuthResult> {
    return signInWithEmailAndPassword(email, password).asFlow()
}

/**
 * Sign in with the given [AuthCredential] on Firebase and convert the Google Task to a [Flow] emitting [AuthResult]
 * if the sign in succeed before completing or cancelling if it failed
 * @param credential the user [AuthCredential]
 */
fun FirebaseAuth.signInWithCredentialAsFlow(credential: AuthCredential): Flow<AuthResult> {
    return signInWithCredential(credential).asFlow()
}

/**
 * Sign in with the given token on Firebase and convert the Google Task to a [Flow] emitting [AuthResult] if the sign
 * in succeed before completing or cancelling if it failed
 * @param token the user token
 */
fun FirebaseAuth.signInWithCustomTokenAsFlow(token: String): Flow<AuthResult> {
    return signInWithCustomToken(token).asFlow()
}

/**
 * Create a user with the given email and password on Firebase and convert the Google Task to a [Flow] emitting
 * [AuthResult] if the sign in succeed before completing or cancelling if it failed
 * @param email the user email
 * @param password the user password
 */
fun FirebaseAuth.createUserWithEmailAndPasswordAsFlow(
    email: String,
    password: String
): Flow<AuthResult> {
    return createUserWithEmailAndPassword(email, password).asFlow()
}

/**
 * Ask Firebase for the user sign in method and convert the Google Task to a [Flow] emitting [SignInMethodQueryResult]
 * if the task succeed before completing or cancelling if it failed
 * @param email the user email
 */
fun FirebaseAuth.fetchSignInMethodForEmailAsFlow(email: String): Flow<SignInMethodQueryResult> {
    return fetchSignInMethodsForEmail(email).asFlow()
}

/**
 * Ask Firebase to reset the user password and convert the Google Task to a [Flow] emitting [Void] if the task succeed
 * before completing or cancelling if it failed
 * @param email the user email
 */
fun FirebaseAuth.sendPasswordResetEmailAsFlow(email: String): Flow<Void> {
    return sendPasswordResetEmail(email).asFlow()
}

/**
 * Observe the [FirebaseUser] changes as [Flow]. A null value meaning that the user is not sign in (ie disconnected)
 */
fun FirebaseAuth.collectAuthState(): Flow<FirebaseUser?> = callbackFlow {
    val listener = FirebaseAuth.AuthStateListener { auth -> safeOffer(auth.currentUser) }
    addAuthStateListener(listener)
    awaitClose { removeAuthStateListener(listener) }
}