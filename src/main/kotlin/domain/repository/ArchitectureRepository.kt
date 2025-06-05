package myanalogcodegenerator.domain.repository

import domain.model.ArchitectureLayer
import domain.model.ArchitectureNode
import domain.model.NodeDependency
import domain.repository.ArchitectureDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ArchitectureRepository {
    private val _model = MutableStateFlow(ArchitectureDatabase())
    val model: StateFlow<ArchitectureDatabase> = _model.asStateFlow()

    fun addNode(node: ArchitectureNode) {
        _model.value = _model.value.addNode(node)
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
    }
}