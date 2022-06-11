package ua.dolhanenko.repobrowser.view.login

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.OAuthCredential
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.dolhanenko.repobrowser.application.RepoApp
import ua.dolhanenko.repobrowser.data.remote.BaseApiDataSource
import ua.dolhanenko.repobrowser.domain.usecases.GetActiveUserUseCase
import ua.dolhanenko.repobrowser.domain.usecases.LoginUseCase
import ua.dolhanenko.repobrowser.domain.usecases.QueryUserInfoUseCase
import ua.dolhanenko.repobrowser.domain.usecases.SaveActiveUserUseCase


class LoginVM(
    private val loginUseCase: LoginUseCase,
    private val queryUserInfoUseCase: QueryUserInfoUseCase,
    private val saveActiveUserUseCase: SaveActiveUserUseCase,
    private val getActiveUserUseCase: GetActiveUserUseCase
) : ViewModel() {
    val isLoggingIn: MutableLiveData<Boolean> = MutableLiveData(false)
    val result: MutableLiveData<Boolean?> = MutableLiveData()

    init {
        attemptLoginWthLastActiveUser()
    }

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

    //Attempt to fetch an active user from DB and use their token to fetch user data
    private fun attemptLoginWthLastActiveUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val activeUser = getActiveUserUseCase()
            activeUser?.lastUsedToken?.let {
                RepoApp.activeToken = it
                queryLoggedInUserInfo()
            }
        }
    }

    private fun onLoginCancel() {
        isLoggingIn.postValue(false)
    }

    private suspend fun onLoginSuccess(authResult: AuthResult) {
        RepoApp.activeToken = (authResult.credential as OAuthCredential).accessToken
        isLoggingIn.postValue(false)
        queryLoggedInUserInfo()
    }

    private suspend fun queryLoggedInUserInfo() {
        isLoggingIn.postValue(true)
        var isSuccess = false
        try {
            if (RepoApp.activeToken != null)
                queryUserInfoUseCase(RepoApp.activeToken!!)?.let {
                    saveActiveUserUseCase(it)
                    isSuccess = true
                }
        } catch (e: BaseApiDataSource.NetworkException) {
            if (e.code == BaseApiDataSource.CODE_UNAUTHORIZED) {
                //User token expired
                Log.i("LOGIN_VM", "user token expired or bad credentials")
            }
        }
        isLoggingIn.postValue(false)
        result.postValue(isSuccess)
    }

    private fun onLoginFail() {
        result.postValue(false)
        isLoggingIn.postValue(false)
    }
}