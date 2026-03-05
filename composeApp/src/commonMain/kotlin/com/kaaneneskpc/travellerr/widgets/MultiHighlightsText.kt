package com.kaaneneskpc.travellerr.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class TextHighlight(
    val word: String,
    val color: Color,
    val showUnderline: Boolean = true,
    val fontWeight: FontWeight = FontWeight.Normal
)

@Composable
fun MultiHighlightedText(
    modifier: Modifier,
    fullText: String,
    highlights: List<TextHighlight>,
    textStyle: TextStyle = TextStyle.Default,
    fontSize: TextUnit = 33.sp,
    fontColor: Color = Color.Black,
    thickness: Dp = 6.dp,
    peakHeight: Dp = 10.dp,
) {

    data class HighlightPosition(
        val startIndex: Int,
        val endIndex: Int,
        val highlight: TextHighlight
    )

    val highlightedPositions = remember(fullText, highlights) {
        highlights.mapNotNull {
            val start = fullText.indexOf(it.word)
            if (start != -1) {
                HighlightPosition(
                    startIndex = start,
                    endIndex = start + it.word.length,
                    highlight = it
                )
            } else {
                null
            }
        }.sortedBy { it.startIndex }
    }

    val annotatedString = buildAnnotatedString {
        var currentIndex = 0
        highlightedPositions.forEach {
            if (currentIndex < it.startIndex) {
                withStyle(
                    SpanStyle(
                        color = fontColor,
                        fontSize = fontSize,
                        fontWeight = FontWeight.Normal
                    )
                ) {
                    append(fullText.substring(currentIndex, it.startIndex))
                }
            }
            withStyle(
                SpanStyle(
                    color = it.highlight.color,
                    fontSize = fontSize,
                    fontWeight = it.highlight.fontWeight,
                )
            ) {
                append(fullText.substring(it.startIndex, it.endIndex))
            }
            currentIndex = it.endIndex
        }

        if (currentIndex < fullText.length) {
            withStyle(
                SpanStyle(
                    color = fontColor,
                    fontSize = fontSize,
                    fontWeight = FontWeight.Normal
                )
            ) {
                append(fullText.substring(currentIndex))
            }
        }

    }

    val textMeasurer = rememberTextMeasurer()
    val textLayoutResults = remember {
        textMeasurer.measure(annotatedString, style = textStyle)
    }

    val underlineData = remember(textLayoutResults, highlightedPositions) {
        highlightedPositions.filter { it.highlight.showUnderline }.mapNotNull { position ->
            try {
                val startBox = textLayoutResults.getBoundingBox(position.startIndex)
                val endBox = textLayoutResults.getBoundingBox(position.endIndex - 1)
                Pair(
                    Rect(
                        left = startBox.left,
                        top = startBox.bottom,
                        right = endBox.right,
                        bottom = startBox.bottom
                    ),
                    position.highlight.color
                )

            } catch (ex: Exception) {
                ex.printStackTrace()
                null
            }
        }


    }

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(annotatedString, style = textStyle)
        Canvas(modifier = Modifier.matchParentSize()) {
            underlineData.forEach { (boundingBox, color) ->

                if (boundingBox != Rect.Zero) {
                    val baseY = boundingBox.bottom + 2.dp.toPx()
                    val startX = boundingBox.left
                    val endX = boundingBox.right
                    val peakHeight = peakHeight.toPx()
                    val thickness = thickness.toPx()

                    val path = Path().apply {
                        moveTo(startX, baseY)

                        quadraticTo(
                            x1 = (startX + endX) / 2,
                            y1 = baseY - peakHeight - thickness / 2,
                            x2 = endX,
                            y2 = baseY
                        )

                        quadraticTo(
                            x1 = (startX + endX) / 2,
                            y1 = baseY - peakHeight + thickness / 2,
                            x2 = startX,
                            y2 = baseY
                        )

                    }

                    drawPath(
                        path = path,
                        color = color,
                        style = Fill
                    )

                }
            }

        }

    }


}

@Composable
@Preview(showBackground = true)
fun MultiHighlightedTextPreview() {
    Scaffold {

    }
    MultiHighlightedText(
        fullText = "Explore the \nbeautiful world!",
        highlights = listOf(

            TextHighlight(
                word = "world!",
                color = Color.Magenta,
                showUnderline = true,
                fontWeight = FontWeight.Bold
            ),
            TextHighlight(
                word = "beautiful",
                color = Color.Black,
                showUnderline = false,
                fontWeight = FontWeight.Bold
            )
        ),
        modifier = Modifier
    )
}