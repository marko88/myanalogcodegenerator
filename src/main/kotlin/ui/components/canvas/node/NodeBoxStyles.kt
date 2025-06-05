package ui.components.canvas.node

import androidx.compose.ui.graphics.Color
import domain.model.ArchitectureLayer

object NodeBoxStyles {

    fun labelColorForLayer(layer: ArchitectureLayer): Color = when (layer) {
        ArchitectureLayer.PRESENTATION -> Color(0xFF81D4FA)
        ArchitectureLayer.DOMAIN -> Color(0xFFAED581)
        ArchitectureLayer.DATA -> Color(0xFFFFAB91)
        ArchitectureLayer.OTHER -> TODO()
    }

    data class ItemStyle(
        val backgroundColor: Color,
        val borderColor: Color,
        val alpha: Float,
        val labelColor: Color
    )

    fun fromSelection(selected: Boolean): ItemStyle = if (selected) {
        ItemStyle(
            backgroundColor = Color(0xFF2D2236),
            borderColor = Color(0xFFB83B5E),
            alpha = 1f,
            labelColor = Color(0xFF181926)
        )
    } else {
        ItemStyle(
            backgroundColor = Color(0xFF23192D),
            borderColor = Color(0xFF4A4A4A),
            alpha = 1f,
            labelColor = Color(0xFF181926)
        )
    }
}
