package myanalogcodegenerator.ui.components.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset

/**
 * Remembers and stores the zoom and pan (offset) state of the canvas.
 */
@Composable
fun rememberCanvasTransform(): CanvasTransformState {
    return remember {
        CanvasTransformState()
    }
}

class CanvasTransformState {
    var scale by mutableStateOf(1f)
    var offset by mutableStateOf(Offset.Zero)

    fun zoom(delta: Float) {
        scale = (scale * delta).coerceIn(0.5f, 3f)
    }

    fun pan(delta: Offset) {
        offset += delta
    }
}