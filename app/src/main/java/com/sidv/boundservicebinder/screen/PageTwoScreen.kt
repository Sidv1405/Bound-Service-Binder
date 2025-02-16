package com.sidv.boundservicebinder.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sidv.boundservicebinder.service.rememberBoundService
import kotlinx.coroutines.delay

@SuppressLint("ContextCastToActivity")
@Composable
fun PageTwoScreen() {
    val activity = LocalContext.current as Activity
    val service = rememberBoundService(activity, "PageTwoScreen")
    var time by remember { mutableIntStateOf(0) }

    LaunchedEffect(service) {
        Log.d("PageTwoScreen", "LaunchedEffect triggered with service: $service")
        while (service == null) {
            Log.d("PageTwoScreen", "Waiting for service to connect...")
            delay(100)
        }
        while (true) {
            Log.d("PageTwoScreen", "Fetching elapsed time from service...")
            time = service.getElapsedTime()
            Log.d("PageTwoScreen", "Updated time: $time")
            delay(1000)
        }
    }


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Page Two", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Thời gian: $time giây", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
