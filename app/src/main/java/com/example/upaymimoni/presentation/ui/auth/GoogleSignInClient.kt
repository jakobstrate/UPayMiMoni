package com.example.upaymimoni.presentation.ui.auth

import android.content.Context
import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.example.upaymimoni.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL

class GoogleSignInClient(
    val context: Context
) {
    /**
     * Gets the idToken after going through a google credential sign-in.
     *
     * Will return null if the sign in is cancelled
     */
    suspend fun launchCredentialManager(): String? {
        val request = configureRequest()

        try {
            val result = CredentialManager.create(context).getCredential(
                request = request,
                context = context
            )

            return getIdToken(result.credential)

        } catch (e: GetCredentialException) {
            Log.e("GoogleAuth", "Couldn't retrieve user's credentials: ${e.message}")
            return null
        }
    }

    private fun configureRequest(): GetCredentialRequest {
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(true)
            .setNonce("")
            .setServerClientId(context.getString(R.string.default_web_client_id))
            .build()

        return GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }

    /**
     * Gets the idToken from a credential. Return null if it is not a google id.
     *
     * @return The idToken of the account as a string, null if it is not a google account.
     */
    private fun getIdToken(credential: Credential): String? {
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            return googleIdTokenCredential.idToken
        } else {
            Log.w("GoogleAuth", "Credential is not of type Google ID")
            return null
        }
    }
}