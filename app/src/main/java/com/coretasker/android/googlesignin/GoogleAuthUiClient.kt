package com.coretasker.android.googlesignin

import androidx.credentials.GetCredentialRequest
import com.coretasker.android.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class GoogleAuthUiClient {
   /* private lateinit var auth: FirebaseAuth
    auth = Firebase.auth

    val googleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(true)
        .setServerClientId(*//*baseContext.getString(R.string.default_web_client_id)*//*"1074384944818-kqfiqv3rfjfutj0kvhn5pod1m7bquqqs.apps.googleusercontent.com")
        .build()

    val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    val credential = GoogleAuthProvider.getCredential(idToken, null)
    auth.signInWithCredential(credential)
    .addOnCompleteListener(this) { task ->
        if (task.isSuccessful) {
            val user = auth.currentUser
            updateUI(user)
        } else {
            updateUI(null)
        }
    }*/
}