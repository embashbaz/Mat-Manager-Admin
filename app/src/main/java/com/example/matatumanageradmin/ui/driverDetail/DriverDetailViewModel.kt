package com.example.matatumanageradmin.ui.driverDetail

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matatumanageradmin.data.Bus
import com.example.matatumanageradmin.data.Driver
import com.example.matatumanageradmin.data.MainRepository
import com.example.matatumanageradmin.data.MatAdmin
import com.example.matatumanageradmin.ui.matAdminRegistration.MatManagerAdminRegistrationViewModel
import com.example.matatumanageradmin.utils.Constant
import com.example.matatumanageradmin.utils.DispatcherProvider
import com.example.matatumanageradmin.utils.OperationStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DriverDetailViewModel @Inject
constructor(private var repository: MainRepository,
            private val dispatcher: DispatcherProvider
): ViewModel() {

    private var _registrationDriverState = MutableLiveData<RegistrationDriverStatus>(
        RegistrationDriverStatus.Empty)
    val   registrationDriverState : LiveData<RegistrationDriverStatus>
        get() = _registrationDriverState


    private var _registerOrProfile = MutableLiveData(false)
    val   registerOrProfile: LiveData<Boolean>
        get() = _registerOrProfile

    private var _profileData = MutableLiveData<Driver>()
    val profileData: LiveData<Driver>
        get() = _profileData

    fun changeToProfileType(){
        _registerOrProfile.value = true
    }

    fun setDriverObject(driver: Driver){
        _profileData.value = driver
    }

    fun getUiData(adminId: String,
                    name: String,
                    email: String,
                    phoneNumber: String,
                    password: String,
                    confirmPassword: String,
                    address: String,
                    licenseNumber: String,
                    nationalId: String,
                    mBitmap: Bitmap?){
        if(!name.isNullOrEmpty() && !email.isNullOrEmpty() && !password.isNullOrEmpty()
            && !confirmPassword.isNullOrEmpty() && !address.isNullOrEmpty() &&
                !licenseNumber.isNullOrEmpty() && !nationalId.isNullOrEmpty()){

            if ( password == confirmPassword) {

                var floatPhone = phoneNumber.toLongOrNull()
                if (!phoneNumber.isNullOrEmpty() && floatPhone == null) {
                    _registrationDriverState.value =
                        RegistrationDriverStatus.Failed("Invalid phone number")
                    return

                } else if (phoneNumber.isNullOrEmpty()) {
                    floatPhone = 0L
                }

                val driver = Driver(
                    "", adminId, nationalId, licenseNumber, "this",
                    "this", name, email, floatPhone!!, address, "active", "this", "this"
                )

                if (mBitmap == null){
                    saveDriver(driver, password)
                }else{

                    savePicture(mBitmap, driver, password)
                }




            }else{
                _registrationDriverState.value = RegistrationDriverStatus.Failed("Both password have to match")
            }

        }else{
            _registrationDriverState.value = RegistrationDriverStatus.Failed("Invalid phone number")
        }



    }

    fun savePicture(bitmap: Bitmap, driver: Driver, password: String){
        viewModelScope.launch(dispatcher.io) {
            _registrationDriverState.postValue(RegistrationDriverStatus.Loading)

            when (val result = repository.addSaveImage(bitmap, driver.managerId, Constant.DRIVER_IMAGE, driver.cardId)) {
                is OperationStatus.Error -> _registrationDriverState.postValue(
                    RegistrationDriverStatus.Failed(result.message!!)
                )
                is OperationStatus.Success -> {
                    driver.pictureLink = result.data!!
                    saveDriver(driver, password)
                }

            }
        }
    }

    fun saveDriver(driver: Driver, password: String) {
        viewModelScope.launch(dispatcher.io) {
            _registrationDriverState.postValue(RegistrationDriverStatus.Loading)

            when (val result = repository.registerDriver(driver, password)) {
                is OperationStatus.Error -> _registrationDriverState.postValue(
                    RegistrationDriverStatus.Failed(result.message!!)
                )
                is OperationStatus.Success -> _registrationDriverState.postValue(
                    RegistrationDriverStatus.Success("Registration Successful ")
                )

            }
        }

    }




    sealed class RegistrationDriverStatus{
        class Success(val resultText: String): RegistrationDriverStatus()
        class Failed(val errorText: String): RegistrationDriverStatus()
        object Loading: RegistrationDriverStatus()
        object Empty: RegistrationDriverStatus()
    }

}