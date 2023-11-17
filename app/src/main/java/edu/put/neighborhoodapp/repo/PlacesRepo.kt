package edu.put.neighborhoodapp.repo

import android.util.Log
import edu.put.neighborhoodapp.BuildConfig
import edu.put.neighborhoodapp.api.PlacesApi
import edu.put.neighborhoodapp.data.Location
import edu.put.neighborhoodapp.data.PlacesResponse
import edu.put.neighborhoodapp.db.PlacesDao
import edu.put.neighborhoodapp.db.PlacesDatabase
import edu.put.neighborhoodapp.db.data.LocationEntity
import edu.put.neighborhoodapp.db.data.PlaceEntity
import edu.put.neighborhoodapp.di.IoDispatcher
import edu.put.neighborhoodapp.util.PlacesApiResult
import edu.put.neighborhoodapp.util.wrapApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class PlacesRepo @Inject constructor(
    private val placesApi: PlacesApi,
    private val placesDao: PlacesDao,
    private val placesDb: PlacesDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getPlacesFromApi(location: String = "53.146572625154796, 17.598054268893183", radius: Int = 1000, query: String = "school"): List<PlaceEntity> = withContext(ioDispatcher) {

        val apiResult = wrapApiCall {
            placesApi.getPlaces(
                location = location,
                radius = radius,
                query = query,
                apiKey = "AIzaSyDxtJjgi6UfDQrRUQ89FsIFWBAQWAawUwQ"
            )
        }
        when(apiResult) {
            is PlacesApiResult.Exception -> { return@withContext emptyList() }
            is PlacesApiResult.Success -> {
                val locId = placesDao.insertLocation(LocationEntity(location = location))
                val placesEntities = apiResult.data.getAllPlaces(locId)
                placesDao.insertPlaces(placesEntities)
                return@withContext placesEntities
            }
        }
    }

    private fun PlacesResponse.getAllPlaces(locationId: Long) : List<PlaceEntity> =
        results.map {
            PlaceEntity(
                placeId = it.place_id,
                address = it.formatted_address,
                name = it.name,
                rating = it.rating,
                url = if (it.photos != null) {
                    it.photos[0].photo_reference } else { null },
                userRatingsTotal = it.user_ratings_total,
                parentLocationId = locationId
            )
        }

}