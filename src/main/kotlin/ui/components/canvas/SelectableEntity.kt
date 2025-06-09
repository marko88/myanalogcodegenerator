package myanalogcodegenerator.ui.components.canvas

import domain.model.NodeAttribute
import domain.model.NodeMethod

/**
 * Represents any selectable component in the architecture.
 */
sealed class SelectableEntity(open val nodeId: String, open val nodeName: String) {

    /**
     * A whole node (e.g., a ViewModel, Presenter, etc.)
     */
    data class Node(override val nodeId: String) : SelectableEntity(nodeId, "")

    /**
     * A method declared on a node.
     */
    data class Method(override val nodeId: String, val method: NodeMethod) : SelectableEntity(nodeId, method.name)

    /**
     * An attribute declared on a node.
     */
    data class Attribute(override val nodeId: String, val attribute: NodeAttribute) : SelectableEntity(nodeId, attribute.name)

    // If needed in future, more types can be added here, like Events, Flows, etc.
}

/**
 * This will be used only for Method or Attribute to get a name.
 */
val SelectableEntity.name: String?
    get() = when (this) {
        is SelectableEntity.Attribute -> this.attribute.name
        is SelectableEntity.Method -> this.method.name
        is SelectableEntity.Node -> null
    }