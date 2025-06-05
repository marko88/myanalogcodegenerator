package domain.blueprint

import domain.model.ArchitectureNode
import domain.model.ArchitectureLayer
import domain.model.DependencyType
import domain.model.NodeDependency
import myanalogcodegenerator.domain.repository.ArchitectureRepository

/**
 * UseCase for managing architecture components and their relationships.
 * Uses ComponentFactory to create components and ArchitectureModel to manage state.
 */
class ArchitectureUseCase(
    private val architectureModel: ArchitectureRepository,
) {
    /**
     * Creates a new feature structure and adds it to the architecture model
     */
    fun createFeature(name: String, includeViewModel: Boolean = false) {
        val nodes = ComponentFactory.createFeatureStructure(name, includeViewModel)
        nodes.forEach { node ->
            architectureModel.addNode(node)
        }
    }

    /**
     * Creates a new UseCase and adds it to the architecture model
     */
    fun createUseCase(name: String) {
        val useCase = ComponentFactory.createUseCase(name)
        architectureModel.addNode(useCase)
    }

    /**
     * Creates a new View and adds it to the architecture model
     */
    fun createView(name: String) {
        val view = ComponentFactory.createView(name)
        architectureModel.addNode(view)
    }

    /**
     * Creates a new Presenter and adds it to the architecture model
     */
    fun createPresenter(name: String) {
        val presenter = ComponentFactory.createPresenter(name)
        architectureModel.addNode(presenter)
    }

    /**
     * Creates a new ViewModel and adds it to the architecture model
     */
    fun createViewModel(name: String) {
        val viewModel = ComponentFactory.createViewModel(name)
        architectureModel.addNode(viewModel)
    }

    /**
     * Adds a dependency between two nodes
     */
    fun addDependency(
        sourceId: String,
        targetId: String,
        type: DependencyType,
        description: String = "",
    ) {
        val sourceNode = architectureModel.getNodeById(sourceId)
        val targetNode = architectureModel.getNodeById(targetId)

        if (sourceNode != null && targetNode != null) {
            val dependency = NodeDependency(
                targetId = targetId,
                type = type,
                description = description
            )
            architectureModel.addDependency(sourceId, dependency)
        }
    }

    /**
     * Gets all nodes in the architecture
     */
    fun getAllNodes(): List<ArchitectureNode> {
        return architectureModel.getAllNodes()
    }

    /**
     * Gets nodes by layer
     */
    fun getNodesByLayer(layer: ArchitectureLayer): List<ArchitectureNode> {
        return architectureModel.getNodesByType(layer)
    }

    /**
     * Clears the entire architecture
     */
    fun clear() {
        architectureModel.clear()
    }
} 