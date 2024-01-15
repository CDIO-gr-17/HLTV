package com.example.hltv.ui.common

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun showToast(message: String) {
    val context = LocalContext.current
    Toast.makeText(context, message,Toast.LENGTH_SHORT).show()
}