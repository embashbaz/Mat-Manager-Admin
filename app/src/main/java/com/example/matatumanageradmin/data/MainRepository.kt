package com.example.matatumanageradmin.data


import com.example.matatumanageradmin.utils.OperationStatus

interface MainRepository {

    suspend fun loginAdmin(email: String, password: String): OperationStatus<MatAdmin>
    suspend fun registerAdmin(matatuAdmin: MatAdmin, password: String): OperationStatus<String>
    suspend fun registerDriver(driver: Driver, password: String): OperationStatus<String>
    suspend fun addTrip (trip: Trip): OperationStatus<String>
    suspend fun addMatatu (matatu: Bus): OperationStatus<String>

    //ToDo: update admin
    //ToDo: update driver
    suspend fun updateBus(bus: Bus): OperationStatus<String>
    suspend fun updateTrip(trip: Trip): OperationStatus<String>

    suspend fun getAdmin(uId: String): OperationStatus<MatAdmin>
    suspend fun getDrivers(uId: String): OperationStatus<List<Driver>>
    suspend fun getBuses(uId: String): OperationStatus<List<Bus>>
    suspend fun getDriver(driverId: String ): OperationStatus<Driver>
    suspend fun getBus(plate: String): OperationStatus<Bus>

    suspend fun getTrips(type: String, id: String, startDate: String, endDate: String): OperationStatus<List<Trip>>
    suspend fun getStats(type: String, id: String, startDate: String, endDate: String): OperationStatus<List<Statistics>>
    suspend fun getExpenses(type: String, id: String, startDate: String, endDate: String): OperationStatus<List<Expense>>

    suspend fun getDriverWithQuery(stringQuery: String, adminId: String): OperationStatus<List<Driver>>
    suspend fun getBusesWithQuery(stringQuery: String, adminId: String): OperationStatus<List<Bus>>
    suspend fun getIssues(type: String, id: String, startDate: String, endDate: String): OperationStatus<List<Issue>>


}