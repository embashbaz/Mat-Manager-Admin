package com.example.matatumanageradmin.ui.dashboard

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.matatumanageradmin.MatManagerAdminApp
import com.example.matatumanageradmin.R
import com.example.matatumanageradmin.databinding.FragmentDashboardBinding
import com.example.matatumanageradmin.ui.dialog.NoticeDialogFragment
import com.example.matatumanageradmin.utils.Constant
import com.example.matatumanageradmin.utils.showLongToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment(), NoticeDialogFragment.NoticeDialogListener {

    private lateinit var dashboardBinding: FragmentDashboardBinding
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private val adminId : String by lazy {  ( activity?.application as MatManagerAdminApp).matAdmin!!.matAdminId }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
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

        dashboardViewModel.logOutStatus.observe(viewLifecycleOwner, {
            when(it){
                is DashboardViewModel.LogOutStatus.Failed -> showLongToast(it.errorText)
                is DashboardViewModel.LogOutStatus.Success -> {
                    ( activity?.application as MatManagerAdminApp).matAdmin = null
                    this.findNavController().navigateUp()
                    dashboardViewModel.setLogoutStatusToEmpty()
                }
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.dashboard_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.log_out_menu){
           openNoticeDialog("Yes", "Are you sure you want to logout")
        }



        return super.onOptionsItemSelected(item)
    }


    fun openNoticeDialog(positiveButton: String,  message: String){
        val dialog = NoticeDialogFragment(positiveButton, message)
        dialog.setListener(this)
        dialog.show(parentFragmentManager, "Log Out")

    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        super.onDialogPositiveClick(dialog)
        dashboardViewModel.logOut()
    }
}