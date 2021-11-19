package com.example.matatumanageradmin.data

import com.example.util.*
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MatManagerApi {


    @POST(CREATE_MAT_ADMIN)
    suspend fun createMatAdmin(@Body admin: MatAdmin): Response<JsonObject>

    @POST(CREATE_DRIVERS)
    suspend fun createDriver(@Body driver: Driver): Response<JsonObject>

    @POST(CREATE_BUSES)
    suspend fun createBus(@Body bus: Bus): Response<JsonObject>

    @POST(CREATE_TRIPS)
    suspend fun createTrip(@Body trip: Trip): Response<JsonObject>

    @POST(CREATE_EXPENSES)
    suspend fun createExpense(@Body expense: Expense): Response<String>


    @POST(UPDATE_BUSES)
    suspend fun updateBus(@Body bus: Bus): Response<String>

    @POST(UPDATE_TRIPS)
    suspend fun updateTrip(@Body trip: Trip): Response<String>


    @GET(MAT_ADMIN)
    suspend fun getAdmin(
        @Query("adminId") adminId: String

    ): Response<MatAdmin>

    @GET(DRIVERS)
    suspend fun getDrivers(
        @Query("type") type: String,
        @Query("id") adminId: String,
        @Query("string_query") stringQuery: String

    ): Response<List<Driver>>

    @GET(BUSES)
    suspend fun getBus(
        @Query("type") type: String,
        @Query("id") adminId: String,
        @Query("string_query") stringQuery: String

    ): Response<List<Bus>>

    @GET(TRIPS)
    suspend fun getTrips(
        @Query("type") type: String,
        @Query("id") id: String,
        @Query ("startDate") startDate: String,
        @Query("endDate") endDate: String

    ): Response<List<Trip>>

    @GET(STATS)
    suspend fun getStats(
        @Query("type") type: String,
        @Query("id") id: String,
        @Query ("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Response<List<Statistics>>

    @GET(EXPENSES)
    suspend fun getExpenses(
        @Query("type") type: String,
        @Query("id") id: String,
        @Query ("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Response<List<Expense>>


}