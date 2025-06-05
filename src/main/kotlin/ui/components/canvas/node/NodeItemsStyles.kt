package myanalogcodegenerator.ui.components.canvas.node

import androidx.compose.ui.graphics.Color

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

    fun attributeStyle(selected: Boolean): ItemStyle =
        if (selected) selectedAttributeStyle else defaultAttributeStyle

    fun methodStyle(selected: Boolean): ItemStyle =
        if (selected) selectedMethodStyle else defaultMethodStyle
}