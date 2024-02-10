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
            _uiState.update { it.copy(isLoading = true) }
            val currentState = _uiState.value
            val latLng = currentState.address?.let { geocoder.getLatLngFromAddress(it) }
            if (latLng != null) {
                val lat = latLng[0].toString()
                val lon = latLng[1].toString()
                val places = repository.getPlacesFromApi("$lat, $lon", query = query)
                val sortedPlacesList = places.sortedBy { it.distance }
                _uiState.update { it.copy(isLoading = false) }
                when(query) {
                    "Grocery store" -> _uiState.update { state ->
                        state.copy(storesData = sortedPlacesList)
                    }
                    "Gym" -> _uiState.update { state ->
                        state.copy(gymsData = sortedPlacesList)
                    }
                    "Bus stop" ->_uiState.update { state ->
                        state.copy(busesData = sortedPlacesList)
                    }
                    "Tram stop" ->_uiState.update { state ->
                        state.copy(tramsData = sortedPlacesList)
                    }
                    "Park" ->_uiState.update { state ->
                        state.copy(parksData = sortedPlacesList)
                    }
                    "Restaurant" ->_uiState.update { state ->
                        state.copy(restaurantsData = sortedPlacesList)
                    }
                }

            } else {
                eventChannel.send(Event.ShowToast("Pobranie danych dla podanego adresu nie udało się"))
            }
        }
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
        val address: String?,
        val isLoading: Boolean = false
    ) {
        companion object {
            val DEFAULT = State(
                storesData = null,
                gymsData = null,
                busesData = null,
                tramsData = null,
                parksData = null,
                restaurantsData = null,
                address = null,
                isLoading = false
            )
        }
    }

    sealed class Event {
        data class ShowToast(val message: String) : Event()
    }
}