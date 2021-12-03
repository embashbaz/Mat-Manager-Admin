package com.example.matatumanageradmin.ui.busDetail

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matatumanageradmin.data.Bus
import com.example.matatumanageradmin.data.MainRepository
import com.example.matatumanageradmin.ui.matAdminRegistration.MatManagerAdminRegistrationViewModel
import com.example.matatumanageradmin.utils.Constant
import com.example.matatumanageradmin.utils.DispatcherProvider
import com.example.matatumanageradmin.utils.OperationStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BusDetailViewModel @Inject constructor(
    val repository: MainRepository,
    private val dispatcher: DispatcherProvider
) : ViewModel() {

    private var _createOrUpdateBus = MutableLiveData(false)
    val createOrUpdateBus: LiveData<Boolean>
        get() = _createOrUpdateBus

    private var _busObject = MutableLiveData<Bus>()
    val busObject: LiveData<Bus>
        get() = _busObject

    private var _createOrUpdateBusResult =
        MutableLiveData<CreateOrUpdateBusStatus>(CreateOrUpdateBusStatus.Empty)
    val createOrUpdateBusResult: LiveData<CreateOrUpdateBusStatus>
        get() = _createOrUpdateBusResult


    fun changeToUpdate() {
        _createOrUpdateBus.value = true
    }

    fun setBusObject(bus: Bus) {
        _busObject.value = bus
    }

    fun getDataFromView(
        adminId: String,
        plate: String,
        identifier: String,
        model: String,
        comment: String,
        bitmap: Bitmap?
    ) {


        if (_createOrUpdateBus.value == true) {
            _busObject.value!!.plate = plate
            _busObject.value!!.identifier = identifier
            _busObject.value!!.carModel = model
            _busObject.value!!.comment = comment
            _busObject.value!!.managerId = adminId

            if (bitmap == null) {
                updateBus()
            } else {
                saveBusPicture(_busObject.value!!, bitmap)
            }


        } else {
            val bus = Bus(
                plate, adminId, identifier, model, "", "", "", "", 0.0,
                0.0, "active", comment, ""
            )

            if (bitmap == null) {
                saveBusToDb(bus)
            } else {

                viewModelScope.launch(dispatcher.io) {
                    saveBusPicture(bus, bitmap)
                }
            }


        }

    }

    fun saveBusToDb(bus: Bus) {
        viewModelScope.launch(dispatcher.io) {
            _createOrUpdateBusResult.postValue(CreateOrUpdateBusStatus.Loading)
            when (val result = repository.addMatatu(bus)) {
                is OperationStatus.Error -> _createOrUpdateBusResult.postValue(
                    CreateOrUpdateBusStatus.Failed(result.message!!)
                )
                is OperationStatus.Success -> _createOrUpdateBusResult.postValue(
                    CreateOrUpdateBusStatus.Success("Bus updated")
                )

            }

        }
    }

    fun updateBus() {
        viewModelScope.launch(dispatcher.io) {
            _createOrUpdateBusResult.postValue(CreateOrUpdateBusStatus.Loading)
            when (val result = repository.updateBus(_busObject.value!!)) {
                is OperationStatus.Error -> _createOrUpdateBusResult.postValue(
                    CreateOrUpdateBusStatus.Failed(result.message!!)
                )
                is OperationStatus.Success -> _createOrUpdateBusResult.postValue(
                    CreateOrUpdateBusStatus.Success("Bus added")
                )

            }

        }

    }

    fun saveBusPicture(bus: Bus, bitmap: Bitmap) {
        viewModelScope.launch(dispatcher.io) {

            _createOrUpdateBusResult.postValue(CreateOrUpdateBusStatus.Loading)
            when (val result =
                repository.addSaveImage(bitmap, bus.managerId, Constant.BUS_IMAGE, bus.plate)) {
                is OperationStatus.Error -> _createOrUpdateBusResult.postValue(
                    CreateOrUpdateBusStatus.Failed(result.message!!)
                )
                is OperationStatus.Success -> {
                    if (_createOrUpdateBus.value == false) {
                        bus.picture = result.data!!
                        saveBusToDb(bus)
                    } else {
                        _busObject.value!!.picture = result.data!!
                        updateBus()
                    }
                }

            }
        }

    }


    sealed class CreateOrUpdateBusStatus {
        class Success(val resultText: String) : CreateOrUpdateBusStatus()
        class Failed(val errorText: String) : CreateOrUpdateBusStatus()
        object Loading : CreateOrUpdateBusStatus()
        object Empty : CreateOrUpdateBusStatus()
    }

}