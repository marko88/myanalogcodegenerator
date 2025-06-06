package myanalogcodegenerator.ui.components.canvas

import domain.model.NodeAttribute
import domain.model.NodeMethod

/**
 * Represents any selectable component in the architecture.
 */
sealed class SelectableEntity {

    /**
     * A whole node (e.g., a ViewModel, Presenter, etc.)
     */
    data class Node(val nodeId: String) : SelectableEntity()

    /**
     * A method declared on a node.
     */
    data class Method(val nodeId: String, val method: NodeMethod) : SelectableEntity()

    /**
     * An attribute declared on a node.
     */
    data class Attribute(val nodeId: String, val attribute: NodeAttribute) : SelectableEntity()

    // If needed in future, more types can be added here, like Events, Flows, etc.
}