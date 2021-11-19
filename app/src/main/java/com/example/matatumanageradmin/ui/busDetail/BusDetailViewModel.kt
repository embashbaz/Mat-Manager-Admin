package com.example.matatumanageradmin.ui.busDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matatumanageradmin.data.Bus
import com.example.matatumanageradmin.data.MainRepository
import com.example.matatumanageradmin.ui.matAdminRegistration.MatManagerAdminRegistrationViewModel
import com.example.matatumanageradmin.utils.DispatcherProvider
import com.example.matatumanageradmin.utils.OperationStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BusDetailViewModel  @Inject constructor(val repository: MainRepository,
                                              private val dispatcher: DispatcherProvider
) : ViewModel(){

    private var _createOrUpdateBus = MutableLiveData(false)
    val createOrUpdateBus : LiveData<Boolean>
            get() = _createOrUpdateBus

    private var _busObject = MutableLiveData<Bus>()
    val busObject : LiveData<Bus>
            get() = _busObject

    private var _createOrUpdateBusResult = MutableLiveData<CreateOrUpdateBusStatus>(CreateOrUpdateBusStatus.Empty)
    val createOrUpdateBusResult : LiveData<CreateOrUpdateBusStatus>
        get() = _createOrUpdateBusResult



    fun changeToUpdate(){
        _createOrUpdateBus.value = true
    }

    fun setBusObject(bus: Bus){
        _busObject.value = bus
    }

    fun getDataFromView(
        plate: String,
        identifier: String,
        model: String,
        comment: String
    ){


        if(_createOrUpdateBus.value == true){
            _busObject.value!!.plate = plate
            _busObject.value!!.identifier = identifier
            _busObject.value!!.carModel =model
            _busObject.value!!.comment = comment
            _busObject.value!!.managerId ="8IPFL9JZ5gQFmOPZXHMoY8mu04g2"
            val bus = Bus(plate, "8IPFL9JZ5gQFmOPZXHMoY8mu04g2", identifier, model,"","", "",
                        "", 0.0, 0.0, "new", comment,"")

            viewModelScope.launch(dispatcher.io) {
                _createOrUpdateBusResult.postValue(CreateOrUpdateBusStatus.Loading)
                when (val result = repository.updateBus(bus)){
                    is OperationStatus.Error -> _createOrUpdateBusResult.postValue(
                        CreateOrUpdateBusStatus.Failed(result.message!!))
                    is OperationStatus.Success -> _createOrUpdateBusResult.postValue(
                        CreateOrUpdateBusStatus.Success("Bus added"))

                }

            }

        }else{
            val bus = Bus(plate, "8IPFL9JZ5gQFmOPZXHMoY8mu04g2", identifier, model, "", "", "","",0.0,
                0.0, "active", comment, "")

            viewModelScope.launch(dispatcher.io) {
                _createOrUpdateBusResult.postValue(CreateOrUpdateBusStatus.Loading)
                when (val result = repository.addMatatu(bus)){
                    is OperationStatus.Error -> _createOrUpdateBusResult.postValue(
                        CreateOrUpdateBusStatus.Failed(result.message!!))
                    is OperationStatus.Success -> _createOrUpdateBusResult.postValue(
                        CreateOrUpdateBusStatus.Success("Bus updated"))

                }

            }

        }



    }




    sealed class CreateOrUpdateBusStatus{
        class Success(val resultText: String): CreateOrUpdateBusStatus()
        class Failed(val errorText: String): CreateOrUpdateBusStatus()
        object Loading: CreateOrUpdateBusStatus()
        object Empty: CreateOrUpdateBusStatus()
    }

}