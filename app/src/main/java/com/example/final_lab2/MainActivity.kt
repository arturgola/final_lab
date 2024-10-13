package com.example.final_lab2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.example.final_lab2.front.MinisterScreen
import com.example.final_lab2.ui.theme.Final_lab2Theme
import com.example.final_lab2.viewmodel.MinisterViewModel
import com.example.final_lab2.viewmodel.ViewModelProvider
import kotlinx.coroutines.launch

// Date: 13.10.2024
// Name: Artur Golavskiy 2215446
// Description: The MainActivity initializes the MinisterViewModel, sets the content to the MinisterScreen,
// and collects ministers to fetch comments for the first minister.

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MinisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider.provideMinisterViewModel(this)

        setContent {
            Final_lab2Theme {
                MinisterScreen(viewModel)
            }
        }

        lifecycleScope.launch {
            viewModel.ministers.collect { ministers ->
                if (ministers.isNotEmpty()) {
                    viewModel.fetchComments(ministers[0].hetekaId)
                }
            }
        }
    }
}