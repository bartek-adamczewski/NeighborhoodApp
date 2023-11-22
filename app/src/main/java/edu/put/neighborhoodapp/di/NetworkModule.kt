package edu.put.neighborhoodapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.put.neighborhoodapp.api.DistanceApi
import edu.put.neighborhoodapp.api.PlacesApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module // klasa to moduł - zapewnia definicje funkcji tworzących instancje różnych klas
@InstallIn(SingletonComponent::class) // określenie zasięgu życia instancji - jedno odpalenie jedna instancja
class NetworkModule {

    @Provides
    fun retrofit(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl("https://maps.googleapis.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun gamesApi(retrofit: Retrofit): PlacesApi {
        return retrofit.create(PlacesApi::class.java)
    }

    fun distanceApi(retrofit: Retrofit): DistanceApi {
        return retrofit.create(DistanceApi::class.java)
    }

}