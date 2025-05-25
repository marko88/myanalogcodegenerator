package ui.components.canvas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import domain.model.ArchitectureDefinitionModel
import domain.model.ArchitectureNode

class NodeLayoutState(
    private val architecture: ArchitectureDefinitionModel
) {
    var selectedNodeId by mutableStateOf<String?>(null)
        private set

    var nodePositions by mutableStateOf(mapOf<String, Offset>())
        private set

    var nodeSizes by mutableStateOf(mapOf<String, Offset>())
        private set

    var connectingNodeId by mutableStateOf<String?>(null)
        private set

    val selectedNode: ArchitectureNode?
        get() = selectedNodeId?.let { architecture.getNodeById(it) }

    val dependencyIds: Set<String>
        get() = selectedNode?.dependencies?.map { it.targetId }?.toSet() ?: emptySet()

    val dependentIds: Set<String>
        get() = architecture.getAllNodes()
            .filter { n -> n.dependencies.any { it.targetId == selectedNodeId } }
            .map { it.id }
            .toSet()

    val highlightIds: Set<String>
        get() = (dependencyIds + dependentIds + setOfNotNull(selectedNodeId))

    fun selectNode(nodeId: String?) {
        selectedNodeId = nodeId
    }

    fun startConnecting(nodeId: String) {
        connectingNodeId = nodeId
    }

    fun stopConnecting() {
        connectingNodeId = null
    }

    fun updateNodePosition(nodeId: String, position: Offset) {
        nodePositions = nodePositions + (nodeId to position)
    }

    fun updateNodeSize(nodeId: String, size: Offset) {
        nodeSizes = nodeSizes + (nodeId to size)
    }
} 