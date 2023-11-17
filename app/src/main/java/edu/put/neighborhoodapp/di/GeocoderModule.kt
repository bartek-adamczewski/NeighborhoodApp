package edu.put.neighborhoodapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.put.neighborhoodapp.di.GeocoderInterface
import edu.put.neighborhoodapp.di.GeocoderInterfaceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GeocoderModule {
    @Provides
    @Singleton
    fun provideGeocoderService(@ApplicationContext context: Context): GeocoderInterface {
        return GeocoderInterfaceImpl(context)
    }
}