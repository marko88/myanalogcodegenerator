package myanalogcodegenerator.ui.components.canvas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import domain.model.ArchitectureLayer
import domain.model.ArchitectureNode
import myanalogcodegenerator.ui.components.canvas.node.NodeBox

@Composable
fun LayeredNodeLayout(nodes: List<ArchitectureNode>) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        ArchitectureLayer.values().forEach { layer ->
            val nodesInLayer = nodes.filter { it.layer == layer }
            val nodesByType = nodesInLayer.groupBy { it.type }

            nodesByType.forEach { (_, groupedNodes) ->
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    groupedNodes.forEach { node ->
                        NodeBox(
                            node = node
                        )
                    }
                }
                Spacer(modifier = Modifier.size(12.dp))
            }
        }
    }
}