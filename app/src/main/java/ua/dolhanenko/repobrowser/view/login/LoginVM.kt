package ua.dolhanenko.repobrowser.view.login

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider
import ua.dolhanenko.repobrowser.application.RepoApp


class LoginVM : ViewModel() {
    val result: MutableLiveData<Boolean?> = MutableLiveData()

    fun onLoginClick(activity: Activity, login: String) {
        val provider = OAuthProvider.newBuilder("github.com")
            .addCustomParameter("login", login)
            .setScopes(listOf("user:email"))
            .build()
        FirebaseAuth.getInstance()
            .startActivityForSignInWithProvider(activity, provider)
            .addOnCompleteListener {
                when {
                    it.isSuccessful -> onLoginSuccess(it.result)
                    it.isCanceled -> onLoginCancel()
                    else -> onLoginFail()
                }
            }
    }


    private fun onLoginCancel() {

    }

    private fun onLoginSuccess(authResult: AuthResult) {
        RepoApp.activeToken = (authResult.credential as OAuthCredential).accessToken
        result.postValue(RepoApp.activeToken != null)

    }

    private fun onLoginFail() {
        result.postValue(false)
    }
}