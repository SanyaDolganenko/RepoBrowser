package ua.dolhanenko.repobrowser.view.host

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.dolhanenko.repobrowser.domain.usecases.LogoutUseCase
import ua.dolhanenko.repobrowser.utils.SingleLiveEvent
import ua.dolhanenko.repobrowser.utils.runOnUiThread


class HostVM(private val logoutUseCase: LogoutUseCase) : ViewModel() {
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