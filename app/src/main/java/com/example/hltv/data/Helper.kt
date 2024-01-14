package com.example.hltv.data

import android.graphics.Color.parseColor
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
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


fun getColorFromTier(input: String): Color {
    return when (input) {
        "S" -> Color(parseColor("#CAAC05"))
        "A" -> Color(parseColor("#EB4C4B"))
        "B" -> Color(parseColor("#B12FC1"))
        "C" -> Color(parseColor("#4B69FE"))
        "D" -> Color(parseColor("#5E98D9"))
        else -> Color(parseColor("#AFC4D8"))
    }
}

