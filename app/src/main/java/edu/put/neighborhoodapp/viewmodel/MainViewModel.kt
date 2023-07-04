package edu.put.neighborhoodapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.put.neighborhoodapp.repo.PlacesRepo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: PlacesRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(State.DEFAULT)
    val uiState: Flow<State> = _uiState
    private val eventChannel = Channel<Event>(Channel.CONFLATED)
    val events = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val places = repository.getPlacesFromApi()
            _uiState.update { state ->
                state.copy(data = places.toString())
            }
        }
    }

    data class State(
        val data: String?
    ) {
        companion object {
            val DEFAULT = State(
                data = null
            )
        }
    }

    sealed class Event {
        object getData : Event()
    }
}