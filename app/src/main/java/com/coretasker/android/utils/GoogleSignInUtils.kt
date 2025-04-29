package com.coretasker.android.utils

import android.R
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.coretasker.android.data.db.AppDatabase
import com.coretasker.android.data.entity.UserEntity
import com.google.android.gms.common.api.GoogleApi
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class GoogleSignInUtils {
    companion object{
        fun doGoogleSignIn(context: Context,scope: CoroutineScope,launcher: ManagedActivityResultLauncher<Intent, ActivityResult>?,login:()->Unit){
            val credentialManager = CredentialManager.create(context)
            val database = AppDatabase.getDatabase(context)
            val userDao = database.userDao()

            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(true)
                .setServerClientId("1074384944818-kqfiqv3rfjfutj0kvhn5pod1m7bquqqs.apps.googleusercontent.com")
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()
            scope.launch {
                try {
                    val result = credentialManager.getCredential(context,request)
                    when(result.credential){
                        is CustomCredential->{
                            if(result.credential.type== GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
                                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
                                val googleTokenId = googleIdTokenCredential.idToken
                                val authCredential = GoogleAuthProvider.getCredential(googleTokenId,null)
                                val user = Firebase.auth.signInWithCredential(authCredential).await().user
                                user?.let {
                                    if(it.isAnonymous.not()){
                                        val userEntity = UserEntity(
                                            uid = it.uid,
                                            name = it.displayName,
                                            email = it.email,
                                            photoUrl = it.photoUrl?.toString()
                                        )
                                        userDao.insertUser(userEntity)
                                        login.invoke()
                                    }
                                }
                            }
                        }
                        else->{

                        }
                    }
                }catch (e: NoCredentialException){
                    launcher?.launch(getIntent())
                }catch (e: GetCredentialException){
                    e.printStackTrace()
                }
            }

        }

        fun getIntent(): Intent{
            return Intent(Settings.ACTION_ADD_ACCOUNT).apply {
                putExtra(Settings.EXTRA_ACCOUNT_TYPES, arrayOf("com.google"))
            }
        }
    }
}