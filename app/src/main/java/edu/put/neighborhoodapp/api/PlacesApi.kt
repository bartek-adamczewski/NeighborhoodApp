package edu.put.neighborhoodapp.api

import edu.put.neighborhoodapp.data.PlacesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApi {
    @GET("/maps/api/place/textsearch/json")
    suspend fun getPlaces(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("query") query: String,
        @Query("key") apiKey: String
    ): Response<PlacesResponse>
}