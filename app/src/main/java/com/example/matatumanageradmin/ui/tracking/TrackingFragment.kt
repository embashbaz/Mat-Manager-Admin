package com.example.matatumanageradmin.ui.tracking

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.matatumanageradmin.MatManagerAdminApp
import com.example.matatumanageradmin.R
import com.example.matatumanageradmin.data.BusLocation
import com.example.matatumanageradmin.data.Statistics
import com.example.matatumanageradmin.databinding.FragmentTrackingBinding
import com.example.matatumanageradmin.utils.showLongToast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrackingFragment : Fragment(), GoogleMap.OnMarkerClickListener {

    lateinit var mapView: MapView
    private var map: GoogleMap? = null
    private lateinit var bottomSheet: BottomSheetBehavior<LinearLayout>
    lateinit var mainHandler: Handler


    private lateinit var trackingBinding: FragmentTrackingBinding
    private val trackingViewModel: TrackingViewModel by viewModels()
    private val adminId by lazy { (activity?.application as MatManagerAdminApp).matAdmin!!.matAdminId }
    private var allLocation = mutableListOf<BusLocation>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        trackingBinding = FragmentTrackingBinding.inflate(inflater, container, false)
        val view = trackingBinding.root
        mapView = trackingBinding.projectMapView


        bottomSheet = BottomSheetBehavior.from(trackingBinding.tackingDetail)
        subscribeToTrackingObjectClicked()
        mainHandler = Handler(Looper.getMainLooper())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync {
            map = it
            subscriberToObserver()
            map!!.setOnMarkerClickListener(this)

        }


        bottomSheet.apply {
            peekHeight = 50
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }


    }

    fun getLatestLocation() =
       object : Runnable {
            override fun run() {
                trackingViewModel.getStat(adminId)
                mainHandler.postDelayed(this, 20000)
            }
        }


    private fun subscriberToObserver() {
        trackingViewModel.busLocations.observe(viewLifecycleOwner, {
            when (it) {
                is TrackingViewModel.TrackingStatus.Failed -> {
                    showLongToast(it.errorText)
                }
                is TrackingViewModel.TrackingStatus.Success -> {
                    showItemsOnTheMap(it.busLocations)
                    allLocation = it.busLocations as MutableList<BusLocation>
                }

            }


        })
    }

    private fun showItemsOnTheMap(busLocations: List<BusLocation>) {
        map!!.clear()
        for (busLocation in busLocations) {
            val location = busLocation.location!!
            val loc = LatLng(location.latitude, location.longitude)
            var locMarker = busLocation.marker

            if (locMarker == null) {
                val markerOptions = MarkerOptions()
                markerOptions.position(loc)
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.final_bus))
                markerOptions.rotation(location.bearing)
                markerOptions.anchor(0.5f, 0.5f)
                locMarker = map?.addMarker(markerOptions)

            } else {
                locMarker!!.position = loc
                locMarker!!.rotation = location.bearing
            }

            map?.animateCamera(

                //ToDo: this will create a bug later on
                CameraUpdateFactory.newLatLngZoom(
                    loc,
                    15f
                )
            )


        }
    }

    private fun subscribeToTrackingObjectClicked() {
        trackingViewModel.trackingObject.observe(viewLifecycleOwner, {
            when (it) {
                is TrackingViewModel.TrackingObjectStatus.Success -> {
                    bottomSheet.apply {
                        this.state = BottomSheetBehavior.STATE_EXPANDED
                        populateBottomSheetViews(it)
                    }


                }

            }

        })


    }

    private fun populateBottomSheetViews(it: TrackingViewModel.TrackingObjectStatus.Success) {
        if (it.bus != null) {
            trackingBinding.busTrackTxt.setText(" ${it.bus!!.plate}")
            trackingBinding.busSecondaryTxtTrack.setText(" ${it.bus!!.carModel}")
            if (it.bus!!.picture.isNotEmpty())
                Glide.with(requireView()).load(it.bus!!.picture).apply(RequestOptions.circleCropTransform()).into(trackingBinding.busTrackImage)


        }

        if (it.stat != null) {
            showStatInfo(it.stat!!)
        }

        if (it.driver != null) {
            trackingBinding.driverTrackTxt.setText(
                " ${it.driver!!.name} ")

            trackingBinding.driverSecondayTextTrack.setText(" ${it.driver!!.email} \n " +
                    "${it.driver!!.phoneNumber}")

                if (it.driver!!.pictureLink.isNotEmpty())
                    Glide.with(requireView()).load(it.driver!!.pictureLink).apply(RequestOptions.circleCropTransform()).into(trackingBinding.driverTrackImage)



        }

        if (it.resultText.isNotEmpty()) {
            showLongToast(it.resultText)
        }


    }

    private fun showStatInfo(stat: Statistics) {
        trackingBinding.statDataTrack.setText("Time started: ${stat.dayId} \n" +
                "Distance covered today ${stat.distance} \n" +
                "Money collected ${stat.amount} \n" +
                "Expense so far today ${stat.expense} \n" +
                "Number of trip today ${stat.maxSpeed}")
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        val position = p0.position
        for (pos in allLocation) {
            if (pos.location!!.latitude == position.latitude && pos.location!!.longitude == position.longitude) {
                trackingViewModel.busTrackerClicked(pos.busPlate)
            }
        }
        return true
    }


    override fun onResume() {
        super.onResume()
        mainHandler.post(getLatestLocation())
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
        mainHandler.removeCallbacks(getLatestLocation())
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