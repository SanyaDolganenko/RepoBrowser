package ua.dolhanenko.repobrowser.presentation.login

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.OAuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.dolhanenko.repobrowser.RepoApp
import ua.dolhanenko.repobrowser.core.Resource
import ua.dolhanenko.repobrowser.data.remote.base.BaseApiDataSource
import ua.dolhanenko.repobrowser.domain.usecases.GetActiveUserUseCase
import ua.dolhanenko.repobrowser.domain.usecases.LoginUseCase
import ua.dolhanenko.repobrowser.domain.usecases.QueryUserInfoUseCase
import ua.dolhanenko.repobrowser.domain.usecases.SaveActiveUserUseCase
import ua.dolhanenko.repobrowser.presentation.base.BaseVM
import javax.inject.Inject

@HiltViewModel
class LoginVM @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val queryUserInfoUseCase: QueryUserInfoUseCase,
    private val saveActiveUserUseCase: SaveActiveUserUseCase,
    private val getActiveUserUseCase: GetActiveUserUseCase
) : BaseVM() {
    private val isLoggingIn: MutableLiveData<Boolean> = MutableLiveData(false)
    private val getAuthResult: MutableLiveData<Boolean?> = MutableLiveData()

    init {
        attemptLoginWthLastActiveUser()
    }

    fun getIsLoggingIn(): LiveData<Boolean> = isLoggingIn
    fun getAuthResult(): LiveData<Boolean?> = getAuthResult

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

    //Attempt to skip login by fetching an active user from DB and use their token to fetch user data
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
        if (RepoApp.activeToken != null)
            queryUserInfoUseCase(RepoApp.activeToken!!).let {
                if (it is Resource.Success) {
                    isSuccess = it.data != null
                    if (isSuccess) saveActiveUserUseCase(it.data!!)
                } else if (it is Resource.Error) {
                    processFailedQueryUser(it)
                }
            }
        isLoggingIn.postValue(false)
        getAuthResult.postValue(isSuccess)
    }

    private fun <T> processFailedQueryUser(resource: Resource.Error<T>) {
        (resource.exception as? BaseApiDataSource.NetworkException)?.let {
            if (it.code == BaseApiDataSource.CODE_UNAUTHORIZED) {
                //User token expired
                Log.i("LOGIN_VM", "user token expired or bad credentials")
            }
        }
    }

    private fun onLoginFail() {
        getAuthResult.postValue(false)
        isLoggingIn.postValue(false)
    }
}