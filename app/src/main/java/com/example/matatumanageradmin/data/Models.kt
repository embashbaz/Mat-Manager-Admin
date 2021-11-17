package com.example.matatumanageradmin.data

//import kotlinx.serialization.Serializable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MatAdmin(
    @SerializedName("matAdminId") var matAdminId: String ="",
    @SerializedName("name") var name: String = "",
    @SerializedName("email") var email: String = "",
    @SerializedName("phoneNumber") var phoneNumber: Long = 0L,
    @SerializedName("address") var address: String = "",
    @SerializedName("licenseType") var licenseType: String = "",
    @SerializedName("licenseNumber")var licenseNumber: String = "",
    @SerializedName("licenseLink")var licenseLink: String = "",
    @SerializedName("status")var status: String = "",
    @SerializedName("comment")var comment: String = "",
    @SerializedName("dateCreated") var dateCreated: String = "",

    )

data class Driver(
    var driverId: String = "",
    var managerId: String = "",
    var cardId : String = "",
    var permitNumber: String = "",
    var pictureLink : String = "",
    var permitLink: String = "",
    var name: String = "",
    var email: String = "",
    var phoneNumber: Long = 0L,
    var address: String = "",
    var status: String = "",
    var comment: String = "",
    val dateCreated: String = ""

): Serializable

data class Bus(
    var plate: String = "",
    var managerId: String = "",
    var identifier: String = "",
    var carModel : String = "",
    var docLink: String = "",
    var pickupPoint: String = "",
    var picture: String = "",
    var pathPoints: String = "",
    var locationLat: Double = 0.0,
    var locationLng: Double = 0.0,
    var status: String = "",
    var comment: String = "",
    var dateCreated: String = ""

)

data class Trip(
    var tripId: String = "",
    var date: String = "",
    var busPlate: String = "",
    var driverId: String = "",
    var pathPoints: String = "",
    var pickupPoint: String = "",
    var moneyCollected: Double = 0.0,
    var timeStarted: String = "",
    var timeEnded: String = "",
    var tripStatus: String = "",
    var comment: String = ""
)

data class Statistics(
    var dayId: String = "",
    var busPlate: String = "",
    var driverId: String = "",
    var pathPoints: String = "",
    var locationLat: Double = 0.0,
    var locationLng: Double = 0.0,
    var timeStarted: String = "",
    var distance: Double = 0.0,
    var amount: Double = 0.0,
    var expense: Double = 0.0,
    var timeEnded: String = "",
    var maxSpeed: Double = 0.0,
    var comment: String = ""
)

data class Expense(
    var expenseId: String = "",
    var date: String = "",
    var status: String = "",
    var busPlate: String = "",
    var driverId: String = "",
    var amount: Double = 0.0,
    var reason: String = "",
    var comment: String = ""
)

data class Issue(
    var issueId: String = "",
    var date: String = "",
    var status: String = "",
    var busPlate: String = "",
    var driverId: String = "",
    var reason: String = "",
    var comment: String = ""

)
