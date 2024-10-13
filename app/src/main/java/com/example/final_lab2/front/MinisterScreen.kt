package com.example.final_lab2.front

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.final_lab2.viewmodel.MinisterViewModel

// Date: 13.10.2024
// Name: Artur Golavskiy 2215446
// Description: The MinisterScreen composable displays minister details, allows navigation between them,
// and enables users to submit comments and ratings.

@Composable
fun MinisterScreen(viewModel: MinisterViewModel) {
    var selectedMinisterIndex by remember { mutableStateOf(0) }
    var commentText by remember { mutableStateOf(TextFieldValue()) }
    var rating by remember { mutableStateOf(0) }

    val ministers by viewModel.ministers.collectAsState()
    val comments by viewModel.comments.collectAsState()

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
                commentText = TextFieldValue("")
                rating = 0
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