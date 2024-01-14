package com.example.hltv.data.local

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

object PrefDataStore {
    getApplication
    val instance : PrefDataKeyValueStore by lazy {
        PrefDataKeyValueStore(context = LocalContext.current)
    }


}