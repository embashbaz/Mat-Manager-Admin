package com.example.matatumanageradmin.data

import com.example.matatumanageradmin.utils.OperationStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FirebaseRepository @Inject constructor(
    private val mFirestore: FirebaseFirestore,
    private val mAuth: FirebaseAuth,
    uId: String?
): MainRepository {
    override suspend fun loginAdmin(email: String, password: String): OperationStatus<MatatuAdmin> {
        TODO("Not yet implemented")
    }

    override suspend fun registerAdmin(
        matatuAdmin: MatatuAdmin,
        password: String
    ): OperationStatus<String> {
        TODO("Not yet implemented")
    }

    override suspend fun registerDriver(driver: Driver, password: String): OperationStatus<String> {
        TODO("Not yet implemented")
    }

    override suspend fun addTrip(trip: MatatuTrip): OperationStatus<String> {
        TODO("Not yet implemented")
    }

    override suspend fun addMatatu(matatu: Matatu): OperationStatus<String> {
        TODO("Not yet implemented")
    }


}