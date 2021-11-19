package com.example.matatumanageradmin.ui.fleetList

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matatumanageradmin.R
import com.example.matatumanageradmin.data.Bus
import com.example.matatumanageradmin.data.Driver
import com.example.matatumanageradmin.databinding.FleetItemBinding
import com.example.matatumanageradmin.databinding.FragmentFleetListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_fleet_list.*

@AndroidEntryPoint
class FleetListFragment : Fragment() {



    private lateinit var fleetListBinding: FragmentFleetListBinding
    private val fleetListViewModel: FleetListViewModel by viewModels()
    private var adminId = ""
    private lateinit var  fleetListAdapter: FleetListAdapter
    

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        fleetListBinding = FragmentFleetListBinding.inflate(inflater, container, false)
        val view = fleetListBinding.root

        fleetListViewModel.setListTypeToBus()
        fleetListViewModel.getAllData(adminId)
        listenToTextChangeSearchEt()

        fleetListAdapter =FleetListAdapter{item -> moveToDetail(item)  }

        observeDataFromViewModel()

        navigationSelection()

        return view
    }

    fun observeDataFromViewModel(){
        fleetListViewModel.busOrDriverList.observe(viewLifecycleOwner, {
            if (it){
                fleetListViewModel.driverList.observe(viewLifecycleOwner, {
                    when(it){
                        is FleetListViewModel.DriverListStatus.Loading -> {
                            enableProgressBar()
                        }

                        is FleetListViewModel.DriverListStatus.Empty -> {
                           disableProgressBar()
                            enableNoDataTxt()
                        }

                        is FleetListViewModel.DriverListStatus.Success -> {
                            disableProgressBar()
                            hideNoDataTxt()
                            fleetListAdapter.setData(it.drivers as ArrayList<Any>)
                            setRecyclerView()
                        }


                    }
                })
            }else{
                fleetListViewModel.busList.observe(viewLifecycleOwner, {
                    when(it){
                        is FleetListViewModel.BusListStatus.Loading -> {
                            enableProgressBar()
                        }
                        is FleetListViewModel.BusListStatus.Failed -> {
                            disableProgressBar()
                            enableNoDataTxt()
                        }

                        is FleetListViewModel.BusListStatus.Success -> {

                            disableProgressBar()
                            hideNoDataTxt()
                            fleetListAdapter.setData(it.buses as ArrayList<Any>)
                            setRecyclerView()

                        }

                    }

                })

            }


        })


    }

    private fun enableNoDataTxt() {
        fleetListBinding.noDataFleetListTxt.visibility = View.VISIBLE
    }

    private fun hideNoDataTxt(){
        fleetListBinding.noDataFleetListTxt.visibility = View.INVISIBLE
    }

    fun disableProgressBar(){
        fleetListBinding.fleetListProgressBar.visibility = View.INVISIBLE
    }

    fun enableProgressBar(){
        fleetListBinding.fleetListProgressBar.visibility = View.VISIBLE
    }

    fun setRecyclerView(){
        fleetListBinding.recyclerviewFleetList.layoutManager = LinearLayoutManager(activity)
        fleetListBinding.recyclerviewFleetList.adapter = fleetListAdapter
    }

    fun navigationSelection(){
        fleetListBinding.bottomNavigationView.setOnItemReselectedListener {
            when(it.itemId){
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationSelection()

    }

    private fun moveToDetail(item: Any) {
        if(item is Bus){

        }else if(item is Driver){

        }
    }

    private fun listenToTextChangeSearchEt(){
        fleetListBinding.searchFleetListEt.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

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

            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
       // super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fleet_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
            if (item.itemId == R.id.new_bus_menu){
                goToNewBus()
            }
            else if (item.itemId == R.id.new_driver_menu){
                goToNewDriver()
            }


        return super.onOptionsItemSelected(item)
    }

    fun goToNewBus(){
        this.findNavController().navigate(R.id.action_busListFragment_to_busDetailFragment)
    }

    fun goToNewDriver(){
        this.findNavController().navigate(R.id.action_busListFragment_to_driverDetailFragment)
    }
}