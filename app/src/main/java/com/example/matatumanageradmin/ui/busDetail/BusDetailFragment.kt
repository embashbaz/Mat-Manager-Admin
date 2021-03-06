package com.example.matatumanageradmin.ui.busDetail

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.matatumanageradmin.MatManagerAdminApp
import com.example.matatumanageradmin.R
import com.example.matatumanageradmin.data.Bus
import com.example.matatumanageradmin.databinding.FragmentBusDetailBinding
import com.example.matatumanageradmin.utils.Constant
import com.example.matatumanageradmin.utils.showLongToast
import com.example.matatumanageradmin.utils.stringFromTl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BusDetailFragment : Fragment() {

    var imageBitmap: Bitmap? = null
    val REQUEST_IMAGE_CAPTURE = 1

    private lateinit var busDetailBinding: FragmentBusDetailBinding
    private val busDetailViewModel: BusDetailViewModel by viewModels()

    private val adminId: String by lazy { (activity?.application as MatManagerAdminApp).matAdmin!!.matAdminId }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        busDetailBinding = FragmentBusDetailBinding.inflate(inflater, container, false)
        val view = busDetailBinding.root

        if (arguments?.getString("createOrUpdateBus") == "update") {
            busDetailViewModel.changeToUpdate()
            arguments?.getParcelable<Bus>("bus")?.let { busDetailViewModel.setBusObject(it) }

        } else {
            busDetailBinding.nextActionBus.layoutParams.height = 0
        }

        busDetailBinding.busImageDetail.setOnClickListener {
            dispatchTakePictureIntent()
        }

        busDetailViewModel.createOrUpdateBus.observe(viewLifecycleOwner, {
            if (it) {
                changeViewBehaviour()
                populateView()
                listenToNextActionClick()

            } else {

            }

        })

        busDetailBinding.addBusButton.setOnClickListener {
            saveBusOnButtonClicked()
        }

        busDetailViewModel.createOrUpdateBusResult.observe(viewLifecycleOwner, {
            when (it) {
                is BusDetailViewModel.CreateOrUpdateBusStatus.Success -> showLongToast("Success")
                is BusDetailViewModel.CreateOrUpdateBusStatus.Failed -> showLongToast(it.errorText)
            }

        })

        return view
    }

    private fun listenToNextActionClick() {
        busDetailBinding.goToExpensesViewCard.setOnClickListener {
            val busPlate = busDetailViewModel.busObject.value!!.plate
            val bundle = Bundle()
            bundle.putString("record_id", busPlate)
            bundle.putString("expense_type", Constant.BUS_EXPENSE)
            this.findNavController()
                .navigate(R.id.action_busDetailFragment_to_expenseListFragment, bundle)
        }

        busDetailBinding.goToIssueCard.setOnClickListener {
            val busPlate = busDetailViewModel.busObject.value!!.plate
            val bundle = Bundle()
            bundle.putString("record_id", busPlate)
            bundle.putString("issue_type", Constant.BUS_ISSUE)
            this.findNavController()
                .navigate(R.id.action_busDetailFragment_to_issueFragment, bundle)

        }

        busDetailBinding.goToListTrips.setOnClickListener {
            val busPlate = busDetailViewModel.busObject.value!!.plate
            val bundle = Bundle()
            bundle.putString("record_id", busPlate)
            bundle.putString("trip_type", Constant.BUSES_TRIP)
            this.findNavController().navigate(R.id.action_busDetailFragment_to_tripFragment, bundle)

        }

        busDetailBinding.goToStatCard.setOnClickListener {
            val busPlate = busDetailViewModel.busObject.value!!.plate
            val bundle = Bundle()
            bundle.putString("record_id", busPlate)
            bundle.putString("stat_type", Constant.BUS_STAT)
            this.findNavController()
                .navigate(R.id.action_busDetailFragment_to_statisticsFragment, bundle)
        }
    }

    private fun saveBusOnButtonClicked() {
        busDetailViewModel.getDataFromView(
            adminId,
            stringFromTl(busDetailBinding.plateBusDetailTl),
            "",
            stringFromTl(busDetailBinding.busModelDetailTl),
            stringFromTl(busDetailBinding.commentBusDetailTl),
            imageBitmap
        )
    }

    private fun populateView() {
        busDetailViewModel.busObject.observe(viewLifecycleOwner, {
            if (it != null) {
                busDetailBinding.plateBusDetailTl.editText!!.setText(it.plate)
                busDetailBinding.busModelDetailTl.editText!!.setText(it.carModel)
                busDetailBinding.commentBusDetailTl.editText!!.setText(it.comment)
                if (!it.picture.isNullOrEmpty()) {
                    Glide.with(requireView()).load(it.picture)
                        .apply(RequestOptions.circleCropTransform())
                        .into(busDetailBinding.busImageDetail)
                }
            }

        })
    }

    private fun changeViewBehaviour() {
        busDetailBinding.addBusButton.text = "Update"

    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            activity?.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            imageBitmap = data?.extras?.get("data") as Bitmap
            busDetailBinding.busImageDetail.setImageBitmap(imageBitmap)
            //confirmSavingPicture(imageBitmap!!)

        }
    }


}