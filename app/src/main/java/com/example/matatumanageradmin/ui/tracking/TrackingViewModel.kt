package com.example.matatumanageradmin.ui.tracking

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matatumanageradmin.data.Bus
import com.example.matatumanageradmin.data.BusLocation
import com.example.matatumanageradmin.data.MainRepository
import com.example.matatumanageradmin.utils.DispatcherProvider
import com.example.matatumanageradmin.utils.OperationStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackingViewModel @Inject constructor(val repository: MainRepository,
                                                             private val dispatcher: DispatcherProvider
) : ViewModel() {



    private var _busLocations = MutableLiveData<TrackingStatus>(TrackingStatus.Empty)
    val busLocations : LiveData<TrackingStatus>
            get() = _busLocations


    fun getBuses(adminId: String){
        viewModelScope.launch(dispatcher.io){
            _busLocations.postValue(TrackingStatus.Loading)
            when(val response = repository.getBuses(adminId)){
                is OperationStatus.Error -> _busLocations.postValue(TrackingStatus.Failed(response.message!!))
                is OperationStatus.Success -> {
                    if (response.data!!.isEmpty()){
                        _busLocations.postValue( TrackingStatus.Failed("No data was returned"))
                    }else{
                        constructBusLocation(response.data)
                       // _busList.postValue( FleetListViewModel.BusListStatus.Success("success", response.data))
                    }

                }
            }

        }

    }

    fun constructBusLocation(buses: List<Bus>){
        var busLocations = mutableListOf<BusLocation>()
        for (bus in buses){
            if (bus.status == "in service") {
                val location = Location("")
                location.latitude = bus.locationLat
                location.longitude = bus.locationLng
                val busLocation = BusLocation(bus.plate, null, location )
                busLocations.add(busLocation)
            }

        }
        if (busLocations.isEmpty()){
            _busLocations.postValue(TrackingStatus.Failed("No bus in service right now"))
        }
        else{
            _busLocations.postValue(TrackingStatus.Success("Success", busLocations))
        }


    }




    sealed class TrackingStatus{
        class Success(val resultText: String, val busLocations: List<BusLocation>): TrackingStatus()
        class Failed(val errorText: String): TrackingStatus()
        object Loading: TrackingStatus()
        object Empty: TrackingStatus()
    }


}