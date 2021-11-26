package com.example.matatumanageradmin.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matatumanageradmin.data.Driver
import com.example.matatumanageradmin.data.MainRepository
import com.example.matatumanageradmin.data.MatAdmin
import com.example.matatumanageradmin.utils.DispatcherProvider
import com.example.matatumanageradmin.utils.OperationStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject
            constructor(private var repository: MainRepository,
                        private val dispatcher: DispatcherProvider
            ): ViewModel() {


    private var _loginStatus = MutableLiveData<LoginStatus>(LoginStatus.Empty)
    val loginStatus: LiveData<LoginStatus>
            get() = _loginStatus

    private var _registerButtonClicked = MutableLiveData(false)
    val registerButtonClicked: LiveData<Boolean>
            get() = _registerButtonClicked

    fun setToRegsterButtonClicked(boolean: Boolean){
        _registerButtonClicked.value = boolean
    }

    fun loginMethod(email: String, password: String){
        if (email.isNotEmpty() && password.isNotEmpty())
            viewModelScope.launch(dispatcher.io) {
                _loginStatus.postValue(LoginStatus.Loading)
                when (val response = repository.loginAdmin(email, password)){
                    is OperationStatus.Error -> _loginStatus.postValue(LoginStatus.Failed(response.message!!))
                    is OperationStatus.Success -> {

                        if(response.data!!.status == "active") {
                            _loginStatus.postValue(LoginStatus.Success("Success", response.data))
                        }
                        else if (response.data!!.status == "Pending"){
                            _loginStatus.value = LoginStatus.Failed("Your account has not been verified yet, please contact us at emerybashige@gmail.com to follow up on the verification process")
                        }
                    }
                }

            }else{
            _loginStatus.value = LoginStatus.Failed("Please give both the password and email")
        }

    }

    sealed class LoginStatus{
        class Success(val resultText: String, val adminObject: MatAdmin?): LoginStatus()
        class Failed(val errorText: String): LoginStatus()
        object Loading: LoginStatus()
        object Empty: LoginStatus()
    }

}