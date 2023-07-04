package edu.put.neighborhoodapp.repo

import android.util.Log
import edu.put.neighborhoodapp.api.PlacesApi
import edu.put.neighborhoodapp.data.PlacesResponse
import edu.put.neighborhoodapp.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class PlacesRepo @Inject constructor(
    private val placesApi: PlacesApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getPlacesFromApi(): PlacesResponse = withContext(ioDispatcher) {
        val resp = placesApi.getPlaces(location = "Sydney", radius = 10000, query = "restaurant", apiKey = "AIzaSyDxtJjgi6UfDQrRUQ89FsIFWBAQWAawUwQ")
        return@withContext resp
    }
}