package ui.components.canvas

import androidx.compose.ui.graphics.Color

/**
 * Defines the interaction or UI state of a node, attribute, or method.
 */
enum class NodeSelectionState {
    DEFAULT,        // Normal appearance
    SELECTED,       // Currently selected
    HIGHLIGHTED,    // Highlighted for reference or flow
    DISABLED;       // Grayed out or inactive

    val backgroundColor: Color
        get() = when (this) {
            DEFAULT -> Color(0xFF23192D)
            SELECTED -> Color(0xFF342B3D)
            HIGHLIGHTED -> Color(0xFF2D2236)
            DISABLED -> Color(0xFF1C1C1C)
        }

    val borderColor: Color
        get() = when (this) {
            DEFAULT -> Color(0xFF4A4A4A)
            SELECTED -> Color(0xFF61F2E2)
            HIGHLIGHTED -> Color(0xFFB83B5E)
            DISABLED -> Color(0xFF333333)
        }

    val alpha: Float
        get() = when (this) {
            DEFAULT -> 1f
            SELECTED -> 1f
            HIGHLIGHTED -> 1f
            DISABLED -> 0.4f
        }

    val textColor: Color
        get() = when (this) {
            DEFAULT -> Color.White
            SELECTED -> Color(0xFFE5FFF9)
            HIGHLIGHTED -> Color(0xFFFFE082)
            DISABLED -> Color.Gray
        }
}
