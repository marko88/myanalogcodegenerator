package myanalogcodegenerator.ui.components.canvas.graph

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import myanalogcodegenerator.domain.command.CommandManager
import myanalogcodegenerator.domain.command.use_cases.SelectEntityCommand
import myanalogcodegenerator.domain.repository.ArchitectureRepository
import myanalogcodegenerator.domain.repository.PinPositionRegistry
import myanalogcodegenerator.ui.components.canvas.grid.BlueprintGridBackground
import myanalogcodegenerator.ui.components.canvas.node.DataFlowConnectionView

@Composable
fun CanvasView(
    architectureRepository: ArchitectureRepository,
    scale: Float,
    pan: Offset,
    onTransform: (newScale: Float, newPan: Offset) -> Unit,
    modifier: Modifier = Modifier,
    minScale: Float = 0.2f,
    maxScale: Float = 4f
) {
    val architecture by architectureRepository.model.collectAsState()
    val nodes = remember(architecture) { architecture.getAllNodes().toList() }
    val selection by architectureRepository.selection.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            // Catch pinch + pan gestures over the whole canvas
            .pointerInput(scale, pan, minScale, maxScale) {
                detectTransformGestures { _, panChange, zoomChange, _ ->
                    val newScale = (scale * zoomChange).coerceIn(minScale, maxScale)
                    val newPan = pan + panChange
                    onTransform(newScale, newPan)
                }
            }
    ) {
        // Apply the same transform to ALL layers so they stay aligned
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = pan.x,
                    translationY = pan.y
                )
        ) {
            // BACKGROUND (under everything)
            BlueprintGridBackground(
                scale = scale,
                offset = pan
            )

            // EDGES / FLOWS
            architectureRepository.activeDataFlows.value.forEach { flow ->
                val from = PinPositionRegistry.getPosition("${flow.fromNodeId}#${flow.fromSymbol}")
                val to = PinPositionRegistry.getPosition("${flow.toNodeId}#${flow.toSymbol}")
                if (from != null && to != null) {
                    DataFlowConnectionView(
                        fromCoord = from,
                        toCoord = to,
                        semantics = flow.semantics
                    )
                }
            }

            // NODES (above edges)
            LayeredNodeLayout(
                architectureRepository = architectureRepository,
                nodes = nodes,
                selection = selection,
                onClick = { selectable ->
                    CommandManager.execute(
                        SelectEntityCommand(
                            architectureRepository,
                            setOf(selectable)
                        )
                    )
                }
            )
        }
    }
}