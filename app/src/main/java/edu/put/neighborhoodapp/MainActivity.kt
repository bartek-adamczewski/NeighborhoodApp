package edu.put.neighborhoodapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import edu.put.neighborhoodapp.viewmodel.MainViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private val mainText by lazy {
        findViewById<TextView>(R.id.mainText)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.uiState.onEach { state ->
            mainText.text = state.data ?: "No data"
        }.launchIn(lifecycleScope)
    }
}