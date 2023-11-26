package com.example.hltv.ui.screens.singleMatch

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SingleMatchScreen(viewModel: SingleMatchViewModel){
val R = MaterialTheme
    val singlematches by viewModel.matches.collectAsState()
//teamCard(modifier = Modifier, R = R, text1 = singlematches.toString(), text2 = "test")
}

@Preview(showBackground = true)
@Composable
fun SingleMatchScreenPreview() {
    SingleMatchScreen(viewModel = SingleMatchViewModel())

}