package com.example.matatumanageradmin.ui.tracking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.matatumanageradmin.MatManagerAdminApp
import com.example.matatumanageradmin.R
import com.example.matatumanageradmin.data.BusLocation
import com.example.matatumanageradmin.databinding.FragmentTrackingBinding
import com.example.matatumanageradmin.utils.showLongToast
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrackingFragment : Fragment() {

    lateinit var mapView: MapView
    private var map: GoogleMap? = null


    private lateinit var trackingBinding: FragmentTrackingBinding
    private val trackingViewModel: TrackingViewModel by viewModels()
    private val adminId by lazy { (activity?.application as MatManagerAdminApp).matAdmin!!.matAdminId }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        trackingBinding = FragmentTrackingBinding.inflate(inflater, container, false)
        val view = trackingBinding.root
        mapView = trackingBinding.projectMapView
        trackingViewModel.getBuses(adminId)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync {
            map = it
            subscriberToObserver()

        }


    }

    private fun subscriberToObserver() {
        trackingViewModel.busLocations.observe(viewLifecycleOwner, {
            when(it){
                is TrackingViewModel.TrackingStatus.Failed -> {
                    showLongToast(it.errorText)
                }
                is TrackingViewModel.TrackingStatus.Success -> {
                    showItemsOnTheMap(it.busLocations)
                }

            }


        })
    }

    private fun showItemsOnTheMap(busLocations: List<BusLocation>) {
        for (busLocation in busLocations){
            val location = busLocation.location!!
            val loc = LatLng(location.latitude, location.longitude)
            var locMarker = busLocation.marker

            if (locMarker == null){
                val markerOptions = MarkerOptions()
                markerOptions.position(loc)
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.final_bus))
                markerOptions.rotation(location.bearing)
                locMarker = map?.addMarker(markerOptions)
            }else{
                locMarker!!.position = loc
                locMarker!!.rotation = location.bearing
            }


        }
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }


}