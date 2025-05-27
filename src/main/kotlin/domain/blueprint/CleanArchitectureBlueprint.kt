package domain.blueprint

import domain.model.ArchitectureNode
import domain.model.ArchitectureDefinitionModel
import domain.model.ArchitectureLayer
import domain.model.DependencyType
import domain.model.NodeDependency
import androidx.compose.ui.graphics.Color

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
            // Presentation Layer - Base Components
            ArchitectureNode(
                id = "lifecycleAware",
                name = "LifecycleAware",
                packageName = "presentation.lifecycle",
                layer = ArchitectureLayer.PRESENTATION,
                description = "Interface for components that need to handle lifecycle events",
                color = Color(0xFFB983FF),
                dependencies = emptyList(),
                attributes = listOf(
                    "lifecycleState: LifecycleState",
                    "lifecycleOwner: LifecycleOwner?"
                ),
                methods = listOf(
                    "onCreate()",
                    "onStart()",
                    "onResume()",
                    "onPause()",
                    "onStop()",
                    "onDestroy()",
                    "registerLifecycleOwner(owner: LifecycleOwner)",
                    "unregisterLifecycleOwner()"
                )
            ),

            // Presentation Layer - UI Components
            ArchitectureNode(
                id = "view",
                name = "View",
                packageName = "presentation",
                layer = ArchitectureLayer.PRESENTATION,
                description = "UI component that displays data and handles user input",
                color = Color(0xFFB983FF),
                dependencies = listOf(
                    NodeDependency(
                        targetId = "presenter",
                        type = DependencyType.INJECTS,
                        description = "View injects and uses Presenter"
                    ),
                    NodeDependency(
                        targetId = "lifecycleAware",
                        type = DependencyType.IMPLEMENTS,
                        description = "View implements lifecycle awareness"
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
                packageName = "presentation",
                layer = ArchitectureLayer.PRESENTATION,
                description = "Handles business logic and updates the View",
                color = Color(0xFFB983FF),
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
                    ),
                    NodeDependency(
                        targetId = "useCase",
                        type = DependencyType.INJECTS,
                        description = "Presenter injects and uses UseCases"
                    ),
                    NodeDependency(
                        targetId = "lifecycleAware",
                        type = DependencyType.IMPLEMENTS,
                        description = "Presenter implements lifecycle awareness"
                    )
                ),
                attributes = listOf(
                    "view: View",
                    "viewModel: ViewModel?",
                    "useCases: List<UseCase>"
                ),
                methods = listOf(
                    "attachView(view: View)",
                    "detachView()",
                    "onViewCreated()",
                    "onViewDestroyed()"
                )
            ),
            ArchitectureNode(
                id = "viewModel",
                name = "ViewModel",
                packageName = "presentation",
                layer = ArchitectureLayer.PRESENTATION,
                description = "Manages UI-related data and survives configuration changes",
                color = Color(0xFFB983FF),
                dependencies = listOf(
                    NodeDependency(
                        targetId = "useCase",
                        type = DependencyType.INJECTS,
                        description = "ViewModel injects and uses UseCases"
                    ),
                    NodeDependency(
                        targetId = "lifecycleAware",
                        type = DependencyType.IMPLEMENTS,
                        description = "ViewModel implements lifecycle awareness"
                    )
                ),
                attributes = listOf(
                    "state: StateFlow<State>",
                    "useCases: List<UseCase>"
                ),
                methods = listOf(
                    "init()",
                    "onCleared()",
                    "updateState(newState: State)"
                )
            ),

            // Domain Layer
            ArchitectureNode(
                id = "useCase",
                name = "UseCase",
                packageName = "domain",
                layer = ArchitectureLayer.DOMAIN,
                description = "Single responsibility business logic unit",
                color = Color(0xFF5DE2E6),
                dependencies = listOf(
                    NodeDependency(
                        targetId = "repository",
                        type = DependencyType.INJECTS,
                        description = "UseCase injects and uses Repository"
                    )
                ),
                attributes = listOf(
                    "repository: Repository",
                    "dispatchers: CoroutineDispatchers"
                ),
                methods = listOf(
                    "execute(params: Params): Flow<Result<Result>>"
                )
            ),

            // Data Layer
            ArchitectureNode(
                id = "repository",
                name = "Repository",
                packageName = "data",
                layer = ArchitectureLayer.DATA,
                description = "Generic repository interface for data operations",
                color = Color(0xFFFAED7D),
                dependencies = listOf(
                    NodeDependency(
                        targetId = "service",
                        type = DependencyType.INJECTS,
                        description = "Repository uses Service for remote data"
                    )
                ),
                attributes = listOf(
                    "service: Service",
                    "localDataSource: LocalDataSource?"
                ),
                methods = listOf(
                    "get(id: String): Flow<Result<Entity>>",
                    "getAll(): Flow<Result<List<Entity>>>",
                    "save(entity: Entity): Flow<Result<Unit>>",
                    "delete(id: String): Flow<Result<Unit>>",
                    "update(entity: Entity): Flow<Result<Unit>>",
                    "query(params: QueryParams): Flow<Result<List<Entity>>>"
                )
            ),
            ArchitectureNode(
                id = "service",
                name = "Service",
                packageName = "data",
                layer = ArchitectureLayer.DATA,
                description = "Handles remote data operations",
                color = Color(0xFFFAED7D),
                dependencies = listOf(
                    NodeDependency(
                        targetId = "retrofit",
                        type = DependencyType.USES,
                        description = "Service uses Retrofit for network calls"
                    )
                ),
                attributes = listOf(
                    "api: Api",
                    "retrofit: Retrofit"
                ),
                methods = listOf(
                    "get(id: String): Flow<Result<Entity>>",
                    "getAll(): Flow<Result<List<Entity>>>",
                    "save(entity: Entity): Flow<Result<Unit>>",
                    "delete(id: String): Flow<Result<Unit>>"
                )
            )
        )
    }
} 