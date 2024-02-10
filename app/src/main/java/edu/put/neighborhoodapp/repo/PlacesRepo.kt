package edu.put.neighborhoodapp.repo

import edu.put.neighborhoodapp.BuildConfig
import edu.put.neighborhoodapp.api.DistanceApi
import edu.put.neighborhoodapp.api.PlacesApi
import edu.put.neighborhoodapp.data.PlacesResponse
import edu.put.neighborhoodapp.data.distance.DistanceResponse
import edu.put.neighborhoodapp.db.PlacesDao
import edu.put.neighborhoodapp.db.PlacesDatabase
import edu.put.neighborhoodapp.db.data.DistanceEntity
import edu.put.neighborhoodapp.db.data.LocationEntity
import edu.put.neighborhoodapp.db.data.PlaceEntity
import edu.put.neighborhoodapp.di.IoDispatcher
import edu.put.neighborhoodapp.util.PlacesApiResult
import edu.put.neighborhoodapp.util.wrapApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlacesRepo @Inject constructor(
    private val placesApi: PlacesApi,
    private val distanceApi: DistanceApi,
    private val placesDao: PlacesDao,
    private val placesDb: PlacesDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getPlacesFromApi(location: String, radius: Int = 1000, query: String): List<PlaceEntity> = withContext(ioDispatcher) {

        val apiResult = wrapApiCall {
            placesApi.getPlaces(
                location = location,
                radius = radius,
                query = query,
                apiKey = BuildConfig.PLACES_API_KEY
            )
        }

        when(apiResult) {
            is PlacesApiResult.Exception -> return@withContext emptyList()
            is PlacesApiResult.Success -> {
                val locId = placesDao.insertLocation(LocationEntity(location = location))
                var placesEntities = apiResult.data.getAllPlaces(locId)

                val destinations = placesEntities.map { it.address }.toList()
                val distancesResult = getDistance(location, destinations)

                placesEntities = placesEntities.mapIndexed { index, placeEntity ->
                    placeEntity.apply {
                        distance = distancesResult[index].distance
                        time = distancesResult[index].time
                    }
                }

                placesDao.insertPlaces(placesEntities)
                return@withContext placesEntities
            }
        }
    }

    suspend fun getDistance(origin: String, destinations: List<String>): List<DistanceEntity> {
        val destinationsString = destinations.joinToString("|")
        val apiResult = distanceApi.getDistance(
            origin = origin,
            destinations = destinationsString,
            apiKey = BuildConfig.DISTANCE_API_KEY,
            mode = "walking",
            units = "metric"
        )
        return apiResult.getDistances(origin, destinations)
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
                photoHeight = if(it.photos != null) {
                    it.photos[0].height } else { null },
                photoWidth = if(it.photos != null) {
                    it.photos[0].width } else { null },
                userRatingsTotal = it.user_ratings_total,
                parentLocationId = locationId,
                distance = 1,
                time = 1
            )
        }

    private fun DistanceResponse.getDistances(origin: String, destinations: List<String>) : List<DistanceEntity> {
        var i = 0
        return rows.flatMap {
            it.elements.map { e->
                DistanceEntity(
                    distance = e.distance.value,
                    time = e.duration.value,
                    placeMain = origin,
                    placeName = destinations[i++]
                )
            }

        }
    }

}