package com.example.matatumanageradmin.ui.matAdminRegistration

import androidx.lifecycle.ViewModel
import com.example.matatumanageradmin.data.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MatManagerAdminRegistration @Inject
    constructor(private var repository: MainRepository): ViewModel() {


}