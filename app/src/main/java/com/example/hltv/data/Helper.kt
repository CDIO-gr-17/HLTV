package com.example.hltv.data

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.example.hltv.R

@Composable
fun getFlagFromCountryCode(countryCode: String?): AsyncImagePainter {
    if ( countryCode != null) {
        Log.i("getFlagFromCountryCode", "Getting flag with country code: " + countryCode)
        return rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .decoderFactory(SvgDecoder.Factory()) //TODO HLTV-144 Crash may be caused here although doesn't seem to be a bitmap?
                .data("https://flagcdn.com/${countryCode.lowercase()}.svg")
                .size(Size.ORIGINAL) // Set the target size to load the image at.
                .size(200) //This should fix HLTV-144
                .build()
        )
    }
    else {
        return rememberAsyncImagePainter(R.drawable.world_flag)
    }


}
fun capitalizeFirstLetter(input: String): String {
    return if (input.isNotEmpty()) {
        input.substring(0, 1).uppercase() + input.substring(1)
    } else {
        input
    }
}