package edu.put.neighborhoodapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.put.neighborhoodapp.db.PlacesDao
import edu.put.neighborhoodapp.db.PlacesDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun appDatabase(@ApplicationContext context: Context) : PlacesDatabase = Room
        .databaseBuilder(context, PlacesDatabase::class.java, "MyDb")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun gamesDao(gamesDatabase: PlacesDatabase) : PlacesDao = gamesDatabase.gamesDao()


}