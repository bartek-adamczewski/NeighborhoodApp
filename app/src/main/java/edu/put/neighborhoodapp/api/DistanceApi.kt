package edu.put.neighborhoodapp.api

import edu.put.neighborhoodapp.data.PlacesResponse
import edu.put.neighborhoodapp.data.distance.DistanceResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DistanceApi {
    @GET("/maps/api/distancematrix/json")
    suspend fun getDistance(
        @Query("origins") origin: String,
        @Query("destinations") destinations: List<String>,
        @Query("key") apiKey: String,
        @Query("mode") mode: String,
        @Query("units") units: String
    ): DistanceResponse
}