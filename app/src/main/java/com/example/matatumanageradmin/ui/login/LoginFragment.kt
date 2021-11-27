package com.example.matatumanageradmin.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.matatumanageradmin.MatManagerAdminApp
import com.example.matatumanageradmin.R
import com.example.matatumanageradmin.databinding.FragmentLoginBinding
import com.example.matatumanageradmin.ui.dialog.NoticeDialogFragment
import com.example.matatumanageradmin.ui.resetPassword.ResetPasswordDialog
import com.example.matatumanageradmin.utils.stringFromTl
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment(), NoticeDialogFragment.NoticeDialogListener,
    ResetPasswordDialog.ResetPasswordDialogListener {

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
        onResetPasswordClicked()
        obsevePasswordReset()

        return view
    }

    private fun obsevePasswordReset() {
        loginViewModel.resetPasswordStatus.observe(viewLifecycleOwner, {
            when(it){
                is LoginViewModel.LoginStatus.Failed -> {
                    hideProgressBar()
                    openNoticeDialog("ok", it.errorText)
                }
                is LoginViewModel.LoginStatus.Success -> {
                    hideProgressBar()
                    openNoticeDialog("ok", it.resultText)

                }

                is LoginViewModel.LoginStatus.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun onResetPasswordClicked() {
            loginBinding.forgotPassword.setOnClickListener {
                openResetPasswordDialog()
            }
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
                loginViewModel.setToRegsterButtonClicked(false)
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
                    hideProgressBar()
                    openNoticeDialog("ok", it.errorText)
                }
                is LoginViewModel.LoginStatus.Success -> {
                    (activity?.application as MatManagerAdminApp).matAdmin = it.adminObject
                    hideProgressBar()
                    this.findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment)
                    loginViewModel.setLoginStatusToEmpty()

                }

                is LoginViewModel.LoginStatus.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    fun openNoticeDialog(positiveButton: String,  message: String){
        val dialog = NoticeDialogFragment(positiveButton, message)
        dialog.setListener(this)
        dialog.show(parentFragmentManager, "Error")

    }

    fun openResetPasswordDialog(){
        val dialog = ResetPasswordDialog()
        dialog.setListener(this)
        dialog.show(parentFragmentManager, "Reset Password")

    }

    private fun hideProgressBar(){
        loginBinding.progressBarLogin.visibility = View.INVISIBLE
    }

    private fun showProgressBar(){
        loginBinding.progressBarLogin.visibility = View.VISIBLE
    }

    override fun onSaveButtonClicked(email: String) {
        loginViewModel.resetPassword(email)
    }


}