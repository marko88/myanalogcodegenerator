package myanalogcodegenerator.ui.components.layout


import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.sp
import domain.model.ArchitectureLayer
import domain.model.ArchitectureNode

fun DrawScope.drawConnections(nodes: List<ArchitectureNode>) {
    val nodeMap = nodes.associateBy { it.id }

    nodes.forEach { node ->
        node.dependencies.forEach { dependency ->
            val target = nodeMap[dependency.targetId] ?: return@forEach
            drawLine(
                color = Color.LightGray,
                start = node.position,
                end = target.position,
                strokeWidth = 2f
            )
        }
    }
}

fun DrawScope.drawNodeBoxes(
    nodes: List<ArchitectureNode>,
    textMeasurer: TextMeasurer
) {
    val boxSize = 120f
    val padding = 8f

    nodes.forEach { node ->
        val color = when (node.layer) {
            ArchitectureLayer.PRESENTATION -> Color(0xFF81D4FA)
            ArchitectureLayer.DOMAIN -> Color(0xFFAED581)
            ArchitectureLayer.DATA -> Color(0xFFFFAB91)
            else -> Color.Gray
        }

        // Draw box
        drawRect(
            color = color,
            topLeft = node.position,
            size = androidx.compose.ui.geometry.Size(boxSize, boxSize)
        )

        // Draw node name
        val textPosition = Offset(
            node.position.x + padding,
            node.position.y + padding
        )

        val textLayoutResult = textMeasurer.measure(
            text = AnnotatedString(node.name),
            style = TextStyle(fontSize = 14.sp, color = Color.Black)
        )

        drawText(
            textLayoutResult = textLayoutResult,
            topLeft = textPosition
        )
    }
}
