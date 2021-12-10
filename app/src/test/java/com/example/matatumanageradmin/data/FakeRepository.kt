package com.example.matatumanageradmin.data

import android.graphics.Bitmap
import com.example.matatumanageradmin.utils.OperationStatus

class FakeRepository: MainRepository {

    private val buses = mutableListOf<Bus>()
    private val drivers = mutableListOf<Driver>()
    private var testAdmin: MatAdmin? = null

    private val trips = mutableListOf<Trip>()
    private val stats = mutableListOf<Statistics>()
    private val issues = mutableListOf<Issue>()
    private val expenses = mutableListOf<Expense>()


    private var networkError = false

    fun returnNetworkError(value: Boolean){
        networkError = value
    }

    fun addExpenses(){
        trips.add(Trip())
        trips.add(Trip())
        trips.add(Trip())
    }

    fun addIssues(){
        issues.add(Issue())
        trips.add(Trip())
        trips.add(Trip())
    }

    fun addStat(){
        stats.add(Statistics())
        stats.add(Statistics())
        stats.add(Statistics())

    }

    fun addTrips(){
        trips.add(Trip())
        trips.add(Trip())
        trips.add(Trip())
    }




    override suspend fun loginAdmin(email: String, password: String): OperationStatus<MatAdmin> {
       if(networkError){
            if (testAdmin == null){
                return OperationStatus.Error("Admin doesn't exist")
            }else{
                return getAdmin(email)
            }
       }else{
           return OperationStatus.Error("Network error")
       }
    }

    override suspend fun forgotAdminPassword(email: String): OperationStatus<String> {
        if(networkError){
            return OperationStatus.Success("Suceess")
        }else{
            return OperationStatus.Error("Network error")
        }
    }

    override suspend fun logOut(): OperationStatus<String> {
        if(networkError){
            testAdmin = null
            return OperationStatus.Success("logged out")

        }else{
            return OperationStatus.Error("Network error")
        }
    }

    override suspend fun registerAdmin(
        matatuAdmin: MatAdmin,
        password: String
    ): OperationStatus<String> {
        if(networkError){
            testAdmin = matatuAdmin
            return OperationStatus.Success("admin added")

        }else{
            return OperationStatus.Error("Network error")
        }
    }

    override suspend fun registerDriver(driver: Driver, password: String): OperationStatus<String> {
        if(networkError){

            drivers.add(driver)
            return OperationStatus.Success("driver added")

        }else{
            return OperationStatus.Error("Network error")
        }
    }

    override suspend fun addTrip(trip: Trip): OperationStatus<String> {
        TODO("Not yet implemented")
    }

    override suspend fun addMatatu(matatu: Bus): OperationStatus<String> {
        if(networkError){
            buses.add(matatu)
            return OperationStatus.Success("bus added")

        }else{
            return OperationStatus.Error("Network error")
        }
    }

    override suspend fun addSaveImage(
        mBitmap: Bitmap,
        adminId: String,
        type: String,
        name: String
    ): OperationStatus<String> {
        if(networkError){
            return OperationStatus.Success("image saved")
        }else{
            return OperationStatus.Error("Network error")
        }
    }

    override suspend fun updateBus(bus: Bus): OperationStatus<String> {
        TODO("Not yet implemented")
    }

    override suspend fun updateTrip(trip: Trip): OperationStatus<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getAdmin(uId: String): OperationStatus<MatAdmin> {
        if(networkError){
            if (testAdmin == null){
                return OperationStatus.Error("Errot")
            }else{
                return OperationStatus.Success(testAdmin!!)
            }

        }else{
            return OperationStatus.Error("Network error")
        }
    }

    override suspend fun getDrivers(uId: String): OperationStatus<List<Driver>> {
        if(networkError){
            if (drivers.isNotEmpty()){
                return OperationStatus.Success(drivers)
            }else{
                return OperationStatus.Error("No driver found")
            }

        }else{
            return OperationStatus.Error("Network error")
        }
    }

    override suspend fun getBuses(uId: String): OperationStatus<List<Bus>> {
        if(networkError){
            if (buses.isNotEmpty()){
                return OperationStatus.Success(buses)
            }else{
                return OperationStatus.Error("No bus found")
            }

        }else{
            return OperationStatus.Error("Network error")
        }
    }

    override suspend fun getDriver(driverId: String): OperationStatus<Driver> {
        if(networkError){
            if (drivers.isNotEmpty()){
                for(driver in drivers){
                    if (driverId == driver.driverId){
                        return OperationStatus.Success(driver)
                    }
                }
                return OperationStatus.Error("No such driver found")
            }else{
                return OperationStatus.Error("No driver found")
            }

        }else{
            return OperationStatus.Error("Network error")
        }
    }

    override suspend fun getBus(plate: String): OperationStatus<Bus> {
        if(networkError){
            if (buses.isNotEmpty()){
                for(bus in buses){
                    if (plate == bus.plate){
                        return OperationStatus.Success(bus)
                    }
                }
                return OperationStatus.Error("No such bus found")
            }else{
                return OperationStatus.Error("No bus found")
            }
        }else{
            return OperationStatus.Error("Network error")
        }
    }

    override suspend fun getTrips(
        type: String,
        id: String,
        startDate: String,
        endDate: String
    ): OperationStatus<List<Trip>> {
        if(networkError){
            if (trips.isNotEmpty()){
                return OperationStatus.Success(trips)
            }
            else{
                return OperationStatus.Error("No record found")
            }
        }else{
            return OperationStatus.Error("Network error")
        }
    }

    override suspend fun getStats(
        type: String,
        id: String,
        startDate: String,
        endDate: String
    ): OperationStatus<List<Statistics>> {
        if(networkError){
            if (trips.isNotEmpty()){
                return OperationStatus.Success(stats)
            }
            else{
                return OperationStatus.Error("No record found")
            }
        }else{
            return OperationStatus.Error("Network error")
        }
    }

    override suspend fun getExpenses(
        type: String,
        id: String,
        startDate: String,
        endDate: String
    ): OperationStatus<List<Expense>> {
        if(networkError){
            if (expenses.isNotEmpty()){
                return OperationStatus.Success(expenses)
            }
            else{
                return OperationStatus.Error("No record found")
            }
        }else{
            return OperationStatus.Error("Network error")
        }
    }

    override suspend fun getDriverWithQuery(
        stringQuery: String,
        adminId: String
    ): OperationStatus<List<Driver>> {
        TODO("Not yet implemented")
    }

    override suspend fun getBusesWithQuery(
        stringQuery: String,
        adminId: String
    ): OperationStatus<List<Bus>> {
        TODO("Not yet implemented")
    }

    override suspend fun getIssues(
        type: String,
        id: String,
        startDate: String,
        endDate: String
    ): OperationStatus<List<Issue>> {
        if(networkError){
            if (issues.isNotEmpty()){
                return OperationStatus.Success(issues)
            }
            else{
                return OperationStatus.Error("No record found")
            }
        }else{
            return OperationStatus.Error("Network error")
        }
    }
}