package com.example.matatumanageradmin.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matatumanageradmin.data.MainRepository
import com.example.matatumanageradmin.utils.DispatcherProvider
import com.example.matatumanageradmin.utils.OperationStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel  @Inject
constructor(private var repository: MainRepository,
            private val dispatcher: DispatcherProvider
): ViewModel() {

    private var _fleetCardClicked = MutableLiveData(false)
    val fleetCardClicked: LiveData<Boolean>
            get() = _fleetCardClicked

    private var _profileCardClicked = MutableLiveData(false)
    val profileCardClicked: LiveData<Boolean>
        get() = _profileCardClicked

    private var _issuesCardClicked = MutableLiveData(false)
    val issuesCardClicked: LiveData<Boolean>
        get() = _issuesCardClicked

    private var _trackingCardClicked = MutableLiveData(false)
    val trackingCardClicked: LiveData<Boolean>
        get() = _trackingCardClicked

    private var _logOutStatus = MutableLiveData<LogOutStatus>(LogOutStatus.Empty)
    val logOutStatus : LiveData<LogOutStatus>
        get() = _logOutStatus

    fun fleetCardClicked(action: Boolean){
        _fleetCardClicked.value = action
    }

    fun issueCardClicked(action: Boolean){
        _issuesCardClicked.value = action
    }

    fun trackingCardClicked(action: Boolean){
        _trackingCardClicked.value = action
    }

    fun profileCardClicked(action: Boolean){
        _profileCardClicked.value = action
    }

    fun setLogoutStatusToEmpty(){
        _logOutStatus.value = LogOutStatus.Empty
    }

    fun logOut(){
        viewModelScope.launch(dispatcher.io){
            _logOutStatus.postValue(LogOutStatus.Loading)
            when(val response = repository.logOut()){
                is OperationStatus.Success -> _logOutStatus.postValue(LogOutStatus.Success("Signed out"))
                is OperationStatus.Error -> _logOutStatus.postValue(LogOutStatus.Failed(response.message!!))
            }


        }


    }

    sealed class LogOutStatus{
        class Success(val resultText: String): LogOutStatus()
        class Failed(val errorText: String): LogOutStatus()
        object Loading: LogOutStatus()
        object Empty: LogOutStatus()
    }


}