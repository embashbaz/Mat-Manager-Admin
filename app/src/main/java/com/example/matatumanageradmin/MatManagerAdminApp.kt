package com.example.matatumanageradmin

import android.app.Application
import com.example.matatumanageradmin.data.MatAdmin
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MatManagerAdminApp : Application(){
    var matAdmin: MatAdmin? = null


}