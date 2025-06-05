package myanalogcodegenerator.ui.components.canvas


import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import domain.model.ArchitectureNode
import myanalogcodegenerator.ui.components.canvas.node.NodeBox
import ui.components.canvas.NodeSelectionState

@Composable
fun NodeLayout(
    nodes: List<ArchitectureNode>,
    modifier: Modifier = Modifier,
    getStateForNode: (ArchitectureNode) -> NodeSelectionState
) {
    BoxWithConstraints(modifier = modifier) {
        nodes.forEach { node ->
            val state = getStateForNode(node)
            NodeBox(
                node = node,
                modifier = Modifier.offset {
                    IntOffset(node.position.x.toInt(), node.position.y.toInt())
                }
            )
        }
    }
}
