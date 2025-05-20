import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import domain.model.ArchitectureLayer
import domain.model.TestData
import ui.components.canvas.CanvasGUIConstants
import ui.components.canvas.CanvasGUIConstants.nodeBoxHeight
import ui.components.canvas.CanvasGUIConstants.nodeBoxWidth
import ui.components.canvas.NodeBox
import ui.components.canvas.Canvas as InteractiveCanvas

@Composable
fun App() {
    val architecture = TestData.createBookLibraryArchitecture()
    val nodesByLayer = ArchitectureLayer.values().map { layer ->
        layer to architecture.getNodesByType(layer).toList()
    }
    val startX = 100f
    val startY = 80f

    // Calculate node positions (auto-layout)
    val nodePositions = mutableMapOf<String, Offset>()
    nodesByLayer.forEachIndexed { layerIndex, (_, nodes) ->
        val y = startY + layerIndex * CanvasGUIConstants.layerSpacing
        val totalWidth = (nodes.size - 1) * CanvasGUIConstants.nodeSpacing
        val layerStartX = startX + if (nodes.size > 1) 0f else totalWidth / 2
        nodes.forEachIndexed { nodeIndex, node ->
            val x = layerStartX + nodeIndex * CanvasGUIConstants.nodeSpacing
            nodePositions[node.id] = Offset(x, y)
            println("Calculated position for ${node.name}: x=$x, y=$y")
        }
    }

    var selectedNodeId by remember { mutableStateOf<String?>(null) }

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
                    // Draw lines in the same coordinate space as the nodes
                    if (selectedNode != null) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            // Draw dependency lines/arrows from selected node
                            selectedNode.dependencies.forEach { dep ->
                                val from = nodePositions[selectedNode.id] ?: return@forEach
                                val to = nodePositions[dep.targetId] ?: return@forEach
                                
                                // Calculate center points of nodes
                                val fromCenter = Offset(
                                    x = (from.x + nodeBoxWidth / 2) * 2,
                                    y = (from.y + nodeBoxHeight / 2) * 2
                                )
                                val toCenter = Offset(
                                    x = (to.x + nodeBoxWidth / 2) * 2,
                                    y = (to.y + nodeBoxHeight / 2) * 2
                                )
                                
                                drawLine(
                                    color = Color(0xFFB83B5E),
                                    start = fromCenter,
                                    end = toCenter,
                                    strokeWidth = 3f
                                )
                            }
                            
                            // Draw lines from dependents to selected node
                            architecture.getAllNodes().forEach { node ->
                                if (node.dependencies.any { it.targetId == selectedNode.id }) {
                                    val from = nodePositions[node.id] ?: return@forEach
                                    val to = nodePositions[selectedNode.id] ?: return@forEach
                                    
                                    // Calculate center points of nodes
                                    val fromCenter = Offset(
                                        x = (from.x + nodeBoxWidth / 2) * 2,
                                        y = (from.y + nodeBoxHeight / 2) * 2
                                    )
                                    val toCenter = Offset(
                                        x = (to.x + nodeBoxWidth / 2) * 2,
                                        y = (to.y + nodeBoxHeight / 2) * 2
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
                    
                    // Overlay NodeBoxes at calculated positions
                    architecture.getAllNodes().forEach { node ->
                        val pos = nodePositions[node.id] ?: return@forEach
                        val isHighlighted = node.id in highlightIds
                        val isBlurred = selectedNodeId != null && !isHighlighted
                        
                        val nodeState = when {
                            isHighlighted -> NodeBoxState.HIGHLIGHTED
                            isBlurred -> NodeBoxState.BLURRED
                            else -> NodeBoxState.NORMAL
                        }
                        
                        println("Rendering NodeBox for ${node.name} at position: $pos")
                        
                        Box(
                            modifier = Modifier
                                .padding(start = pos.x.dp, top = pos.y.dp)
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
