package myanalogcodegenerator.domain.repository

import domain.model.ArchitectureLayer
import domain.model.ArchitectureNode
import domain.model.NodeDependency
import domain.repository.ArchitectureDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import myanalogcodegenerator.ui.components.canvas.SelectableEntity

class ArchitectureRepository {
    private val _model = MutableStateFlow(ArchitectureDatabase())
    val model: StateFlow<ArchitectureDatabase> = _model

    private val _selection = MutableStateFlow<Set<SelectableEntity>>(emptySet())
    val selection: StateFlow<Set<SelectableEntity>> = _selection

    fun addNode(node: ArchitectureNode) {
        _model.value = _model.value.addNode(node)
    }

    fun updateModel(newModel: ArchitectureDatabase) {
        _model.value = newModel
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
    }

    fun clearSelection() {
        _selection.value = emptySet()
    }

    fun isSelected(entity: SelectableEntity): Boolean {
        return _selection.value.contains(entity)
    }
}
