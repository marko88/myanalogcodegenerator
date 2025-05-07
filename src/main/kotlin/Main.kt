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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.graphicsLayer
import domain.model.ArchitectureLayer
import domain.model.TestData
import ui.components.canvas.NodeBox

@Composable
fun App() {
    val architecture = TestData.createBookLibraryArchitecture()
    val nodesByLayer = ArchitectureLayer.values().map { layer ->
        layer to architecture.getNodesByType(layer).toList()
    }

    // Layout constants
    val layerSpacing = 160f
    val nodeSpacing = 200f // More space for wider boxes
    val startX = 100f
    val startY = 80f
    val nodeBoxWidth = 180f
    val nodeBoxHeight = 80f

    // Calculate node positions (auto-layout)
    val nodePositions = mutableMapOf<String, Offset>()
    nodesByLayer.forEachIndexed { layerIndex, (layer, nodes) ->
        val y = startY + layerIndex * layerSpacing
        val totalWidth = (nodes.size - 1) * nodeSpacing
        val layerStartX = startX + if (nodes.size > 1) 0f else totalWidth / 2
        nodes.forEachIndexed { nodeIndex, node ->
            val x = layerStartX + nodeIndex * nodeSpacing
            nodePositions[node.id] = Offset(x, y)
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
                .padding(24.dp)
                .clickable { selectedNodeId = null } // Reset selection on background click
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Only show connections if a node is selected
                if (selectedNode != null) {
                    // Draw dependency lines/arrows from selected node
                    selectedNode.dependencies.forEach { dep ->
                        val from = nodePositions[selectedNode.id] ?: return@forEach
                        val to = nodePositions[dep.targetId] ?: return@forEach
                        drawLine(
                            color = Color(0xFFB83B5E),
                            start = Offset(from.x + nodeBoxWidth / 2, from.y + nodeBoxHeight / 2),
                            end = Offset(to.x + nodeBoxWidth / 2, to.y + nodeBoxHeight / 2),
                            strokeWidth = 3f
                        )
                    }
                    // Draw lines from dependents to selected node
                    architecture.getAllNodes().forEach { node ->
                        if (node.dependencies.any { it.targetId == selectedNode.id }) {
                            val from = nodePositions[node.id] ?: return@forEach
                            val to = nodePositions[selectedNode.id] ?: return@forEach
                            drawLine(
                                color = Color(0xFFB83B5E),
                                start = Offset(from.x + nodeBoxWidth / 2, from.y + nodeBoxHeight / 2),
                                end = Offset(to.x + nodeBoxWidth / 2, to.y + nodeBoxHeight / 2),
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
                NodeBox(
                    name = node.name,
                    modifier = Modifier
                        .padding(0.dp)
                        .background(Color.Transparent)
                        .padding(start = pos.x.dp, top = pos.y.dp)
                        .graphicsLayer { alpha = if (isBlurred) 0.3f else 1f }
                        .clickable { selectedNodeId = node.id },
                    boxColor = if (isHighlighted) Color(0xFF2D2236) else Color(0xFF23192D),
                    borderColor = if (isHighlighted) Color(0xFFB83B5E) else node.color,
                    boxWidth = nodeBoxWidth.dp,
                    boxHeight = nodeBoxHeight.dp
                )
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
