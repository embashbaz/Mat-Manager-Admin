package com.example.matatumanageradmin.data

import com.example.matatumanageradmin.utils.OperationStatus
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class KtorRepository @Inject constructor(
    private val mAuth: FirebaseAuth,
    private  val api: MatManagerApi
): MainRepository{
    override suspend fun loginAdmin(email: String, password: String): OperationStatus<MatAdmin> {
        TODO("Not yet implemented")
    }

    override suspend fun registerAdmin(
        matatuAdmin: MatAdmin,
        password: String
    ): OperationStatus<String> {
        TODO("Not yet implemented")
    }

    override suspend fun registerDriver(driver: Driver, password: String): OperationStatus<String> {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }

    override suspend fun updateTrip(trip: Trip): OperationStatus<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getAdmin(uId: String): OperationStatus<MatAdmin> {
        TODO("Not yet implemented")
    }

    override suspend fun getDrivers(uId: String): OperationStatus<List<Driver>> {
        TODO("Not yet implemented")
    }

    override suspend fun getBuses(uId: String): OperationStatus<List<Bus>> {
        TODO("Not yet implemented")
    }

    override suspend fun getDriver(driverId: String): OperationStatus<Driver> {
        TODO("Not yet implemented")
    }

    override suspend fun getBus(plate: String): OperationStatus<Bus> {
        TODO("Not yet implemented")
    }

    override suspend fun getTrips(
        type: String,
        id: String,
        startDate: String,
        endDate: String
    ): OperationStatus<List<Trip>> {
        TODO("Not yet implemented")
    }

    override suspend fun getStats(
        type: String,
        id: String,
        startDate: String,
        endDate: String
    ): OperationStatus<List<Statistics>> {
        TODO("Not yet implemented")
    }

    override suspend fun getExpenses(
        type: String,
        id: String,
        startDate: String,
        endDate: String
    ): OperationStatus<List<Expense>> {
        TODO("Not yet implemented")
    }
}