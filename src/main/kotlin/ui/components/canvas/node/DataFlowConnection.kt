package myanalogcodegenerator.ui.components.canvas.node

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import domain.model.DataFlowSemantics

@Composable
fun DataFlowConnectionView(
    fromCoord: LayoutCoordinates,
    toCoord: LayoutCoordinates,
    semantics: DataFlowSemantics,
    color: Color = Color(0xFFFFE082),
    strokeWidth: Dp = 1.5.dp
) {
    val density = LocalDensity.current

    // Pick visual style based on semantics
    val (_, _) = when (semantics) {
        DataFlowSemantics.Command -> Color.Red to PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        DataFlowSemantics.Event -> Color.Green to null
        DataFlowSemantics.State -> Color.Blue to PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f)
        DataFlowSemantics.Response -> Color.Magenta to null
        DataFlowSemantics.Stream -> Color.Cyan to PathEffect.dashPathEffect(floatArrayOf(2f, 6f), 0f)
        DataFlowSemantics.Request -> Color.Gray to PathEffect.dashPathEffect(floatArrayOf(3f, 6f), 0f)
        DataFlowSemantics.Binding -> Color.Gray to PathEffect.dashPathEffect(floatArrayOf(3f, 6f), 0f)
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        // Compute the center of the "from" and "to" pins
        val fromCenter = fromCoord.localToRoot(
            Offset(
                x = fromCoord.size.width / 2f,
                y = fromCoord.size.height / 2f
            )
        )

        val toCenter = toCoord.localToRoot(
            Offset(
                x = toCoord.size.width / 2f,
                y = toCoord.size.height / 2f
            )
        )

        drawLine(
            color = color,
            start = fromCenter,
            end = toCenter,
            strokeWidth = with(density) { strokeWidth.toPx() }
        )
    }
}