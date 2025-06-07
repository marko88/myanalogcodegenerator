package myanalogcodegenerator.ui.components.canvas

import domain.model.NodeAttribute
import domain.model.NodeMethod

/**
 * Represents any selectable component in the architecture.
 */
sealed class SelectableEntity(open val nodeId: String) {

    /**
     * A whole node (e.g., a ViewModel, Presenter, etc.)
     */
    data class Node(override val nodeId: String) : SelectableEntity(nodeId)

    /**
     * A method declared on a node.
     */
    data class Method(override val nodeId: String, val method: NodeMethod) : SelectableEntity(nodeId)

    /**
     * An attribute declared on a node.
     */
    data class Attribute(override val nodeId: String, val attribute: NodeAttribute) : SelectableEntity(nodeId)

    // If needed in future, more types can be added here, like Events, Flows, etc.
}