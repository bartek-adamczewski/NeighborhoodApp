package edu.put.neighborhoodapp.db

import androidx.room.*
import dagger.Provides
import edu.put.neighborhoodapp.db.data.LocationEntity
import edu.put.neighborhoodapp.db.data.PlaceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlacesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaces(items: List<PlaceEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: LocationEntity): Long

    @Query("SELECT * FROM PlaceEntity WHERE parentLocationId = :parentLocationId")
    fun getPlacesByLocation(parentLocationId: Int) : Flow<List<PlaceEntity?>>

    @Query("SELECT * FROM LocationEntity")
    fun getLocations() : Flow<List<LocationEntity?>>


}