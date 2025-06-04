package domain.model

/**
 * Top-level container for the architecture graph that provides efficient querying capabilities
 */
class ArchitectureDefinitionModel {
    // Fast lookup maps
    private val nodesById: MutableMap<String, ArchitectureNode> = mutableMapOf()
    private val nodesByType: MutableMap<ArchitectureLayer, MutableSet<ArchitectureNode>> = mutableMapOf()
    private val nodesByName: MutableMap<String, ArchitectureNode> = mutableMapOf()
    private val nodesByPackage: MutableMap<String, MutableSet<ArchitectureNode>> = mutableMapOf()

    /**
     * Add a node to the graph
     */
    fun addNode(node: ArchitectureNode) {
        nodesById[node.id] = node
        nodesByName[node.name] = node
        nodesByType.getOrPut(node.layer) { mutableSetOf() }.add(node)
        nodesByPackage.getOrPut(node.layer.packageName) { mutableSetOf() }.add(node)
    }

    /**
     * Get a node by its ID
     */
    fun getNodeById(id: String): ArchitectureNode? = nodesById[id]

    /**
     * Get all nodes of a specific type
     */
    fun getNodesByType(type: ArchitectureLayer): Set<ArchitectureNode> = 
        nodesByType[type] ?: emptySet()

    /**
     * Get a node by its name
     */
    fun getNodeByName(name: String): ArchitectureNode? = nodesByName[name]

    /**
     * Get all nodes in a specific package
     */
    fun getNodesByPackage(packageName: String): Set<ArchitectureNode> = 
        nodesByPackage[packageName] ?: emptySet()

    /**
     * Get all dependency chains that include a specific node
     */
    fun getDependencyChainsForNode(nodeId: String): List<DependencyChain> {
        val node = nodesById[nodeId] ?: return emptyList()
        return node.dependencyChains
    }

    /**
     * Get all nodes that depend on a specific node
     */
    fun getDependentNodes(nodeId: String): Set<ArchitectureNode> {
        return nodesById.values.filter { node ->
            node.dependencies.any { it.targetId == nodeId }
        }.toSet()
    }

    /**
     * Get all nodes that a specific node depends on
     */
    fun getDependencyNodes(nodeId: String): Set<ArchitectureNode> {
        val node = nodesById[nodeId] ?: return emptySet()
        return node.dependencies.mapNotNull { nodesById[it.targetId] }.toSet()
    }

    /**
     * Get the complete dependency chain starting from a specific node
     * This will traverse the graph and build the complete chain
     */
    fun getCompleteDependencyChain(startNodeId: String): List<ArchitectureNode> {
        val visited = mutableSetOf<String>()
        val chain = mutableListOf<ArchitectureNode>()
        
        fun traverse(nodeId: String) {
            if (nodeId in visited) return
            visited.add(nodeId)
            
            val node = nodesById[nodeId] ?: return
            chain.add(node)
            
            node.dependencies.forEach { dependency ->
                traverse(dependency.targetId)
            }
        }
        
        traverse(startNodeId)
        return chain
    }

    /**
     * Get all nodes in the graph
     */
    fun getAllNodes(): Set<ArchitectureNode> = nodesById.values.toSet()

    /**
     * Clear all data
     */
    fun clear() {
        nodesById.clear()
        nodesByType.clear()
        nodesByName.clear()
        nodesByPackage.clear()
    }
} 