package com.example.matatumanageradmin.ui.tracking

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matatumanageradmin.data.*
import com.example.matatumanageradmin.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackingViewModel @Inject constructor(
    val repository: MainRepository,
    private val dispatcher: DispatcherProvider
) : ViewModel() {


    var allStatistics = mutableListOf<Statistics>()
    var selectedDriver: Driver? = null
    var selectedBus: Bus? = null
    var selectedStat: Statistics? = null

    private var _busLocations = MutableLiveData<TrackingStatus>(TrackingStatus.Empty)
    val busLocations: LiveData<TrackingStatus>
        get() = _busLocations

    private var _trackingObject = MutableLiveData<TrackingObjectStatus>(TrackingObjectStatus.Empty)
    val trackingObject: LiveData<TrackingObjectStatus>
        get() = _trackingObject


    fun getStat(adminId: String) {
        viewModelScope.launch(dispatcher.io) {
            _busLocations.postValue(TrackingStatus.Loading)
            when (val response = repository.getStats(
                Constant.ALL_STAT,
                adminId,
                getFirstDayOfTheMonth(),
                getEndOfToday()
            )) {
                is OperationStatus.Error -> _busLocations.postValue(TrackingStatus.Failed(response.message!!))
                is OperationStatus.Success -> {
                    if (response.data!!.isEmpty()) {
                        _busLocations.postValue(TrackingStatus.Failed("No data was returned"))
                    } else {
                        constructBusLocation(response.data)
                        allStatistics = response.data as MutableList<Statistics>
                        // _busList.postValue( FleetListViewModel.BusListStatus.Success("success", response.data))
                    }

                }
            }

        }

    }

    fun constructBusLocation(stats: List<Statistics>) {
        var busLocations = mutableListOf<BusLocation>()
        for (stat in stats) {
            if (stat.comment == "active") {
                val location = Location("")
                location.latitude = stat.locationLat
                location.longitude = stat.locationLng
                val busLocation = BusLocation(stat.busPlate, null, location)
                busLocations.add(busLocation)
            }

        }
        if (busLocations.isEmpty()) {
            _busLocations.postValue(TrackingStatus.Failed("No bus in service right now"))
        } else {
            _busLocations.postValue(TrackingStatus.Success("Success", busLocations))
        }


    }


    fun busTrackerClicked(plate: String) {
        selectedStat = null
        var driverId = ""
        for (item in allStatistics) {
            if (item.busPlate == plate) {
                selectedStat = item
                driverId = item.driverId
            }

        }
        _trackingObject.postValue(
            TrackingObjectStatus.Success(
                "",
                selectedBus,
                selectedDriver,
                selectedStat
            )
        )
        getBusObject(plate)
        getDriverObject(driverId)

    }

    private fun getDriverObject(driverId: String) {
        selectedBus = null
        viewModelScope.launch(dispatcher.io) {
            when (val response = repository.getDriver(driverId)) {
                is OperationStatus.Success -> {

                    selectedDriver = response.data
                    _trackingObject.postValue(
                        TrackingObjectStatus.Success(
                            "",
                            selectedBus,
                            selectedDriver,
                            selectedStat
                        )
                    )
                }

                is OperationStatus.Error -> {
                    _trackingObject.postValue(
                        TrackingObjectStatus.Success(
                            "Driver could not be fetched",
                            selectedBus,
                            selectedDriver,
                            selectedStat
                        )
                    )

                }


            }


        }

    }

    private fun getBusObject(plate: String) {
        selectedBus = null
        viewModelScope.launch(dispatcher.io) {
            when (val response = repository.getBus(plate)) {
                is OperationStatus.Success -> {
                    selectedBus = response.data
                    _trackingObject.postValue(
                        TrackingObjectStatus.Success(
                            "",
                            selectedBus,
                            selectedDriver,
                            selectedStat
                        )
                    )

                }

                is OperationStatus.Error -> {
                    _trackingObject.postValue(
                        TrackingObjectStatus.Success(
                            "Bus data could not be fetched",
                            selectedBus,
                            selectedDriver,
                            selectedStat
                        )
                    )

                }

            }

        }
    }


    sealed class TrackingStatus {
        class Success(val resultText: String, val busLocations: List<BusLocation>) :
            TrackingStatus()

        class Failed(val errorText: String) : TrackingStatus()
        object Loading : TrackingStatus()
        object Empty : TrackingStatus()
    }

    sealed class TrackingObjectStatus {
        class Success(
            val resultText: String,
            var bus: Bus?,
            var driver: Driver?,
            var stat: Statistics?
        ) : TrackingObjectStatus()

        class Failed(val errorText: String) : TrackingObjectStatus()
        object Loading : TrackingObjectStatus()
        object Empty : TrackingObjectStatus()
    }


}