package edu.put.neighborhoodapp.data.distance

data class DistanceResponse(
    val destination_addresses: List<String>,
    val origin_addresses: List<String>,
    val rows: List<Row>,
    val status: String
)