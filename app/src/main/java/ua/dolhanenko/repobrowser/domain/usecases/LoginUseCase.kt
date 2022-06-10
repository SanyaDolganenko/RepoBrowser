package ua.dolhanenko.repobrowser.domain.usecases

import android.app.Activity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class LoginUseCase {
    suspend operator fun invoke(userName: String, activity: Activity): Task<AuthResult> =
        suspendCoroutine { cont ->
            val provider = OAuthProvider.newBuilder("github.com")
                .addCustomParameter("login", userName)
                .setScopes(listOf("user:email"))
                .build()
            FirebaseAuth.getInstance()
                .startActivityForSignInWithProvider(activity, provider)
                .addOnCompleteListener {
                    cont.resume(it)
                }
        }
}