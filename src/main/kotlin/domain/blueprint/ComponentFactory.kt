package domain.blueprint

import domain.model.ArchitectureNode
import domain.model.ArchitectureLayer
import domain.model.DependencyType
import domain.model.NodeDependency
import androidx.compose.ui.graphics.Color

/**
 * Factory class for creating Clean Architecture components.
 * Handles the creation of View, Presenter, ViewModel, and UseCase components
 * with their respective dependencies and relationships.
 */
class ComponentFactory {
    companion object {
        fun createView(name: String): ArchitectureNode {
            return ArchitectureNode(
                id = name,
                name = name,
                layer = ArchitectureLayer.PRESENTATION
            )
        }

        fun createPresenter(name: String): ArchitectureNode {
            return ArchitectureNode(
                id = name,
                name = name,
                layer = ArchitectureLayer.PRESENTATION
            )
        }

        fun createViewModel(name: String): ArchitectureNode {
            return ArchitectureNode(
                id = name,
                name = name,
                layer = ArchitectureLayer.PRESENTATION
            )
        }

        fun createUseCase(name: String): ArchitectureNode {
            return ArchitectureNode(
                id = name,
                name = name,
                layer = ArchitectureLayer.DOMAIN
            )
        }

        /**
         * Creates a complete feature structure with View, Presenter, and optional ViewModel.
         * Automatically creates dependencies between components.
         *
         * @param name Base name for the feature (e.g., "UserProfile" will create "UserProfileView", "UserProfilePresenter", etc.)
         * @param includeViewModel Whether to include a ViewModel in the structure
         * @return List of created nodes in order: [View, Presenter, ViewModel?]
         */
        fun createFeatureStructure(name: String, includeViewModel: Boolean = false): List<ArchitectureNode> {
            val view = ArchitectureNode(
                id = "${name}View",
                name = "${name}View",
                layer = ArchitectureLayer.PRESENTATION,
                dependencies = listOf(
                    NodeDependency(
                        targetId = "${name}Presenter",
                        type = DependencyType.INJECTS,
                        description = "View injects and uses Presenter"
                    )
                )
            )

            val presenter = ArchitectureNode(
                id = "${name}Presenter",
                name = "${name}Presenter",
                layer = ArchitectureLayer.PRESENTATION,
                dependencies = mutableListOf<NodeDependency>().apply {
                    add(
                        NodeDependency(
                            targetId = "${name}View",
                            type = DependencyType.USES,
                            description = "Presenter updates View"
                        )
                    )
                    if (includeViewModel) {
                        add(
                            NodeDependency(
                                targetId = "${name}ViewModel",
                                type = DependencyType.USES,
                                description = "Presenter uses ViewModel for state management"
                            )
                        )
                    }
                }
            )

            val nodes = mutableListOf(view, presenter)

            if (includeViewModel) {
                val viewModel = ArchitectureNode(
                    id = "${name}ViewModel",
                    name = "${name}ViewModel",
                    layer = ArchitectureLayer.PRESENTATION
                )
                nodes.add(viewModel)
            }

            return nodes
        }
    }
} 