package ui.components.canvas

import androidx.compose.ui.graphics.Color

/**
 * Defines the interaction or UI state of a node, attribute, or method.
 *
 * */
enum class NodeSelectionState {
    DEFAULT,        // Normal appearance
    SELECTED,       // Currently selected
    HIGHLIGHTED,    // Highlighted for reference or flow
    DISABLED;       // Grayed out or inactive

    val backgroundColor: Color
        get() = when (this) {
            DEFAULT -> Color(0xFF1E1E2E)         // Dark purple-blue
            SELECTED -> Color(0xFF2C2C4C)         // Deep bluish selection
            HIGHLIGHTED -> Color(0xFF3A2E4F)      // Soft purple highlight
            DISABLED -> Color(0xFF161616)         // Very dark for disabled
        }

    val borderColor: Color
        get() = when (this) {
            DEFAULT -> Color(0xFF5A5A5A)          // Neutral gray
            SELECTED -> Color(0xFF66FFF8)         // Vibrant aqua
            HIGHLIGHTED -> Color(0xFFFFC947)      // Warm amber for emphasis
            DISABLED -> Color(0xFF3A3A3A)          // Subdued
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
            DEFAULT -> Color(0xFFEEEEEE)          // Soft white
            SELECTED -> Color(0xFFCCF5FF)          // Light cyan
            HIGHLIGHTED -> Color(0xFFFFF8DC)       // Light yellow/cream
            DISABLED -> Color(0xFF777777)          // Dim gray
        }
}
