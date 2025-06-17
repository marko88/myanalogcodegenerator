package myanalogcodegenerator.ui.components.canvas.grid

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BlueprintGridBackground(
    scale: Float,
    offset: Offset,
    baseSpacing: Dp = 20.dp,
    lineColor: Color = Color(0xFF2B2B2B),
    thickerLineColor: Color = Color(0xFF3A3A3A)
) {
    val density = LocalDensity.current
    Canvas(modifier = Modifier.fillMaxSize()) {
        val spacingPx = with(density) { baseSpacing.toPx() } * scale

        // The offset needs to be reversed because graphicsLayer moves the content
        val adjustedOffset = Offset(-offset.x, -offset.y)

        val startX = adjustedOffset.x % spacingPx
        val startY = adjustedOffset.y % spacingPx

        val cols = (size.width / spacingPx).toInt() + 2
        val rows = (size.height / spacingPx).toInt() + 2

        // Vertical lines
        for (i in -1..cols) {
            val x = i * spacingPx - startX
            val isThick = ((x + offset.x) / spacingPx).toInt() % 5 == 0
            drawLine(
                color = if (isThick) thickerLineColor else lineColor,
                start = Offset(x, 0f),
                end = Offset(x, size.height)
            )
        }

        // Horizontal lines
        for (i in -1..rows) {
            val y = i * spacingPx - startY
            val isThick = ((y + offset.y) / spacingPx).toInt() % 5 == 0
            drawLine(
                color = if (isThick) thickerLineColor else lineColor,
                start = Offset(0f, y),
                end = Offset(size.width, y)
            )
        }
    }
}




