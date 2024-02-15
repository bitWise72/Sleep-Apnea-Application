package com.example.sleepapnea.presentation.sign_in

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.example.sleepapnea.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.util.concurrent.CancellationException

class GoogleSignInClient(
    private val context: Context,
    private val oneTapClient:SignInClient
) {

    private val auth= Firebase.auth

    suspend fun signIn(): IntentSender?{
        val result= try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        }catch (e:Exception){
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }

        return result?.pendingIntent?.intentSender
    }

    suspend fun getSignInResultFromIntent(intent: Intent): SignInResult{
        val credential= oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken= credential.googleIdToken
        val googleCredentials= GoogleAuthProvider.getCredential(googleIdToken,null)

        return try {
            val user= auth.signInWithCredential(googleCredentials).await().user
            SignInResult(
                userData = user?.run {
                    UserData(user.uid,
                        user.displayName,
                        user.photoUrl.toString())
                },
                errorMessage = null
            )
        }catch (e: Exception){
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(
                userData = null,
                errorMessage = e.message
            )
        }
    }

    suspend fun signOut(){
        try {
           oneTapClient.signOut().await()
           auth.signOut()
        }catch (e:Exception){
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    fun getSignedInUser(): UserData?= auth.currentUser?.run {
        UserData(uid,
            displayName,
            photoUrl.toString())
    }
    private fun buildSignInRequest(): BeginSignInRequest{
        return BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(context.getString(R.string.Google_web_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            .setAutoSelectEnabled(true)
            .build()
    }
}