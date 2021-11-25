package com.example.matatumanageradmin.ui.trip

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matatumanageradmin.R
import com.example.matatumanageradmin.databinding.FragmentTripBinding
import com.example.matatumanageradmin.ui.expense.ExpenseListViewModel
import com.example.matatumanageradmin.ui.other.DefaultRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TripFragment : Fragment() {

    lateinit var tripBinding: FragmentTripBinding
    private val tripListViewModel : TripListViewModel by viewModels()

    private lateinit var  defaultRecyclerAdapter: DefaultRecyclerAdapter

    private var type = ""
    private var recordId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tripBinding = FragmentTripBinding.inflate(inflater,container, false )
        val view = tripBinding.root
        defaultRecyclerAdapter = DefaultRecyclerAdapter { trip -> onTripClicked(trip) }
        type = arguments?.getString("trip_type")!!
        recordId = arguments?.getString("record_id")!!

        getTrips()

        return view
    }

    private fun getTrips() {
        tripListViewModel.getTrips(recordId, type)
        tripListViewModel.tripList.observe(viewLifecycleOwner, {
            when(it){
                is TripListViewModel.TripListStatus.Success -> {
                    if(!it.trips.isEmpty()) {
                        defaultRecyclerAdapter.setData(it.trips as ArrayList<Any>)
                        defaultRecyclerAdapter.notifyDataSetChanged()
                        setRecyclerView()
                        hideNoDataText()
                        hideProgressBar()
                    }else{
                        showNoDataText()
                    }
                }

                is TripListViewModel.TripListStatus.Failed -> {
                    showNoDataText()
                    hideProgressBar()
                }

                is TripListViewModel.TripListStatus.Loading -> {
                    showProgressBar()
                    hideNoDataText()
                }


            }
        })
    }

    private fun onTripClicked(trip: Any) {



    }

    fun setRecyclerView(){
        tripBinding.tripListRecycler.layoutManager = LinearLayoutManager(activity)
        tripBinding.tripListRecycler.adapter = defaultRecyclerAdapter
    }

    private fun hideNoDataText(){
        tripBinding.noDataTripList.visibility = View.INVISIBLE
    }

    private fun hideProgressBar(){
        tripBinding.tripListProgress.visibility = View.INVISIBLE
    }

    private fun showNoDataText(){
        tripBinding.noDataTripList.visibility = View.VISIBLE
    }

    private fun showProgressBar(){
        tripBinding.tripListProgress.visibility = View.VISIBLE
    }


}