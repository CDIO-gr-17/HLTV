package com.example.hltv.data

import android.graphics.Color.parseColor
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
    return if ( countryCode != null) {
        rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .decoderFactory(SvgDecoder.Factory())
                .data("https://flagcdn.com/${countryCode.lowercase()}.svg")
                .size(Size.ORIGINAL)
                .size(200)
                .build()
        )
    }
    else {
        rememberAsyncImagePainter(R.drawable.world_flag)
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
        "S" -> Color(parseColor("#F53C3C"))
        "A" -> Color(parseColor("#B12FC1"))
        "B" -> Color(parseColor("#4B69FE"))
        "C" -> Color(parseColor("#5E98D9"))
        //"D" -> Color(parseColor("#AFC4D8"))
        else -> Color(parseColor("#AFC4D8"))
    }
}

