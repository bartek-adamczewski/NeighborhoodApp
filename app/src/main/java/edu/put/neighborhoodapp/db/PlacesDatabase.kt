package edu.put.neighborhoodapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.put.neighborhoodapp.db.data.LocationEntity
import edu.put.neighborhoodapp.db.data.PlaceEntity

@Database(entities = [PlaceEntity::class, LocationEntity::class], version = 3, exportSchema = false)
abstract class PlacesDatabase : RoomDatabase() {
    abstract fun gamesDao() : PlacesDao
}