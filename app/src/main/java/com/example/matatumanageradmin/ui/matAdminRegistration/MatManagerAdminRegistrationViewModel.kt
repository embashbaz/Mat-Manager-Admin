package com.example.matatumanageradmin.ui.matAdminRegistration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matatumanageradmin.data.MainRepository
import com.example.matatumanageradmin.data.MatAdmin
import com.example.matatumanageradmin.utils.DispatcherProvider
import com.example.matatumanageradmin.utils.OperationStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.round

@HiltViewModel
class MatManagerAdminRegistrationViewModel @Inject
    constructor(private var repository: MainRepository,
                private val dispatcher: DispatcherProvider
): ViewModel() {

  private var _registrationstate = MutableLiveData<RegistrationStatus>(RegistrationStatus.Empty)
  val   registrationstate : LiveData<RegistrationStatus>
            get() = _registrationstate

  private var _registerOrProfile = MutableLiveData(false)
  val   registerOrProfile: LiveData<Boolean>
                get() = _registerOrProfile

  private var _profileData = MutableLiveData<MatAdmin>()



    fun changeToProfileType(){
        _registerOrProfile.value = true
    }

    fun getUiData(
             name: String,
             email: String,
             phoneNumber: String,
             password: String,
             confirmPassword: String,
             city: String,
             completeAdress: String,
             license: String

        ){
            if(!name.isNullOrEmpty() && !email.isNullOrEmpty() && !password.isNullOrEmpty()
                && !confirmPassword.isNullOrEmpty() && !city.isNullOrEmpty() && !license.isNullOrEmpty()){
                if ( password == confirmPassword){
                   val floatPhone = phoneNumber.toLongOrNull()
                    if (floatPhone == null){
                        _registrationstate.value = RegistrationStatus.Failed("Invalid phone number")
                        return

                    }
                        val admin = MatAdmin(
                            "", name, email,
                            floatPhone!!, completeAdress, "", license, "", "Pending",
                            "", ""
                        )

                    viewModelScope.launch(dispatcher.io) {
                      _registrationstate.postValue(RegistrationStatus.Loading)
                       when(val result =  repository.registerAdmin(admin, password)){
                           is OperationStatus.Error -> _registrationstate.postValue(RegistrationStatus.Failed(result.message!!))
                           is OperationStatus.Success -> _registrationstate.postValue(RegistrationStatus.Success("Registration Successful "))

                       }



                    }

                }else{
                    _registrationstate.value = RegistrationStatus.Failed("Passwords do not match")
                    return
                }
            }else {
                _registrationstate.value = RegistrationStatus.Failed("Some fields are are empty")
                return
            }

        }


    sealed class RegistrationStatus{
        class Success(val resultText: String): RegistrationStatus()
        class Failed(val errorText: String): RegistrationStatus()
        object Loading: RegistrationStatus()
        object Empty: RegistrationStatus()
    }
}