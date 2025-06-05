package ui.components.canvas

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.launch
import myanalogcodegenerator.domain.repository.ArchitectureRepository
import myanalogcodegenerator.ui.components.canvas.NodeLayout
import myanalogcodegenerator.ui.components.layout.rememberCanvasTransform

@Composable
fun CanvasView(
    architectureRepository: ArchitectureRepository,
    modifier: Modifier = Modifier.fillMaxSize().background(Color(0xFF1A1B26))
) {
    val architecture = architectureRepository.model.collectAsState().value
    val transformState = rememberCanvasTransform()
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scope.launch {
                        transformState.zoom(zoom)
                        transformState.pan(pan)
                    }
                }
            }
            .graphicsLayer(
                scaleX = transformState.scale,
                scaleY = transformState.scale,
                translationX = transformState.offset.x,
                translationY = transformState.offset.y
            )
    ) {
        NodeLayout(
            nodes = architecture.getAllNodes().toList(),
            getStateForNode = { NodeBoxState.NORMAL }
        )
    }
}

