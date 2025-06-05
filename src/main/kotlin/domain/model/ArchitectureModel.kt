package domain.model

import androidx.compose.ui.geometry.Offset

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
    val attributes: List<NodeAttribute> = emptyList(),   // Attributes for the node
    val methods: List<NodeMethod> = emptyList(),       // Methods for the node
)

/**
 * Describes a logical connection between two components in terms of data.
 */
data class DataFlowConnection(
    val fromNodeId: String,
    val fromSymbol: String,
    val toNodeId: String,
    val toSymbol: String,
    val semantics: DataFlowSemantics = DataFlowSemantics.Stream,
    val isGenerated: Boolean = true,
    val notes: String = ""
)

/**
 * Represents a direct dependency of a node
 */
data class NodeDependency(
    val targetId: String,              // ID of the dependent node
    val type: DependencyType,          // Type of dependency
    val isExternal: Boolean = false,   // Whether the dependency is outside our architecture
    val description: String = "",      // Optional description of the dependency relationship
    val optional: Boolean = false,
)

/**
 * Represents a method available on a node
 */
data class NodeMethod(
    val name: String,
    val returnType: String,
    val parameters: List<Pair<String, String>> = emptyList(), // List of (name, type)
    val isSuspend: Boolean = false,
    val isPublic: Boolean = true,
    val description: String = "",
    val semantics: DataFlowSemantics? = null
)

/**
 * Represents an attribute exposed by a node
 */
data class NodeAttribute(
    val name: String,
    val type: String,
    val isReactive: Boolean = false, // e.g., Flow, StateFlow, LiveData
    val isMutable: Boolean = false,
    val description: String = "",
    val semantics: DataFlowSemantics? = null
)

/**
 * Describes the architectural meaning of a data flow connection.
 * These are intentionally language-agnostic to support multiple code generators.
 *
 * - State:     A persistent, observable value that changes over time (e.g., UI state)
 * - Event:     A one-time signal or notification (e.g., login success)
 * - Command:   A trigger for behavior, typically without return (e.g., submit form)
 * - Request:   A call expecting a response (e.g., fetch user)
 * - Response:  The result of a request (e.g., User object)
 * - Stream:    A continuous flow of values (e.g., logs, feeds)
 * - Binding:   Two-way sync (e.g., form input bound to model)
 */
enum class DataFlowSemantics { State, Event, Command, Request, Response, Stream, Binding }

/**
 * Represents a complete dependency chain
 * Example: BooksLibraryViewModel -> GetAllRentedBooksUseCase -> LibraryService -> BooksRepository
 */
data class DependencyChain(
    val nodes: List<String>,           // List of node IDs in the chain
    val types: List<DependencyType>,   // Types of dependencies between nodes
    val description: String = "",       // Description of what this chain represents
)
