package ua.dolhanenko.repobrowser.presentation.login

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.dolhanenko.repobrowser.core.Constants
import ua.dolhanenko.repobrowser.core.NetworkException
import ua.dolhanenko.repobrowser.core.Resource
import ua.dolhanenko.repobrowser.domain.usecases.*
import ua.dolhanenko.repobrowser.presentation.base.BaseVM
import javax.inject.Inject

@HiltViewModel
internal class LoginVM @Inject constructor(
    private val queryUserInfoUseCase: QueryUserInfoUseCase,
    private val saveActiveUserUseCase: SaveActiveUserUseCase,
    private val getActiveUserUseCase: GetActiveUserUseCase,
    private val getActiveTokenUseCase: GetActiveTokenUseCase,
    private val saveActiveTokenUseCase: SaveActiveTokenUseCase,
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
        viewModelScope.launch(Dispatchers.Main) {
            val provider = OAuthProvider.newBuilder("github.com")
                .addCustomParameter("login", userName)
                .setScopes(listOf("user:email"))
                .build()
            FirebaseAuth.getInstance()
                .startActivityForSignInWithProvider(activity, provider)
                .addOnCompleteListener { result ->
                    when {
                        result.isSuccessful -> onLoginSuccess(result.result)
                        result.isCanceled -> onLoginCancel()
                        else -> onLoginFail()
                    }
                }
        }
    }

    //Attempt to skip login by fetching an active user from DB and use their token to fetch user data
    private fun attemptLoginWthLastActiveUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val activeUser = getActiveUserUseCase()
            activeUser?.lastUsedToken?.let {
                saveActiveTokenUseCase(it)
                queryLoggedInUserInfo()
            }
        }
    }

    private fun onLoginCancel() {
        isLoggingIn.postValue(false)
    }

    private fun onLoginSuccess(authResult: AuthResult) {
        saveActiveTokenUseCase((authResult.credential as OAuthCredential).accessToken)
        isLoggingIn.postValue(false)
        viewModelScope.launch(Dispatchers.IO) {
            queryLoggedInUserInfo()
        }
    }

    private suspend fun queryLoggedInUserInfo() {
        isLoggingIn.postValue(true)
        var isSuccess = false
        val activeToken = getActiveTokenUseCase()
        if (activeToken != null)
            queryUserInfoUseCase(activeToken).let {
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
        (resource.exception as? NetworkException)?.let {
            if (it.code == Constants.CODE_UNAUTHORIZED) {
                //User token expired
                logger.i("LOGIN_VM", "user token expired or bad credentials")
            }
        }
    }

    private fun onLoginFail() {
        getAuthResult.postValue(false)
        isLoggingIn.postValue(false)
    }
}