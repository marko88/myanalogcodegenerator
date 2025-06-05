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