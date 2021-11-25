package com.example.matatumanageradmin.ui.matAdminRegistration

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.matatumanageradmin.MatManagerAdminApp
import com.example.matatumanageradmin.R
import com.example.matatumanageradmin.data.MatAdmin
import com.example.matatumanageradmin.databinding.FragmentMatManagerRegistrationBinding
import com.example.matatumanageradmin.ui.dialog.NoticeDialogFragment
import com.example.matatumanageradmin.utils.stringFromTl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatManagerRegistrationFragment : Fragment(), NoticeDialogFragment.NoticeDialogListener {

    private lateinit var matManagerRegistrationFragmentBinding: FragmentMatManagerRegistrationBinding
    private val adminRegisterViewModel: MatManagerAdminRegistrationViewModel by viewModels()
    var matAdmin: MatAdmin? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        matManagerRegistrationFragmentBinding = FragmentMatManagerRegistrationBinding.inflate(inflater, container, false)
        val view = matManagerRegistrationFragmentBinding.root

        if(arguments?.getString("registration")== "profile") {
            adminRegisterViewModel.changeToProfileType()
            matAdmin = ( activity?.application as MatManagerAdminApp).matAdmin!!
        }


        adminRegisterViewModel.registerOrProfile.observe(viewLifecycleOwner, {
            if (it){
                changeViewBehaviour()
                populateViewWithAdminData()
            }
        })

        matManagerRegistrationFragmentBinding.registerAdminButton.setOnClickListener {
            getDataFromView()
        }

        adminRegisterViewModel.registrationstate.observe(viewLifecycleOwner, {
            when(it){
                is MatManagerAdminRegistrationViewModel.RegistrationStatus.Success -> {openNoticeDialog("Ok", it.resultText)}
                is MatManagerAdminRegistrationViewModel.RegistrationStatus.Failed -> {
                    openNoticeDialog("Ok", it.errorText)
                    Log.d("API ERROR", it.errorText)

                }
            }
        })


        return view
    }


    private fun changeViewBehaviour() {
                matManagerRegistrationFragmentBinding.nameRegister .isEnabled = false
                matManagerRegistrationFragmentBinding.emailRegister.isEnabled = false
                matManagerRegistrationFragmentBinding.phoneRegister.isEnabled = false
                matManagerRegistrationFragmentBinding.passwordRegister.isEnabled = false
                matManagerRegistrationFragmentBinding.confirmPassword.isEnabled = false
                matManagerRegistrationFragmentBinding.cityAdress.isEnabled = false
                matManagerRegistrationFragmentBinding.addressTl.isEnabled = false
                matManagerRegistrationFragmentBinding.licenseNumberTl.isEnabled = false
                matManagerRegistrationFragmentBinding.registerAdminButton.isEnabled = false
    }

    private fun populateViewWithAdminData() {
        matManagerRegistrationFragmentBinding.nameRegister.editText?.setText(matAdmin!!.name)
        matManagerRegistrationFragmentBinding.emailRegister.editText?.setText(matAdmin!!.email)
        matManagerRegistrationFragmentBinding.phoneRegister.editText?.setText(matAdmin!!.phoneNumber.toString())
        matManagerRegistrationFragmentBinding.cityAdress.editText?.setText(matAdmin!!.address)
        matManagerRegistrationFragmentBinding.addressTl.editText?.setText(matAdmin!!.address)
        matManagerRegistrationFragmentBinding.licenseNumberTl.editText?.setText(matAdmin!!.licenseNumber)
    }

    private fun  getDataFromView(){
        adminRegisterViewModel.getUiData(
            stringFromTl(matManagerRegistrationFragmentBinding.nameRegister),
            stringFromTl(matManagerRegistrationFragmentBinding.emailRegister),
            stringFromTl(matManagerRegistrationFragmentBinding.phoneRegister),
            stringFromTl(matManagerRegistrationFragmentBinding.passwordRegister),
            stringFromTl(matManagerRegistrationFragmentBinding.confirmPassword),
            stringFromTl(matManagerRegistrationFragmentBinding.cityAdress),
            stringFromTl(matManagerRegistrationFragmentBinding.addressTl),
            stringFromTl(matManagerRegistrationFragmentBinding.licenseNumberTl)
        )
    }

    fun openNoticeDialog(positiveButton: String,  message: String){
        val dialog = NoticeDialogFragment(positiveButton, message)
        dialog.setListener(this)
        dialog.show(parentFragmentManager, "Confirm you want to save picture")

    }




}