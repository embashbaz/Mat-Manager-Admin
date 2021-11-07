package com.example.matatumanageradmin.data

import com.example.matatumanageradmin.utils.OperationStatus
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class KtorRepository @Inject constructor(
    private val mAuth: FirebaseAuth,
    private  val api: MatManagerApi
): MainRepository{
    override suspend fun loginAdmin(email: String, password: String): OperationStatus<MatAdmin> {
        return try {

            var uId = mAuth.signInWithEmailAndPassword(email, password).await().user!!.uid
            if (!!uId.isNullOrEmpty()){
                getAdmin(uId)
            }else{
                OperationStatus.Error("Pleas make sure the account exit")
            }

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
            if (!!uId.isEmpty()){
                matatuAdmin.matAdminId = uId
                val response = api.createMatAdmin(matatuAdmin)
                val result = response.body()
                if(response.isSuccessful && result != null && !result.isNullOrEmpty()){
                    OperationStatus.Success(result)
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
            if (!!uId.isEmpty()){
                val response = api.createDriver(driver)
                val result = response.body()
                if(response.isSuccessful && result != null && !result.isNullOrEmpty()){
                    OperationStatus.Success(result)
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
            if(response.isSuccessful && result != null && !result.isNullOrEmpty()){
                OperationStatus.Success(result)
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
            if(response.isSuccessful && result != null && !result.isNullOrEmpty()){
                OperationStatus.Success(result)
            }else{
                OperationStatus.Error(response.message())
            }

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
            val response = api.getDrivers("", uId)
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
            val response = api.getBus("", uId)
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
            val response = api.getDrivers("", driverId)
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
            val response = api.getBus("", plate)
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
            val response = api.getTrips("", id, startDate, endDate)
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
            val response = api.getStats("", id, startDate, endDate)
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
            val response = api.getExpenses("", id, startDate, endDate)
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

    override suspend fun getDriverByName(stringQuery: String): OperationStatus<Driver> {
        TODO("Not yet implemented")
    }

    override suspend fun getBusesByPlate(stringQuery: String): OperationStatus<List<Bus>> {
        TODO("Not yet implemented")
    }
}