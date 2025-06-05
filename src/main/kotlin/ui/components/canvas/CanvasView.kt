package ui.components.canvas

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import myanalogcodegenerator.domain.repository.ArchitectureRepository
import myanalogcodegenerator.ui.components.canvas.LayeredNodeLayout

@Composable
fun CanvasView(architectureRepository: ArchitectureRepository) {
    val architecture by architectureRepository.model.collectAsState()
    val nodes = architecture.getAllNodes().toList()

    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale *= zoom
                    offset += pan
                }
            }
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offset.x,
                translationY = offset.y
            )
            .background(color = androidx.compose.ui.graphics.Color(0xFF1A1B26))
    ) {
        LayeredNodeLayout(nodes = nodes)
    }
}

