package com.example.hltv.ui.common

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
/**
 * @param maxLines is the max lines before scaling down text
 * @param maxFontSize is the max font size, in case the text doesn't need to downscale. Default is Integer.MAX_VALUE.sp
 */
@Composable
fun ResizingText(
    modifier: Modifier = Modifier,
    text: AnnotatedString = AnnotatedString("INSERT TEXT"),
    color: Color = Color.Unspecified,
    maxFontSize: TextUnit = Integer.MAX_VALUE.sp,
    fontStyle: FontStyle? = FontStyle.Normal,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = TextAlign.Center,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    style: TextStyle = LocalTextStyle.current
) {


    var localMaxLines = maxLines
    if (!(text.text.contains(' ') || text.text.contains('\n'))) {
        localMaxLines = 1
    }


    val longestWordLength = text.split(" ").maxOf { it.length }

    val textSize = remember { mutableStateOf(maxFontSize) }
    val readyToDraw = remember { mutableStateOf(false) }
    val availableWidth = remember { mutableIntStateOf(0) }

    var targetTextSize =
        remember { (availableWidth.intValue / longestWordLength).coerceAtMost(maxFontSize.value.toInt()) }

    Text(

        text = buildAnnotatedString {
            withStyle(ParagraphStyle(lineHeight = textSize.value * 0.75)) {
                append(
                    AnnotatedString( //The thing breaks at the word "championships" so we do this jank and it fixes it. Scuffed? Yes. Hotel? Trivago
                        text = if ((text.split(" ").maxOf { it.length }) > 8) text.text.replace(
                            ' ',
                            '\n'
                        ) else text.text,
                        spanStyles = text.spanStyles,
                        paragraphStyles = text.paragraphStyles
                    )
                )
            }
        },
        color = color,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        inlineContent = inlineContent,
        style = style,
        fontSize = textSize.value,

        onTextLayout = { textLayoutResult: TextLayoutResult ->

            if (textLayoutResult.lineCount > localMaxLines || textLayoutResult.didOverflowWidth) {
                textSize.value = (textSize.value * 0.9)
            } else if (text.text != "NULL") {
                readyToDraw.value = true
            }
        },
        modifier = modifier.drawWithContent {
            if (readyToDraw.value) {
                drawContent()
            }
        }
    )
}