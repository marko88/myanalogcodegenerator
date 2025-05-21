import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.dp
import domain.model.ArchitectureLayer
import domain.model.TestData
import ui.components.canvas.NodeBox
import ui.components.canvas.Canvas as InteractiveCanvas

@Composable
fun App() {
    val architecture = TestData.createBookLibraryArchitecture()
    val nodesByLayer = ArchitectureLayer.values().map { layer ->
        layer to architecture.getNodesByType(layer).toList()
    }

    var selectedNodeId by remember { mutableStateOf<String?>(null) }
    var nodePositions by remember { mutableStateOf(mapOf<String, Offset>()) }
    var nodeSizes by remember { mutableStateOf(mapOf<String, Offset>()) }

    // Find related nodes for highlighting
    val selectedNode = selectedNodeId?.let { architecture.getNodeById(it) }
    val dependencyIds = selectedNode?.dependencies?.map { it.targetId }?.toSet() ?: emptySet()
    val dependentIds = architecture.getAllNodes().filter { n ->
        n.dependencies.any { it.targetId == selectedNodeId }
    }.map { it.id }.toSet()
    val highlightIds = (dependencyIds + dependentIds + setOfNotNull(selectedNodeId))

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1A1B26))
                .clickable { selectedNodeId = null } // Reset selection on background click
        ) {
            InteractiveCanvas {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Draw lines if a node is selected
                    if (selectedNode != null) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            // Draw lines to dependencies
                            selectedNode.dependencies.forEach { dep ->
                                val fromPos = nodePositions[selectedNode.id]
                                val toPos = nodePositions[dep.targetId]
                                val fromSize = nodeSizes[selectedNode.id]
                                val toSize = nodeSizes[dep.targetId]
                                
                                if (fromPos != null && toPos != null && fromSize != null && toSize != null) {
                                    val fromCenter = Offset(
                                        x = fromPos.x + fromSize.x / 2f,
                                        y = fromPos.y + fromSize.y / 2f
                                    )
                                    val toCenter = Offset(
                                        x = toPos.x + toSize.x / 2f,
                                        y = toPos.y + toSize.y / 2f
                                    )
                                    
                                    drawLine(
                                        color = Color(0xFFB83B5E),
                                        start = fromCenter,
                                        end = toCenter,
                                        strokeWidth = 3f
                                    )
                                }
                            }
                            
                            // Draw lines from dependents
                            architecture.getAllNodes().forEach { n ->
                                if (n.dependencies.any { it.targetId == selectedNode.id }) {
                                    val fromPos = nodePositions[n.id]
                                    val toPos = nodePositions[selectedNode.id]
                                    val fromSize = nodeSizes[n.id]
                                    val toSize = nodeSizes[selectedNode.id]
                                    
                                    if (fromPos != null && toPos != null && fromSize != null && toSize != null) {
                                        val fromCenter = Offset(
                                            x = fromPos.x + fromSize.x / 2f,
                                            y = fromPos.y + fromSize.y / 2f
                                        )
                                        val toCenter = Offset(
                                            x = toPos.x + toSize.x / 2f,
                                            y = toPos.y + toSize.y / 2f
                                        )
                                        
                                        drawLine(
                                            color = Color(0xFFB83B5E),
                                            start = fromCenter,
                                            end = toCenter,
                                            strokeWidth = 3f
                                        )
                                    }
                                }
                            }
                        }
                    }
                    
                    // Render nodes in layers
                    Box(modifier = Modifier.fillMaxSize()) {
                        var currentY = 80f
                        nodesByLayer.forEach { (layer, nodes) ->
                            var currentX = 80f
                            val maxHeight = nodes.maxOfOrNull { nodeSizes[it.id]?.y ?: 0f } ?: 0f
                            
                            nodes.forEach { node ->
                                val isHighlighted = node.id in highlightIds
                                val isBlurred = selectedNodeId != null && !isHighlighted
                                
                                val nodeState = when {
                                    isHighlighted -> NodeBoxState.HIGHLIGHTED
                                    isBlurred -> NodeBoxState.BLURRED
                                    else -> NodeBoxState.NORMAL
                                }
                                
                                Box(
                                    modifier = Modifier
                                        .offset(
                                            x = currentX.dp,
                                            y = currentY.dp
                                        )
                                        .onGloballyPositioned { coordinates ->
                                            val position = coordinates.positionInParent()
                                            nodePositions = nodePositions + (node.id to position)
                                            nodeSizes = nodeSizes + (node.id to Offset(
                                                coordinates.size.width.toFloat(),
                                                coordinates.size.height.toFloat()
                                            ))
                                        }
                                ) {
                                    NodeBox(
                                        node = node,
                                        state = nodeState,
                                        showDetails = true,
                                        modifier = Modifier
                                            .background(Color.Transparent)
                                            .clickable { selectedNodeId = node.id }
                                    )
                                }
                                
                                currentX += 20f + (nodeSizes[node.id]?.x ?: 0f)
                            }
                            
                            currentY += maxHeight + 40f
                        }
                    }
                }
            }
        }
    }
}

fun main() = androidx.compose.ui.window.application {
    androidx.compose.ui.window.Window(
        onCloseRequest = ::exitApplication,
        title = "Architecture Visualization"
    ) {
        App()
    }
}
