package myanalogcodegenerator.ui.components.canvas.node.style

import androidx.compose.ui.graphics.Color
import ui.components.canvas.NodeSelectionState

object NodeItemStyles {

    data class ItemStyle(
        val textColor: Color,
        val backgroundColor: Color
    )

    private val defaultAttributeStyle = ItemStyle(
        textColor = Color.White,
        backgroundColor = Color.Transparent
    )

    private val selectedAttributeStyle = ItemStyle(
        textColor = Color.Black,
        backgroundColor = Color(0xFFFAFA90)
    )

    private val defaultMethodStyle = ItemStyle(
        textColor = Color(0xFFB8FFB8),
        backgroundColor = Color.Transparent
    )

    private val selectedMethodStyle = ItemStyle(
        textColor = Color.Black,
        backgroundColor = Color(0xFF90EE90)
    )

    fun fromSelection(selectionState: NodeSelectionState): ItemStyle = when (selectionState) {
        NodeSelectionState.DEFAULT -> ItemStyle(
            backgroundColor = Color(0xFF23192D),
            textColor = Color.White
        )

        NodeSelectionState.SELECTED -> ItemStyle(
            backgroundColor = Color(0xFF342B3D),
            textColor = Color(0xFFE5FFF9)
        )

        NodeSelectionState.HIGHLIGHTED -> ItemStyle(
            backgroundColor = Color(0xFF2D2236),
            textColor = Color(0xFFFFE082)
        )

        NodeSelectionState.DISABLED -> ItemStyle(
            backgroundColor = Color(0xFF1C1C1C),
            textColor = Color.Gray
        )
    }

    fun attributeStyle(selected: Boolean): ItemStyle =
        if (selected) selectedAttributeStyle else defaultAttributeStyle

    fun methodStyle(selected: Boolean): ItemStyle =
        if (selected) selectedMethodStyle else defaultMethodStyle
}