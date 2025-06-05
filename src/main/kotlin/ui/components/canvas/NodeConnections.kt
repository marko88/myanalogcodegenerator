import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import domain.repository.ArchitectureDefinitionModel
import domain.model.ArchitectureNode

@Composable
fun NodeConnections(
    selectedNode: ArchitectureNode?,
    architecture: ArchitectureDefinitionModel,
    nodePositions: Map<String, Offset>,
    nodeSizes: Map<String, Offset>,
    modifier: Modifier = Modifier
) {
    if (selectedNode != null) {
        Canvas(modifier = modifier) {
            // Draw lines to dependencies
            selectedNode.dependencies.forEach { dep ->
                drawConnection(
                    fromId = selectedNode.id,
                    toId = dep.targetId,
                    nodePositions = nodePositions,
                    nodeSizes = nodeSizes
                )
            }
            
            // Draw lines from dependents
            architecture.getAllNodes().forEach { node ->
                if (node.dependencies.any { it.targetId == selectedNode.id }) {
                    drawConnection(
                        fromId = node.id,
                        toId = selectedNode.id,
                        nodePositions = nodePositions,
                        nodeSizes = nodeSizes
                    )
                }
            }
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawConnection(
    fromId: String,
    toId: String,
    nodePositions: Map<String, Offset>,
    nodeSizes: Map<String, Offset>
) {
    val fromPos = nodePositions[fromId]
    val toPos = nodePositions[toId]
    val fromSize = nodeSizes[fromId]
    val toSize = nodeSizes[toId]
    
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