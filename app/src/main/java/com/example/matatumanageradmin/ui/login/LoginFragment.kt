package com.example.matatumanageradmin.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.matatumanageradmin.R
import com.example.matatumanageradmin.databinding.FragmentLoginBinding
import com.example.matatumanageradmin.utils.stringFromTl


class LoginFragment : Fragment() {

    private lateinit var loginBinding: FragmentLoginBinding
    private val loginViewModel : LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        loginBinding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = loginBinding.root

        login()
        observeLoginStatus()
        observeRegisterButton()
        onRegisterButtonClicked()

        return view
    }

    private fun onRegisterButtonClicked() {
        loginBinding.registerLogin.setOnClickListener {
            loginViewModel.setToRegsterButtonClicked(true)
        }
    }

    private fun observeRegisterButton() {
        loginViewModel.registerButtonClicked.observe(viewLifecycleOwner, {
            if (it){
                this.findNavController().navigate(R.id.action_loginFragment_to_matManagerRegistrationFragment)
            }
        })
    }

    fun login(){
        loginBinding.loginButton.setOnClickListener {
            loginViewModel.loginMethod(
                stringFromTl(loginBinding.emailLogin),
                stringFromTl(loginBinding.passwordLogin)
            )
        }
    }

    fun observeLoginStatus(){
        loginViewModel.loginStatus.observe(viewLifecycleOwner, {
            when(it){
                is LoginViewModel.LoginStatus.Failed -> {

                }
                is LoginViewModel.LoginStatus.Success -> {
                    this.findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment)
                }
            }
        })
    }

}