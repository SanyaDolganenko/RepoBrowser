package ua.dolhanenko.repobrowser.presentation.host

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.dolhanenko.repobrowser.domain.usecases.LogoutUseCase
import ua.dolhanenko.repobrowser.presentation.base.BaseVM
import ua.dolhanenko.repobrowser.presentation.utils.SingleLiveEvent
import ua.dolhanenko.repobrowser.presentation.utils.runOnUiThread
import javax.inject.Inject

@HiltViewModel
internal class HostVM @Inject constructor(private val logoutUseCase: LogoutUseCase) : BaseVM() {
    private val isLogoutVisible: MutableLiveData<Boolean> = MutableLiveData(false)
    private val successfulLogoutEvent: SingleLiveEvent<Void> = SingleLiveEvent()

    fun getIsLogoutVisible(): LiveData<Boolean> = isLogoutVisible
    fun getSuccessfulLogoutEvent(): LiveData<Void> = successfulLogoutEvent

    fun onSuccessfulLogin() {
        isLogoutVisible.postValue(true)
    }

    fun onLogoutRequest() {
        viewModelScope.launch(Dispatchers.IO) {
            if (logoutUseCase()) {
                runOnUiThread {
                    successfulLogoutEvent.call()
                    isLogoutVisible.value = false
                }
            }
        }
    }
}