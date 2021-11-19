package com.example.matatumanageradmin.ui.driverDetail

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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.matatumanageradmin.R
import com.example.matatumanageradmin.data.Bus
import com.example.matatumanageradmin.data.Driver
import com.example.matatumanageradmin.databinding.FragmentDriverDetailBinding
import com.example.matatumanageradmin.databinding.FragmentMatManagerRegistrationBinding
import com.example.matatumanageradmin.ui.dialog.NoticeDialogFragment
import com.example.matatumanageradmin.utils.stringFromTl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DriverDetailFragment : Fragment(), NoticeDialogFragment.NoticeDialogListener {

    var imageBitmap: Bitmap? = null
    val REQUEST_IMAGE_CAPTURE = 1

    private lateinit var driverDetailBinding : FragmentDriverDetailBinding
    private val driverDetailViewModel : DriverDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        driverDetailBinding = FragmentDriverDetailBinding.inflate(inflater, container, false)
        val view = driverDetailBinding.root

        if(arguments?.getString("createOrUpdateDriver")== "update") {
            driverDetailViewModel.changeToProfileType()
            arguments?.getParcelable<Driver>("driver")?.let { driverDetailViewModel.setDriverObject( it )  }

        }

        driverDetailBinding.driverImageDetail.setOnClickListener { dispatchTakePictureIntent() }

        driverDetailViewModel.registerOrProfile.observe(viewLifecycleOwner, {
            if(it){
                populateView()
                changeViewBehaviour()
            }
        })

        driverDetailBinding.registerDriverButton.setOnClickListener {
            saveDriverOnButtonClicked()
        }

        driverDetailViewModel.registrationDriverState.observe(viewLifecycleOwner, {
            when(it){
                is DriverDetailViewModel.RegistrationDriverStatus.Success -> openNoticeDialog("Ok", it.resultText)
                is DriverDetailViewModel.RegistrationDriverStatus.Failed -> openNoticeDialog("Ok", it.errorText)
            }
        })

        return view
    }


    private fun saveDriverOnButtonClicked() {
        driverDetailViewModel.getUiData(stringFromTl(driverDetailBinding.nameRegister),
                                        stringFromTl(driverDetailBinding.emailRegisterDriverDetailTl),
                                        stringFromTl(driverDetailBinding.phoneDriverRegisterTl),
                                        stringFromTl(driverDetailBinding.passwordRegisterDriverTl),
                                        stringFromTl(driverDetailBinding.confirmPasswordDriverTl),
                                        stringFromTl(driverDetailBinding.addressDriverTl),
                                        stringFromTl(driverDetailBinding.permitDriverTl),
                                        stringFromTl(driverDetailBinding.idNumberDriverTl))
    }

    private fun populateView() {
            driverDetailViewModel.profileData.observe(viewLifecycleOwner, {
               driverDetailBinding.nameRegister.editText!!.setText(it.name)
                driverDetailBinding.emailRegisterDriverDetailTl.editText!!.setText(it.email)
                driverDetailBinding.phoneDriverRegisterTl.editText!!.setText(it.phoneNumber.toString())
                driverDetailBinding.passwordRegisterDriverTl.editText!!.setText("")
                driverDetailBinding.confirmPasswordDriverTl.editText!!.setText("")
                driverDetailBinding.addressDriverTl .editText!!.setText(it.address)
                driverDetailBinding.permitDriverTl.editText!!.setText(it.permitNumber)
                driverDetailBinding.idNumberDriverTl .editText!!.setText(it.driverId)

            })
    }

    private fun changeViewBehaviour() {
        driverDetailBinding.registerDriverButton.text = "Update"

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
            driverDetailBinding.driverImageDetail.setImageBitmap(imageBitmap)
            //confirmSavingPicture(imageBitmap!!)

        }
    }

    fun openNoticeDialog(positiveButton: String,  message: String){
        val dialog = NoticeDialogFragment(positiveButton, message)
        dialog.setListener(this)
        dialog.show(parentFragmentManager, "Confirm you want to save picture")

    }

}