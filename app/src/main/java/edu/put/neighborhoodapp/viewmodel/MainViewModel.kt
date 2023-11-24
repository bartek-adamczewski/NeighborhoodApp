package edu.put.neighborhoodapp.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import edu.put.neighborhoodapp.db.data.DistanceEntity
import edu.put.neighborhoodapp.db.data.PlaceEntity
import edu.put.neighborhoodapp.di.GeocoderInterface
import edu.put.neighborhoodapp.di.GeocoderModule
import edu.put.neighborhoodapp.fragments.FragmentType
import edu.put.neighborhoodapp.repo.PlacesRepo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: PlacesRepo,
    private val geocoder: GeocoderInterface
) : ViewModel() {

    private val _uiState = MutableStateFlow(State.DEFAULT)
    val uiState: Flow<State> = _uiState
    private val eventChannel = Channel<Event>(Channel.CONFLATED)
    val events = eventChannel.receiveAsFlow()

    fun getData(query: String) {
        viewModelScope.launch {
            val currentState = _uiState.value
            val latLng = currentState.address?.let { geocoder.getLatLngFromAddress(it) }
            if (latLng != null) {
                val lat = latLng[0].toString()
                val lon = latLng[1].toString()
                val places = repository.getPlacesFromApi("$lat, $lon", query = query)

                when(query) {
                    "Grocery store" -> _uiState.update { state ->
                        state.copy(storesData = places)
                    }
                    "Gym" -> _uiState.update { state ->
                        state.copy(gymsData = places)
                    }
                    "Bus stop" ->_uiState.update { state ->
                        state.copy(busesData = places)
                    }
                    "Tram stop" ->_uiState.update { state ->
                        state.copy(tramsData = places)
                    }
                    "Park" ->_uiState.update { state ->
                        state.copy(parksData = places)
                    }
                    "Restaurant" ->_uiState.update { state ->
                        state.copy(restaurantsData = places)
                    }
                }

                val destinations = mutableListOf<PlaceEntity>()
                //for (place in places) {
                //    destinations.add(place.address)
                //}

            } else {
                eventChannel.send(Event.ShowToast("Pobranie danych dla podanego adresu nie udało się"))
            }



            //val distances = repository.getDistance(currentState.address.toString(), destinationsAddresses)


            //var i = 0
            //for(distance in distances) {
            //    places[i].distance = distance.distance
            //    places[i].time = distance.time
             //   i++
            //}
        }
    }

    fun getDistances(query: String, ) {

    }

    fun updateAddress(address: String) {
        _uiState.update { state ->
            state.copy(address = address.toString())
        }
    }

    data class State(
        val storesData: List<PlaceEntity>?,
        val gymsData: List<PlaceEntity>?,
        val busesData: List<PlaceEntity>?,
        val tramsData: List<PlaceEntity>?,
        val parksData: List<PlaceEntity>?,
        val restaurantsData: List<PlaceEntity>?,
        val address: String?
    ) {
        companion object {
            val DEFAULT = State(
                storesData = null,
                gymsData = null,
                busesData = null,
                tramsData = null,
                parksData = null,
                restaurantsData = null,
                address = null
            )
        }
    }

    sealed class Event {
        data class ShowToast(val message: String) : Event()
    }
}