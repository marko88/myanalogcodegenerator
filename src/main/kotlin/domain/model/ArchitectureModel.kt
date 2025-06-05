package domain.model

import androidx.compose.ui.geometry.Offset
import domain.repository.ArchitectureDefinitionModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Represents a node in the architecture graph
 */
data class ArchitectureNode(
    val id: String,                    // Unique identifier
    val name: String,                  // Class name
    val layer: ArchitectureLayer,      // Architectural layer
    val description: String = "",      // Component description
    var position: Offset = Offset.Zero, // Position in the diagram
    val dependencies: List<NodeDependency> = emptyList(), // Direct dependencies
    val dependencyChains: List<DependencyChain> = emptyList(), // Complete dependency chains this node is part of
    val attributes: List<String> = emptyList(),   // Attributes for the node
    val methods: List<String> = emptyList()       // Methods for the node
)

/**
 * Represents a direct dependency of a node
 */
data class NodeDependency(
    val targetId: String,              // ID of the dependent node
    val type: DependencyType,          // Type of dependency
    val isExternal: Boolean = false,   // Whether the dependency is outside our architecture
    val description: String = "",      // Optional description of the dependency relationship
    val optional: Boolean = false
)

/**
 * Represents a complete dependency chain
 * Example: BooksLibraryViewModel -> GetAllRentedBooksUseCase -> LibraryService -> BooksRepository
 */
data class DependencyChain(
    val nodes: List<String>,           // List of node IDs in the chain
    val types: List<DependencyType>,   // Types of dependencies between nodes
    val description: String = ""       // Description of what this chain represents
)


class ArchitectureModel {
    private val _model = MutableStateFlow(ArchitectureDefinitionModel())
    val model: StateFlow<ArchitectureDefinitionModel> = _model.asStateFlow()

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
        _model.value = ArchitectureDefinitionModel()
    }
}