package com.example.matatumanageradmin.data

import com.example.matatumanageradmin.utils.Constant
import com.example.matatumanageradmin.utils.OperationStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class FirebaseRepository @Inject constructor(
    private val mFirestore: FirebaseFirestore,
    private val mAuth: FirebaseAuth,
    uId: String?
): MainRepository {
    override suspend fun loginAdmin(email: String, password: String): OperationStatus<MatatuAdmin> {
      return  try {
           var uId = mAuth.signInWithEmailAndPassword(email, password).await().user!!.uid
          if (uId.isEmpty()){
              OperationStatus.Error("An error occurred")
          }else{
             var admin =  mFirestore.collection(Constant.admincollectionName).document(uId).get().await().toObject(MatatuAdmin::class.java)
              if (admin != null){
                  OperationStatus.Success(admin)
              }else{
                  OperationStatus.Error(("An error occurred"))
              }
          }

        }catch (e: Exception){
          OperationStatus.Error(e.message ?: "An error occurred")
        }

    }

    override suspend fun registerAdmin(
        matatuAdmin: MatatuAdmin,
        password: String
    ): OperationStatus<String> {

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