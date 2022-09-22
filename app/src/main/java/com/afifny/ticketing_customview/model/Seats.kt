package com.afifny.ticketing_customview.model

data class Seats (
    val id: Int,
    var x: Float? = 0f,
    var y: Float? = 0f,
    var name: String,
    var isBooked: Boolean
)