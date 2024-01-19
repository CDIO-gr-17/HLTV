package com.example.hltv.ui.common

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun showToast(message: String, time : Int = Toast.LENGTH_SHORT) {
    val context = LocalContext.current
    Toast.makeText(context, message, time).show()
}