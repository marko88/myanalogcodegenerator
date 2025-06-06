package myanalogcodegenerator.ui.components.canvas.node.style

import androidx.compose.ui.graphics.Color
import domain.model.ArchitectureLayer
import ui.components.canvas.NodeSelectionState

object NodeBoxStyles {

    fun labelColorForLayer(layer: ArchitectureLayer): Color = when (layer) {
        ArchitectureLayer.PRESENTATION -> Color(0xFF7C4DFF) // Purple 400
        ArchitectureLayer.DOMAIN -> Color(0xFF00BCD4)       // Cyan 400
        ArchitectureLayer.DATA -> Color(0xFFFFC107)         // Amber 400
        ArchitectureLayer.OTHER -> Color(0xFF90A4AE)        // Blue Gray
    }

    fun labelTextColorForLayer(layer: ArchitectureLayer): Color = when (layer) {
        ArchitectureLayer.PRESENTATION -> Color.White
        ArchitectureLayer.DOMAIN -> Color.Black
        ArchitectureLayer.DATA -> Color.Black
        ArchitectureLayer.OTHER -> Color.Black
    }

    data class ItemStyle(
        val backgroundColor: Color,
        val borderColor: Color,
        val alpha: Float,
        val textColor: Color
    )

    fun fromSelection(state: NodeSelectionState): ItemStyle = when (state) {
        NodeSelectionState.DEFAULT -> ItemStyle(
            backgroundColor = Color(0xFF2A2C3A),
            borderColor = Color(0xFF4A4A4A),
            alpha = 1f,
            textColor = Color(0xFFE0E0E0)
        )

        NodeSelectionState.SELECTED -> ItemStyle(
            backgroundColor = Color(0xFF37474F),
            borderColor = Color(0xFF00E5FF),
            alpha = 1f,
            textColor = Color.White
        )

        NodeSelectionState.HIGHLIGHTED -> ItemStyle(
            backgroundColor = Color(0xFF3E3D5C),
            borderColor = Color(0xFFFFCA28),
            alpha = 1f,
            textColor = Color(0xFFFFF8E1)
        )

        NodeSelectionState.DISABLED -> ItemStyle(
            backgroundColor = Color(0xFF1C1C1C),
            borderColor = Color(0xFF333333),
            alpha = 0.4f,
            textColor = Color(0xFF777777)
        )
    }
}
