package com.example.hltv.ui.screens.singleMatch

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.hltv.data.remote.APIResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SingleMatchViewModel: ViewModel() {
    private val _matches = MutableStateFlow<List<APIResponse.EventsWrapper>>(emptyList())
    val matches =_matches.asStateFlow()
    val matchResult =  mutableStateListOf(" ", " ", " ")




 init{
     CoroutineScope(Dispatchers.IO).launch {

     }



 }

}