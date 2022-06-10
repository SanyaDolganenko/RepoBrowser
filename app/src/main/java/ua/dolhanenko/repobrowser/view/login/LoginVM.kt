package ua.dolhanenko.repobrowser.view.login

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.OAuthCredential
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.dolhanenko.repobrowser.application.RepoApp
import ua.dolhanenko.repobrowser.domain.usecases.LoginUseCase


class LoginVM(private val loginUseCase: LoginUseCase) : ViewModel() {
    val isLoggingIn: MutableLiveData<Boolean> = MutableLiveData(false)
    val result: MutableLiveData<Boolean?> = MutableLiveData()

    fun onLoginClick(activity: Activity, userName: String) {
        isLoggingIn.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            val result = loginUseCase(userName, activity)
            when {
                result.isSuccessful -> onLoginSuccess(result.result)
                result.isCanceled -> onLoginCancel()
                else -> onLoginFail()
            }
        }
    }


    private fun onLoginCancel() {
        isLoggingIn.postValue(false)
    }

    private fun onLoginSuccess(authResult: AuthResult) {
        RepoApp.activeToken = (authResult.credential as OAuthCredential).accessToken
        result.postValue(RepoApp.activeToken != null)
        isLoggingIn.postValue(false)
    }

    private fun onLoginFail() {
        result.postValue(false)
        isLoggingIn.postValue(false)
    }
}