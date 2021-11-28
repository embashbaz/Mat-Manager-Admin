package com.example.matatumanageradmin.data

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.matatumanageradmin.utils.Constant
import com.example.matatumanageradmin.utils.OperationStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import okhttp3.internal.wait
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class KtorRepository @Inject constructor(
    private val mAuth: FirebaseAuth,
    private val api: MatManagerApi,
    private val storage: FirebaseStorage
): MainRepository{
    override suspend fun loginAdmin(email: String, password: String): OperationStatus<MatAdmin> {
        return try {

            var uId = mAuth.signInWithEmailAndPassword(email, password).await().user!!.uid
            if (!uId.isNullOrEmpty()){
                return getAdmin(uId)
            }else{
                OperationStatus.Error("Pleas make sure the account exit")
            }

        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun forgotAdminPassword(email: String): OperationStatus<String> {
        return try {
           mAuth.sendPasswordResetEmail(email).await()
            return OperationStatus.Success("Reset email sent")

        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }

    }

    override suspend fun logOut(): OperationStatus<String> {
     return try {
             mAuth.signOut()
            return OperationStatus.Success("Logged out")
    }catch (e: Exception){
        OperationStatus.Error(e.message ?: "An error occurred")
    }
    }

    override suspend fun registerAdmin(
        matatuAdmin: MatAdmin,
        password: String
    ): OperationStatus<String> {
        return try {

            var uId = mAuth.createUserWithEmailAndPassword(matatuAdmin.email, password).await().user!!.uid
            if (!uId.isEmpty()){
                matatuAdmin.matAdminId = uId
                val response = api.createMatAdmin(matatuAdmin)
                val result = response.body()
                if(response.isSuccessful && result != null && result.has("success")){
                    OperationStatus.Success(result.toString())
                }else{
                    OperationStatus.Error(response.message())
                }
            }else{
                OperationStatus.Error("An error occurred, check your internet connection")
            }

        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun registerDriver(driver: Driver, password: String): OperationStatus<String> {
        return try {

            var uId = mAuth.createUserWithEmailAndPassword(driver.email, password).await().user!!.uid
            if (!uId.isEmpty()){
                driver.driverId = uId
                val response = api.createDriver(driver)
                val result = response.body()
                if(response.isSuccessful && result != null && result.has("true")){
                    OperationStatus.Success(result.toString())
                }else{
                    OperationStatus.Error(response.message())
                }
            }else{
                OperationStatus.Error("An error occurred, check your internet connection")
            }

        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun addTrip(trip: Trip): OperationStatus<String> {
        return  try{
            val response = api.createTrip(trip)
            val result = response.body()
            if(response.isSuccessful && result != null && result.has("success")){
                OperationStatus.Success(result.toString())
            }else{
                OperationStatus.Error(response.message())
            }

        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun addMatatu(matatu: Bus): OperationStatus<String> {
        return  try{
            val response = api.createBus(matatu)
            val result = response.body()
            if(response.isSuccessful && result != null && result.has("success")){
                OperationStatus.Success(result.toString())
            }else{
                OperationStatus.Error(response.message())
            }

        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun addSaveImage(
        mBitmap: Bitmap,
        adminId: String,
        type: String,
        name: String
    ): OperationStatus<String> {
        var imageRef = storage.reference.child(adminId+"/"+type+"/"+name+".jpg")

        val bitmap = mBitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()


        return try {



            var uploadTask = imageRef.putBytes(data).await().task.await()


           var progress = (100.0 * uploadTask.bytesTransferred) / uploadTask.totalByteCount

          // while (progress<100.0){
         //      progress = (100.0 * uploadTask.bytesTransferred) / uploadTask.totalByteCount

         //  }

            Log.d("THISSSSSSSSSSSSSSSSSSS", progress.toString())


            val url = imageRef.downloadUrl.await()
            Log.d("THISSSSSSSSSSSSSSSSSSS", "RETURNINNNNNNNNNNNNNNNNNNNNNNG")
            return OperationStatus.Success(url.toString())


        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }

    }


    override suspend fun updateBus(bus: Bus): OperationStatus<String> {
        return  try{
            val response = api.updateBus(bus)
            val result = response.body()
            if(response.isSuccessful && result != null && !result.isNullOrEmpty()){
                OperationStatus.Success(result)
            }else{
                OperationStatus.Error(response.message())
            }

        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun updateTrip(trip: Trip): OperationStatus<String> {
        return  try{
            val response = api.updateTrip(trip)
            val result = response.body()
            if(response.isSuccessful && result != null && !result.isNullOrEmpty()){
                OperationStatus.Success(result)
            }else{
                OperationStatus.Error(response.message())
            }

        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun getAdmin(uId: String): OperationStatus<MatAdmin> {
        return  try{
            val response = api.getAdmin(uId)
            val result = response.body()
            if(response.isSuccessful && result != null){
                OperationStatus.Success(result)
            }else{
                OperationStatus.Error(response.message())
            }

        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun getDrivers(uId: String): OperationStatus<List<Driver>> {
        return  try{
            val response = api.getDrivers(Constant.LIST_DRIVERS, uId, "")
            val result = response.body()
            if(response.isSuccessful && !result!!.isEmpty()!!){
                OperationStatus.Success(result)
            }else{
                OperationStatus.Error(response.message())
            }

        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun getBuses(uId: String): OperationStatus<List<Bus>> {
        return  try{
            val response = api.getBus(Constant.LIST_BUSES, uId, "")
            val result = response.body()
            if(response.isSuccessful && !result!!.isEmpty()!!){
                OperationStatus.Success(result)
            }else{
                OperationStatus.Error(response.message())
            }

        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun getDriver(driverId: String): OperationStatus<Driver> {
        return  try{
            val response = api.getDrivers(Constant.SINGLE_DRIVER, driverId, "")
            val result = response.body()
            if(response.isSuccessful && !result!!.isEmpty()!!){
                OperationStatus.Success(result[0])
            }else{
                OperationStatus.Error(response.message())
            }

        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun getBus(plate: String): OperationStatus<Bus> {
        return  try{
            val response = api.getBus(Constant.SINGLE_BUS, plate, "")
            val result = response.body()
            if(response.isSuccessful && !result!!.isEmpty()!!){
                OperationStatus.Success(result[0])
            }else{
                OperationStatus.Error(response.message())
            }

        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun getTrips(
        type: String,
        id: String,
        startDate: String,
        endDate: String
    ): OperationStatus<List<Trip>> {
        return  try{
            val response = api.getTrips(type, id, startDate, endDate)
            val result = response.body()
            if(response.isSuccessful && !result!!.isEmpty()!!){
                OperationStatus.Success(result)
            }else{
                OperationStatus.Error(response.message())
            }

        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun getStats(
        type: String,
        id: String,
        startDate: String,
        endDate: String
    ): OperationStatus<List<Statistics>> {
        return  try{
            val response = api.getStats(type, id, startDate, endDate)
            val result = response.body()
            if(response.isSuccessful && !result!!.isEmpty()!!){
                OperationStatus.Success(result)
            }else{
                OperationStatus.Error(response.message())
            }

        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun getExpenses(
        type: String,
        id: String,
        startDate: String,
        endDate: String
    ): OperationStatus<List<Expense>> {
        return  try{
            val response = api.getExpenses(type, id, startDate, endDate)
            val result = response.body()
            val errorBody = response.errorBody()?.charStream()?.readText()?:""
            Log.d("THISSSSSS", errorBody)
            if(response.isSuccessful && !result!!.isEmpty()!!){
                OperationStatus.Success(result)
            }else{
                OperationStatus.Error(response.message())
            }

        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun getDriverWithQuery(
        stringQuery: String,
        adminId: String
    ): OperationStatus<List<Driver>> {
        return  try{
            val response = api.getDrivers(Constant.DRIVERS_QUERY, adminId, stringQuery)
            val result = response.body()
            if(response.isSuccessful && !result!!.isEmpty()!!){
                OperationStatus.Success(result)
            }else{
                OperationStatus.Error(response.message())
            }

        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun getBusesWithQuery(
        stringQuery: String,
        adminId: String
    ): OperationStatus<List<Bus>> {
        return  try{
            val response = api.getBus(Constant.BUSES_QUERY, adminId, stringQuery)
            val result = response.body()
            if(response.isSuccessful && !result!!.isEmpty()!!){
                OperationStatus.Success(result)
            }else{
                OperationStatus.Error(response.message())
            }

        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun getIssues(
        type: String,
        id: String,
        startDate: String,
        endDate: String
    ): OperationStatus<List<Issue>> {
        return  try{
            val response = api.getIssues(type, id, startDate, endDate)
            val result = response.body()
            val errorBody = response.errorBody()?.charStream()?.readText()?:""
            Log.d("THISSSSSS", errorBody)
            if(response.isSuccessful && !result!!.isEmpty()!!){
                OperationStatus.Success(result)
            }else{
                OperationStatus.Error(response.message())
            }

        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }
    }
}