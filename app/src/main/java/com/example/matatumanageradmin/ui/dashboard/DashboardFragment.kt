package com.example.matatumanageradmin.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.matatumanageradmin.MatManagerAdminApp
import com.example.matatumanageradmin.R
import com.example.matatumanageradmin.databinding.FragmentDashboardBinding
import com.example.matatumanageradmin.utils.Constant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private lateinit var dashboardBinding: FragmentDashboardBinding
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private val adminId : String by lazy {  ( activity?.application as MatManagerAdminApp).matAdmin!!.matAdminId }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardBinding = FragmentDashboardBinding.inflate(inflater, container, false)
        val view = dashboardBinding.root

        observeCardClicked()
        listenButtonClicked()


        return view
    }

    private fun observeCardClicked() {
        dashboardViewModel.fleetCardClicked.observe(viewLifecycleOwner, {
            if (it){
                this.findNavController().navigate(R.id.action_dashboardFragment_to_busListFragment)
                dashboardViewModel.fleetCardClicked(false)
            }
        })

        dashboardViewModel.issuesCardClicked.observe(viewLifecycleOwner, {
            if (it){
                val bundle = Bundle()
                bundle.putString("issue_type", Constant.ALL_ISSUE)
                bundle.putString("record_id", adminId)
                this.findNavController().navigate(R.id.action_dashboardFragment_to_issueFragment, bundle)
                dashboardViewModel.issueCardClicked(false)
            }
        })

        dashboardViewModel.trackingCardClicked.observe(viewLifecycleOwner, {
            if (it){
                this.findNavController().navigate(R.id.action_dashboardFragment_to_trackingFragment)
                dashboardViewModel.trackingCardClicked(false)
            }
        })

        dashboardViewModel.profileCardClicked.observe(viewLifecycleOwner, {
            if (it){
                val bundle = Bundle()
                bundle.putString("registration", "profile")

                this.findNavController().navigate(R.id.action_dashboardFragment_to_matManagerRegistrationFragment, bundle)
                dashboardViewModel.profileCardClicked(false)
            }
        })
    }

    private fun listenButtonClicked() {
        dashboardBinding.goToFleetListCard.setOnClickListener {
            dashboardViewModel.fleetCardClicked(true)
        }

        dashboardBinding.goToIssueListCard.setOnClickListener {
            dashboardViewModel.issueCardClicked(true)
        }

        dashboardBinding.goToProfile.setOnClickListener {
            dashboardViewModel.profileCardClicked(true)
        }

        dashboardBinding.goTrackingFragmentCard.setOnClickListener {
            dashboardViewModel.trackingCardClicked(true)
        }
    }


}