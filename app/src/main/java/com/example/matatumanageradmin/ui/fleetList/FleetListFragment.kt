package com.example.matatumanageradmin.ui.fleetList

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.matatumanageradmin.R
import com.example.matatumanageradmin.databinding.FragmentFleetListBinding
import kotlinx.android.synthetic.main.fragment_fleet_list.*


class FleetListFragment : Fragment() {



    private lateinit var fleetListBinding: FragmentFleetListBinding
    private val fleetListViewModel: FleetListViewModel by viewModels()
    private var adminId = ""
    

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fleetListBinding = FragmentFleetListBinding.inflate(inflater, container, false)
        val view = fleetListBinding.root

        fleetListViewModel.setListTypeToBus()
        fleetListViewModel.getAllData(adminId)
        listenToTextChangeSearchEt()

        bottomNavigationView.setOnClickListener {
            when(it.id){
                R.id.bottom_nav_bus -> {
                    fleetListViewModel.setListTypeToBus()
                    fleetListViewModel.getAllData(adminId)
                }
                R.id.bottom_nav_driver -> {
                    fleetListViewModel.setListTypeToDriver()
                    fleetListViewModel.getAllData(adminId)
                }

            }

        }

        return view
    }

    private fun listenToTextChangeSearchEt(){
        fleetListBinding.searchFleetListEt.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()){
                    fleetListViewModel.setToQuery()
                    fleetListViewModel.getDataByQuery(adminId, s.toString())
                }else{
                    fleetListViewModel.getAllData(adminId)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                TODO("Not yet implemented")
            }

        })
    }

}