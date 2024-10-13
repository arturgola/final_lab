package com.example.final_lab2

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.final_lab2.ui.theme.Final_lab2Theme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MinisterViewModel
    private lateinit var apiService: ApiService
    private lateinit var db: AppDatabase
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = AppDatabase.getDatabase(this)
        val commentDao = db.commentDao()
        val repository = CommentRepository(commentDao)
        viewModel = MinisterViewModel(repository)

        apiService = Retrofit.Builder()
            .baseUrl("https://users.metropolia.fi/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        Log.d(TAG, "Fetching ministers from API...")
        viewModel.fetchMinisters(apiService)

        setContent {
            Final_lab2Theme {
                MinisterScreen(viewModel)
            }
        }

        // Start observing ministers to fetch comments for the first minister
        lifecycleScope.launch {
            viewModel.ministers.collect { ministers ->
                if (ministers.isNotEmpty()) {
                    viewModel.fetchComments(ministers[0].hetekaId)
                }
            }
        }
    }
}

@Composable
fun MinisterScreen(viewModel: MinisterViewModel) {
    var selectedMinisterIndex by remember { mutableStateOf(0) }
    var commentText by remember { mutableStateOf(TextFieldValue()) }
    var rating by remember { mutableStateOf(0) }

    val ministers by viewModel.ministers.collectAsState() // Collect state from ViewModel
    val comments by viewModel.comments.collectAsState() // Collect comments from ViewModel

    // Fetch comments for the selected minister whenever selectedMinisterIndex changes
    LaunchedEffect(selectedMinisterIndex) {
        if (ministers.isNotEmpty()) {
            val selectedMinister = ministers[selectedMinisterIndex]
            viewModel.fetchComments(selectedMinister.hetekaId)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (ministers.isNotEmpty()) {
            val minister = ministers[selectedMinisterIndex]
            val imageUrl = "https://avoindata.eduskunta.fi/${minister.pictureUrl}"

            Log.d("MinisterScreen", "Loading image from URL: $imageUrl")

            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = "Minister Picture",
                modifier = Modifier.size(128.dp)
            )

            Text(
                text = "${minister.firstname} ${minister.lastname}",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(text = "Party: ${minister.party}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                selectedMinisterIndex = (selectedMinisterIndex + 1) % ministers.size
                // Fetch comments for the new minister is handled by LaunchedEffect
            }) {
                Text(text = "Next Minister")
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = commentText,
                onValueChange = { commentText = it },
                label = { Text("Comment") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = rating.toString(),
                onValueChange = { rating = it.toIntOrNull() ?: 0 },
                label = { Text("Rating") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                viewModel.addComment(minister.hetekaId, rating, commentText.text)
                commentText = TextFieldValue("") // Clear the comment text
                rating = 0 // Reset rating to 0
                Log.d("MinisterScreen", "Adding comment: ${commentText.text}, rating: $rating")
            }) {
                Text(text = "Add Comment")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Comments:", style = MaterialTheme.typography.headlineSmall)
            for (comment in comments) {
                Text(text = "Rating: ${comment.rating}, Comment: ${comment.comment}", style = MaterialTheme.typography.bodySmall)
            }
        } else {
            Text("No ministers available.", style = MaterialTheme.typography.bodyMedium)
        }
    }
}