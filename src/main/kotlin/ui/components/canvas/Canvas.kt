package ui.components.canvas


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.rememberTextMeasurer
import myanalogcodegenerator.domain.repository.ArchitectureRepository
import myanalogcodegenerator.ui.components.layout.drawConnections
import myanalogcodegenerator.ui.components.layout.drawNodeBoxes
import myanalogcodegenerator.ui.components.layout.rememberCanvasTransform

@Composable
fun CanvasView(
    architectureRepository: ArchitectureRepository,
    modifier: Modifier = Modifier.fillMaxSize().background(Color(0xFF1A1B26))
) {
    val architecture = architectureRepository.model.collectAsState().value
    val transformState = rememberCanvasTransform()
    val textMeasurer = rememberTextMeasurer()

    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = transformState.scale,
                    scaleY = transformState.scale,
                    translationX = transformState.offset.x,
                    translationY = transformState.offset.y
                )
        ) {
            val nodes = architecture.getAllNodes().toList()

            drawIntoCanvas {
                drawConnections(nodes)
                drawNodeBoxes(nodes, textMeasurer)
            }
        }
    }
}
