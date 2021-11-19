package com.example.matatumanageradmin.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.matatumanageradmin.data.MainRepository
import com.example.matatumanageradmin.utils.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
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


}