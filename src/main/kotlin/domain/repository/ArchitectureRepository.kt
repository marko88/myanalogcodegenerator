package myanalogcodegenerator.domain.repository

import domain.model.ArchitectureLayer
import domain.model.ArchitectureNode
import domain.model.DataFlowConnection
import domain.model.NodeDependency
import domain.repository.ArchitectureDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import myanalogcodegenerator.ui.components.canvas.SelectableEntity
import myanalogcodegenerator.ui.components.canvas.name
import ui.components.canvas.NodeSelectionState

class ArchitectureRepository {
    private val _model = MutableStateFlow(ArchitectureDatabase())
    val model: StateFlow<ArchitectureDatabase> = _model

    private val _selection = MutableStateFlow<Set<SelectableEntity>>(emptySet())
    val selection: StateFlow<Set<SelectableEntity>> = _selection

    private val _activeDataFlows = MutableStateFlow<List<DataFlowConnection>>(emptyList())
    val activeDataFlows: StateFlow<List<DataFlowConnection>> = _activeDataFlows

    fun addNode(node: ArchitectureNode) {
        _model.value = _model.value.addNode(node)
    }

    fun updateModel(newModel: ArchitectureDatabase) {
        _model.value = newModel
    }

    fun getRelatedNodeIds(nodeId: String): Set<String> {
        val related = mutableSetOf<String>()

        // 1. Find all nodes this node depends on (outgoing dependencies)
        val node = getNodeById(nodeId)
        node?.dependencies?.forEach { dep ->
            related.add(dep.targetId)
        }

        // 2. Find all nodes that depend on this node (incoming dependencies)
        getAllNodes().forEach { other ->
            if (other.dependencies.any { it.targetId == nodeId }) {
                related.add(other.id)
            }
        }

        // 3. Include data flow relations
        getDataFlow().forEach { flow ->
            if (flow.fromNodeId == nodeId) related.add(flow.toNodeId)
            if (flow.toNodeId == nodeId) related.add(flow.fromNodeId)
        }

        return related
    }

    fun updateActiveDataFlows() {
        val selectedEntities = selection.value
        val relevantFlows = getDataFlow().filter { flow ->
            selectedEntities.any {
                when (it) {
                    is SelectableEntity.Method -> flow.fromSymbol == it.method.name || flow.toSymbol == it.method.name
                    is SelectableEntity.Attribute -> flow.fromSymbol == it.attribute.name || flow.toSymbol == it.attribute.name
                    else -> false
                }
            }
        }
        _activeDataFlows.value = relevantFlows
    }


    fun getRelatedDataFlows(selectableEntity: String?): Set<String> {
        val related = mutableSetOf<String>()

        selectableEntity?.let { entityName ->
            // 3. Include data flow relations
            getDataFlow().forEach { flow ->
                if (flow.fromNodeId == entityName) related.add(flow.toNodeId)
                if (flow.toNodeId == entityName) related.add(flow.fromNodeId)
            }
        }

        return related
    }

    fun addDependency(sourceId: String, dependency: NodeDependency) {
        val sourceNode = getNodeById(sourceId)
        if (sourceNode != null) {
            val updatedNode = sourceNode.copy(
                dependencies = sourceNode.dependencies + dependency
            )
            _model.value = _model.value.addNode(updatedNode)
        }
    }

    fun getNodeById(id: String): ArchitectureNode? {
        return _model.value.getNodeById(id)
    }

    fun getNodeSelectionState(entity: SelectableEntity): NodeSelectionState {
        if (selection.value.isEmpty()) return NodeSelectionState.DEFAULT

        val selectedNodes = selection.value.filterIsInstance<SelectableEntity.Node>().map { it.nodeId }.toSet()
        val selectedAttributes = selection.value.filterIsInstance<SelectableEntity.Attribute>()
        val selectedMethods = selection.value.filterIsInstance<SelectableEntity.Method>()

        val parentSelectedNodes = (selectedMethods.map { it.nodeId } + selectedAttributes.map { it.nodeId }).distinct()

        val nodeRelatedIds = selectedNodes.flatMap { getRelatedNodeIds(it) }.toSet()
        val itemRelatedIds = selection.value
            .filter { it is SelectableEntity.Method || it is SelectableEntity.Attribute }
            .flatMap { getRelatedNodeIds(it.nodeId) }
            .toSet()

        return when (entity) {
            is SelectableEntity.Node -> {
                when {
                    selectedNodes.contains(entity.nodeId) || parentSelectedNodes.contains(entity.nodeId) -> NodeSelectionState.SELECTED
                    nodeRelatedIds.contains(entity.nodeId) || itemRelatedIds.contains(entity.nodeId) -> NodeSelectionState.HIGHLIGHTED
                    else -> NodeSelectionState.DISABLED
                }
            }

            is SelectableEntity.Method -> {
                val isParentSelected = selectedNodes.contains(entity.nodeId)
                val isSelfSelected = selectedMethods.any { it.nodeId == entity.nodeId && it.method == entity.method }
                val isParentRelated = nodeRelatedIds.contains(entity.nodeId)
                val isItemRelated = selection.value.any {
                    (it is SelectableEntity.Method || it is SelectableEntity.Attribute) &&
                            getRelatedNodeIds(it.nodeId).contains(entity.nodeId)
                }

                when {
                    isParentSelected || isSelfSelected -> NodeSelectionState.SELECTED
                    isParentRelated || isItemRelated -> NodeSelectionState.HIGHLIGHTED
                    else -> NodeSelectionState.DISABLED
                }
            }

            is SelectableEntity.Attribute -> {
                val isParentSelected = selectedNodes.contains(entity.nodeId)
                val isSelfSelected = selectedAttributes.any { it.nodeId == entity.nodeId && it.attribute == entity.attribute }
                val isParentRelated = nodeRelatedIds.contains(entity.nodeId)
                val isItemRelated = selection.value.any {
                    (it is SelectableEntity.Method || it is SelectableEntity.Attribute) &&
                            getRelatedDataFlows(it.name).contains(entity.nodeId)
                }

                when {
                    isParentSelected || isSelfSelected -> NodeSelectionState.SELECTED
                    isParentRelated || isItemRelated -> NodeSelectionState.HIGHLIGHTED
                    else -> NodeSelectionState.DISABLED
                }
            }
        }
    }



    fun getDataFlow(): List<DataFlowConnection> {
        return _model.value.dataFlows
    }

    fun getAllNodes(): List<ArchitectureNode> {
        return _model.value.getAllNodes().toList()
    }

    fun getNodesByType(layer: ArchitectureLayer): List<ArchitectureNode> {
        return _model.value.getNodesByType(layer).toList()
    }

    fun clear() {
        _model.value = ArchitectureDatabase()
        _selection.value = emptySet()
    }

    // Selection management
    fun toggleSelection(entity: SelectableEntity) {
        _selection.value = if (_selection.value.contains(entity)) {
            _selection.value - entity
        } else {
            _selection.value + entity
        }
        updateActiveDataFlows()
    }

    fun clearSelection() {
        _selection.value = emptySet()
    }

    fun isSelected(entity: SelectableEntity): Boolean {
        return _selection.value.contains(entity)
    }
}
