package com.example.matatumanageradmin.utils

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.matatumanageradmin.ui.dialog.NoticeDialogFragment
import com.google.android.material.textfield.TextInputLayout


fun stringFromTl(tl: TextInputLayout) = tl.editText!!.text.toString()

fun Fragment.showLongToast(message: String){
    Toast.makeText(this.activity, message, Toast.LENGTH_LONG).show()
}