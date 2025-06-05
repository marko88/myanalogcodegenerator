package domain.blueprint

import domain.model.*
import domain.repository.ArchitectureDefinitionModel
import domain.model.ArchitectureNode
import domain.model.NodeDependency

/**
 * Defines the blueprint for Clean Architecture using ArchitectureNode.
 * This represents the structure and relationships of Clean Architecture itself,
 * not specific project components.
 */
object CleanArchitectureBlueprint {
    fun create(): ArchitectureDefinitionModel {
        val architecture = ArchitectureDefinitionModel()

        // Add all nodes
        createNodes().forEach { architecture.addNode(it) }

        return architecture
    }

    private fun createNodes(): List<ArchitectureNode> {
        return listOf(
            // Presentation Layer - UI Components
            ArchitectureNode(
                id = "view",
                name = "View",
                layer = ArchitectureLayer.PRESENTATION,
                description = "UI component that displays data and handles user input",
                dependencies = listOf(
                    NodeDependency(
                        targetId = "presenter",
                        type = DependencyType.INJECTS,
                        description = "View injects and uses Presenter"
                    )
                ),
                attributes = listOf(
                    "presenter: Presenter",
                    "viewModel: ViewModel?"
                ),
                methods = listOf(
                    "showLoading()",
                    "hideLoading()",
                    "showError(message: String)",
                    "updateUI(data: Any)"
                )
            ),
            ArchitectureNode(
                id = "presenter",
                name = "Presenter",
                layer = ArchitectureLayer.PRESENTATION,
                description = "Handles business logic and updates the View",
                dependencies = listOf(
                    NodeDependency(
                        targetId = "view",
                        type = DependencyType.USES,
                        description = "Presenter updates View"
                    ),
                    NodeDependency(
                        targetId = "viewModel",
                        type = DependencyType.USES,
                        description = "Presenter can use ViewModel for state management"
                    )
                )
            ),
            ArchitectureNode(
                id = "viewModel",
                name = "ViewModel",
                layer = ArchitectureLayer.PRESENTATION,
                description = "Manages UI-related data and survives configuration changes"
            ),

            // Domain Layer
            ArchitectureNode(
                id = "useCase",
                name = "UseCase",
                layer = ArchitectureLayer.DOMAIN,
                description = "Single responsibility business logic unit"
            ),

            // Data Layer
            ArchitectureNode(
                id = "repository",
                name = "Repository",
                layer = ArchitectureLayer.DATA,
                description = "Generic repository interface for data operations",
                dependencies = listOf(
                    NodeDependency(
                        targetId = "service",
                        type = DependencyType.INJECTS,
                        description = "Repository uses Service for remote data"
                    )
                )
            ),
            ArchitectureNode(
                id = "service",
                name = "Service",
                layer = ArchitectureLayer.DATA,
                description = "Handles remote data operations"
            )
        )
    }
} 