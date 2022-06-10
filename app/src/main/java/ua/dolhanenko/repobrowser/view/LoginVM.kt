package ua.dolhanenko.repobrowser.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class LoginVM : ViewModel() {
    val result: MutableLiveData<String?> = MutableLiveData()

    fun onLoginClick(login: String, password: String) {
        result.postValue("Good")
    }
}