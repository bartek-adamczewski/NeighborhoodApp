package edu.put.neighborhoodapp.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import edu.put.neighborhoodapp.di.GeocoderInterface
import edu.put.neighborhoodapp.di.GeocoderModule
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

    fun getData(address: String) {
        viewModelScope.launch {
            val latLng = geocoder.getLatLngFromAddress(address)
            if (latLng != null) {
                eventChannel.send(Event.ShowToast(latLng.toString()))
                //val places = repository.getPlacesFromApi(location = location)
                //_uiState.update { state ->
                //    state.copy(data = places.toString())
                //}
            } else {
                eventChannel.send(Event.ShowToast("Pobranie danych dla podanego adresu nie udało się"))
            }
        }
    }

    data class State(
        val data: String?,
        //val places: List<Int>
    ) {
        companion object {
            val DEFAULT = State(
                data = null
            )
        }
    }

    sealed class Event {
        data class ShowToast(val message: String) : Event()
    }
}