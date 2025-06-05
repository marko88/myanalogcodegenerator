package myanalogcodegenerator.ui.components.canvas.node

/**
 * Defines the interaction or UI state of a node, attribute, or method.
 */
enum class NodeState {
    DEFAULT,        // Normal appearance
    SELECTED,       // Currently selected
    HIGHLIGHTED,    // Highlighted for reference or flow
    DISABLED        // Grayed out or inactive
}