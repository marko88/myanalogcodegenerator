package domain.repository

import domain.model.ArchitectureLayer
import domain.model.ArchitectureNode
import domain.model.DataFlowConnection

/**
 * Immutable top-level container for the architecture graph
 */
data class ArchitectureDatabase(
    val nodesById: Map<String, ArchitectureNode> = emptyMap(),
    val nodesByType: Map<ArchitectureLayer, Set<ArchitectureNode>> = emptyMap(),
    val nodesByName: Map<String, ArchitectureNode> = emptyMap(),
    val nodesByPackage: Map<String, Set<ArchitectureNode>> = emptyMap(),
    val dataFlows: List<DataFlowConnection> = emptyList()
) {

    fun addNode(node: ArchitectureNode): ArchitectureDatabase {
        val newNodesById = nodesById + (node.id to node)
        val newNodesByName = nodesByName + (node.name to node)

        val updatedTypeSet = nodesByType[node.layer]?.plus(node) ?: setOf(node)
        val newNodesByType = nodesByType + (node.layer to updatedTypeSet)

        val packageName = node.layer.packageName
        val updatedPackageSet = nodesByPackage[packageName]?.plus(node) ?: setOf(node)
        val newNodesByPackage = nodesByPackage + (packageName to updatedPackageSet)

        return copy(
            nodesById = newNodesById,
            nodesByName = newNodesByName,
            nodesByType = newNodesByType,
            nodesByPackage = newNodesByPackage
        )
    }

    fun removeNode(id: String): ArchitectureDatabase {
        val node = nodesById[id] ?: return this

        val newNodesById = nodesById - id
        val newNodesByName = nodesByName - node.name
        val newNodesByType = nodesByType.mapValues { (_, set) -> set - node }
        val newNodesByPackage = nodesByPackage.mapValues { (_, set) -> set - node }

        return copy(
            nodesById = newNodesById,
            nodesByName = newNodesByName,
            nodesByType = newNodesByType,
            nodesByPackage = newNodesByPackage
        )
    }

    fun updateNode(id: String, transform: (ArchitectureNode) -> ArchitectureNode): ArchitectureDatabase {
        val oldNode = nodesById[id] ?: return this
        val newNode = transform(oldNode)

        val newNodesById = nodesById + (id to newNode)
        val newNodesByName = nodesByName - oldNode.name + (newNode.name to newNode)

        val updatedTypeSet = nodesByType[oldNode.layer]?.minus(oldNode)?.plus(newNode) ?: setOf(newNode)
        val newNodesByType = nodesByType + (oldNode.layer to updatedTypeSet)

        val oldPackage = oldNode.layer.packageName
        val newNodesByPackage = nodesByPackage + (oldPackage to (
                nodesByPackage[oldPackage]?.minus(oldNode)?.plus(newNode) ?: setOf(newNode)
                ))

        return copy(
            nodesById = newNodesById,
            nodesByName = newNodesByName,
            nodesByType = newNodesByType,
            nodesByPackage = newNodesByPackage
        )
    }

    fun addDataFlow(flow: DataFlowConnection): ArchitectureDatabase {
        return copy(dataFlows = dataFlows + flow)
    }

    fun getNodeById(id: String): ArchitectureNode? = nodesById[id]

    fun getNodesByType(type: ArchitectureLayer): Set<ArchitectureNode> =
        nodesByType[type] ?: emptySet()

    fun getNodeByName(name: String): ArchitectureNode? = nodesByName[name]

    fun getNodesByPackage(packageName: String): Set<ArchitectureNode> =
        nodesByPackage[packageName] ?: emptySet()

    fun getAllNodes(): Collection<ArchitectureNode> = nodesById.values
}