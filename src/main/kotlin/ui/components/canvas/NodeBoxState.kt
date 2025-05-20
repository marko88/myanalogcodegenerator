import androidx.compose.ui.graphics.Color

enum class NodeBoxState {
    NORMAL,
    HIGHLIGHTED,
    BLURRED;

    val backgroundColor: Color
        get() = when (this) {
            NORMAL -> Color(0xFF23192D)
            HIGHLIGHTED -> Color(0xFF2D2236)
            BLURRED -> Color(0xFF23192D)
        }

    val borderColor: Color
        get() = when (this) {
            NORMAL -> Color(0xFF4A4A4A)
            HIGHLIGHTED -> Color(0xFFB83B5E)
            BLURRED -> Color(0xFF4A4A4A)
        }

    val alpha: Float
        get() = when (this) {
            NORMAL -> 1f
            HIGHLIGHTED -> 1f
            BLURRED -> 0.3f
        }

    val labelBgColor: Color
        get() = when (this) {
            NORMAL -> Color(0xFF181926)
            HIGHLIGHTED -> Color(0xFF181926)
            BLURRED -> Color(0xFF181926)
        }
} 