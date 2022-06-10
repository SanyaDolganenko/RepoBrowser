package ua.dolhanenko.repobrowser.view

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider


class LoginVM : ViewModel() {
    val result: MutableLiveData<String?> = MutableLiveData()

    fun onLoginClick(activity: Activity, login: String) {
        val provider = OAuthProvider.newBuilder("github.com")
            .addCustomParameter("login", login)
            .setScopes(listOf("user:email"))
            .build()
        FirebaseAuth.getInstance()
            .startActivityForSignInWithProvider(activity, provider)
            .addOnCompleteListener {
                when {
                    it.isSuccessful -> onLoginSuccess()
                    it.isCanceled -> onLoginCancel()
                    else -> onLoginFail()
                }
            }
    }


    private fun onLoginCancel() {

    }

    private fun onLoginSuccess() {
        result.postValue("Good")
    }

    private fun onLoginFail() {
        result.postValue("Bad")
    }
}