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
import myanalogcodegenerator.domain.command.CommandManager
import myanalogcodegenerator.domain.command.use_cases.SelectEntityCommand
import myanalogcodegenerator.domain.repository.ArchitectureRepository
import myanalogcodegenerator.domain.repository.PinPositionRegistry
import myanalogcodegenerator.ui.components.canvas.LayeredNodeLayout
import myanalogcodegenerator.ui.components.canvas.grid.BlueprintGridBackground
import myanalogcodegenerator.ui.components.canvas.node.DataFlowConnectionView

@Composable
fun CanvasView(architectureRepository: ArchitectureRepository) {
    val architecture by architectureRepository.model.collectAsState()
    val nodes = architecture.getAllNodes().toList()

    val selection by architectureRepository.selection.collectAsState() // 👈 observe selection

    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    BlueprintGridBackground(
        scale,
        offset
    )
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
    ) {

        LayeredNodeLayout(
            architectureRepository,
            nodes = nodes,
            selection = selection, // 👈 pass current selection state
            onClick = { selectableEntity ->
                CommandManager.execute(
                    SelectEntityCommand(
                        architectureRepository,
                        setOf(selectableEntity)
                    )
                )
            }
        )
    }

    architectureRepository.activeDataFlows.value.forEach { flow ->
        PinPositionRegistry.getPosition(flow.fromNodeId + "#" + flow.fromSymbol)?.let {
            PinPositionRegistry.getPosition(flow.toNodeId + "#" + flow.toSymbol)?.let { it1 ->
                DataFlowConnectionView(
                    fromCoord = it,
                    toCoord = it1,
                    semantics = flow.semantics
                )
            }
        }
    }
}

