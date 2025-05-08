package domain.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

/**
 * Represents a node in the architecture graph
 */
data class ArchitectureNode(
    val id: String,                    // Unique identifier
    val name: String,                  // Class name
    val packageName: String,           // Package name
    val layer: ArchitectureLayer,      // Architectural layer
    val description: String = "",      // Component description
    val color: Color = Color.Gray,     // Visualization color
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
    val description: String = ""       // Optional description of the dependency relationship
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