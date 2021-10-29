package com.example.matatumanageradmin.data


import com.example.matatumanageradmin.utils.OperationStatus

interface MainRepository {

    suspend fun loginAdmin(email: String, password: String): OperationStatus<MatAdmin>
    suspend fun registerAdmin(matatuAdmin: MatAdmin, password: String): OperationStatus<String>
    suspend fun registerDriver(driver: Driver, password: String): OperationStatus<String>
    suspend fun addTrip (trip: Trip): OperationStatus<String>
    suspend fun addMatatu (matatu: Bus): OperationStatus<String>



}