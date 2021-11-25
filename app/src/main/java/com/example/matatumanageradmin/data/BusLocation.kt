package com.example.matatumanageradmin.data

import android.location.Location
import com.google.android.gms.maps.model.Marker

data class BusLocation (
    var busPlate: String = "",
    var marker: Marker? = null,
    var location: Location? = null
)