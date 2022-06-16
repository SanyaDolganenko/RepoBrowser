package ua.dolhanenko.repobrowser.domain.usecases

import android.app.Activity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Deprecated("Activity should not be passed here")
class LoginUseCase @Inject constructor() {
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