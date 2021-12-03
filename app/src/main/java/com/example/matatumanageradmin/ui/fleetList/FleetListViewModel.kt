package com.example.matatumanageradmin.ui.fleetList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matatumanageradmin.data.Bus
import com.example.matatumanageradmin.data.Driver
import com.example.matatumanageradmin.data.MainRepository
import com.example.matatumanageradmin.utils.DispatcherProvider
import com.example.matatumanageradmin.utils.OperationStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FleetListViewModel @Inject
constructor(private var repository: MainRepository,
            private val dispatcher: DispatcherProvider
): ViewModel() {

    var adminId = ""
    var queryText = ""

    private var _busOrDriverList =  MutableLiveData(false)
    val busOrDriverList : LiveData<Boolean>
            get() = _busOrDriverList

    private var _querying =  MutableLiveData(false)
    val querying : LiveData<Boolean>
        get() = _querying

    private var _busList = MutableLiveData<BusListStatus>(BusListStatus.Empty)
    val busList: LiveData<BusListStatus>
            get() = _busList

    private var _driverList = MutableLiveData<DriverListStatus>(DriverListStatus.Empty)
    val driverList : LiveData<DriverListStatus>
            get() = _driverList

    fun setListTypeToDriver(){
        _busOrDriverList.value = true

    }

    fun setListTypeToBus(){
        _busOrDriverList.value = false
    }

    fun setToQuery(){
        _querying.value = true
    }


    fun getAllData(admin: String){

        adminId = admin
        if(_busOrDriverList.value == true){
            getAllDrivers()
        }else{
            getAllBuses()
        }
    }

    fun getDataByQuery(admin: String, queryText: String){
        adminId = admin
        if(_busOrDriverList.value == true){
            getDriversByQuery()
        }else{
            getBusesByQuery()
        }
    }

    private fun getDriversByQuery(){
        viewModelScope.launch(dispatcher.io){
            _driverList.postValue(DriverListStatus.Loading)
            when(val response = repository.getDriverWithQuery(queryText, adminId)){
                is OperationStatus.Error -> DriverListStatus.Failed(response.message!!)
                is OperationStatus.Success -> {
                    if (response.data!!.isEmpty()){
                        _driverList.postValue( DriverListStatus.Failed("No data was returned"))
                    }else{
                        _driverList.postValue(DriverListStatus.Success("success", response.data))
                    }

                }
            }

        }
    }

    private fun getBusesByQuery(){
        viewModelScope.launch(dispatcher.io){
            _busList.postValue(BusListStatus.Loading)
            when(val response = repository.getBusesWithQuery(queryText,adminId)){
                is OperationStatus.Error -> BusListStatus.Failed(response.message!!)
                is OperationStatus.Success -> {
                    if (response.data!!.isEmpty()){
                        _busList.postValue(BusListStatus.Failed("No data was returned"))
                    }else{
                        _busList.postValue(BusListStatus.Success("success", response.data))
                    }

                }
            }

        }
    }


    private fun getAllBuses(){
        viewModelScope.launch(dispatcher.io){
            _busList.postValue(BusListStatus.Loading)
            when(val response = repository.getBuses(adminId)){
                is OperationStatus.Error ->  _busList.postValue(BusListStatus.Failed(response.message!!))
                is OperationStatus.Success -> {
                    if (response.data!!.isEmpty()){
                        _busList.postValue( BusListStatus.Failed("No data was returned"))
                    }else{
                        _busList.postValue( BusListStatus.Success("success", response.data))
                    }

                }
            }

        }
    }

    private fun getAllDrivers(){
        viewModelScope.launch(dispatcher.io){
            _driverList.postValue(DriverListStatus.Loading)
            when(val response = repository.getDrivers(adminId)){
                is OperationStatus.Error ->  _driverList.postValue(DriverListStatus.Failed(response.message!!))
                is OperationStatus.Success -> {
                    if (response.data!!.isEmpty()){
                        _driverList.postValue( DriverListStatus.Failed("No data was returned"))
                    }else{
                        _driverList.postValue(DriverListStatus.Success("success", response.data))
                    }

                }
            }

        }
    }


    sealed class BusListStatus{
        class Success(val resultText: String, val buses: List<Bus>): BusListStatus()
        class Failed(val errorText: String): BusListStatus()
        object Loading: BusListStatus()
        object Empty: BusListStatus()
    }

    sealed class DriverListStatus{
        class Success(val resultText: String, val drivers: List<Driver>): DriverListStatus()
        class Failed(val errorText: String): DriverListStatus()
        object Loading: DriverListStatus()
        object Empty: DriverListStatus()
    }


}