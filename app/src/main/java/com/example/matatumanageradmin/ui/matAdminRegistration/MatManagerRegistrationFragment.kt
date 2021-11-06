package com.example.matatumanageradmin.ui.matAdminRegistration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.matatumanageradmin.R
import com.example.matatumanageradmin.databinding.FragmentMatManagerRegistrationBinding


class MatManagerRegistrationFragment : Fragment() {

    private lateinit var matManagerRegistrationFragmentBinding: FragmentMatManagerRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        matManagerRegistrationFragmentBinding = FragmentMatManagerRegistrationBinding.inflate(inflater, container, false)
        val view = matManagerRegistrationFragmentBinding.root
        return view

        matManagerRegistrationFragmentBinding.

    }


}