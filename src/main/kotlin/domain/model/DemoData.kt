package myanalogcodegenerator.domain.model

import domain.model.*
import domain.repository.ArchitectureDatabase

object DemoData {

    fun createDemoArchitecture(): ArchitectureDatabase {
        val splashScreen = ArchitectureNode(
            id = "view1",
            name = "SplashScreen",
            layer = ArchitectureLayer.PRESENTATION,
            type = ArchitectureNodeType.VIEW,
            methods = listOf(NodeMethod("navigateToNext", "Unit", semantics = DataFlowSemantics.Event))
        )

        val createUserScreen = ArchitectureNode(
            id = "view2",
            name = "CreateUserScreen",
            layer = ArchitectureLayer.PRESENTATION,
            type = ArchitectureNodeType.VIEW,
            methods = listOf(NodeMethod("onCreateClick", "Unit", semantics = DataFlowSemantics.Command)),
            attributes = listOf(
                NodeAttribute(
                    "username",
                    "String",
                    isReactive = true,
                    semantics = DataFlowSemantics.State
                )
            )
        )

        val loginScreen = ArchitectureNode(
            id = "view3",
            name = "LoginScreen",
            layer = ArchitectureLayer.PRESENTATION,
            type = ArchitectureNodeType.VIEW,
            methods = listOf(NodeMethod("onLoginClick", "Unit", semantics = DataFlowSemantics.Command)),
            attributes = listOf(NodeAttribute("user", "User?", isReactive = true, semantics = DataFlowSemantics.State))
        )

        val collectionScreen = ArchitectureNode(
            id = "view4",
            name = "MyCollectionScreen",
            layer = ArchitectureLayer.PRESENTATION,
            type = ArchitectureNodeType.VIEW,
            methods = listOf(
                NodeMethod(
                    "onVinylSelected",
                    "Unit",
                    listOf("vinylId" to "String"),
                    semantics = DataFlowSemantics.Event
                )
            ),
            attributes = listOf(
                NodeAttribute(
                    "collection",
                    "List<Vinyl>",
                    isReactive = true,
                    semantics = DataFlowSemantics.State
                )
            )
        )

        val vinylDetailsScreen = ArchitectureNode(
            id = "view5",
            name = "VinylDetailsScreen",
            layer = ArchitectureLayer.PRESENTATION,
            type = ArchitectureNodeType.VIEW,
            attributes = listOf(NodeAttribute("vinyl", "Vinyl", isReactive = true, semantics = DataFlowSemantics.State))
        )

        val userDetailsScreen = ArchitectureNode(
            id = "view6",
            name = "UserDetailsScreen",
            layer = ArchitectureLayer.PRESENTATION,
            type = ArchitectureNodeType.VIEW,
            attributes = listOf(NodeAttribute("user", "User", isReactive = true, semantics = DataFlowSemantics.State))
        )

        // Presenters
        val splashPresenter = ArchitectureNode(
            id = "SplashPresenter",
            name = "SplashPresenter",
            layer = ArchitectureLayer.PRESENTATION,
            type = ArchitectureNodeType.PRESENTER,
            methods = listOf(NodeMethod("handleStartup", "Unit", semantics = DataFlowSemantics.Command))
        )

        val createUserPresenter = ArchitectureNode(
            id = "presenter2",
            name = "CreateUserPresenter",
            layer = ArchitectureLayer.PRESENTATION,
            type = ArchitectureNodeType.PRESENTER,
            methods = listOf(NodeMethod("submitUser", "Unit", semantics = DataFlowSemantics.Command))
        )

        val loginPresenter = ArchitectureNode(
            id = "presenter3",
            name = "LoginPresenter",
            layer = ArchitectureLayer.PRESENTATION,
            type = ArchitectureNodeType.PRESENTER,
            methods = listOf(NodeMethod("performLogin", "Unit", semantics = DataFlowSemantics.Command))
        )

        val collectionPresenter = ArchitectureNode(
            id = "presenter4",
            name = "CollectionPresenter",
            layer = ArchitectureLayer.PRESENTATION,
            type = ArchitectureNodeType.PRESENTER,
            methods = listOf(NodeMethod("showCollection", "Unit", semantics = DataFlowSemantics.Event))
        )

        val vinylDetailsPresenter = ArchitectureNode(
            id = "presenter5",
            name = "VinylDetailsPresenter",
            layer = ArchitectureLayer.PRESENTATION,
            type = ArchitectureNodeType.PRESENTER,
            methods = listOf(NodeMethod("renderDetails", "Unit", semantics = DataFlowSemantics.Event))
        )

        val userDetailsPresenter = ArchitectureNode(
            id = "presenter6",
            name = "UserDetailsPresenter",
            layer = ArchitectureLayer.PRESENTATION,
            type = ArchitectureNodeType.PRESENTER,
            methods = listOf(NodeMethod("renderUser", "Unit", semantics = DataFlowSemantics.Event))
        )

        // ViewModels
        val createUserViewModel = ArchitectureNode(
            id = "vm1",
            name = "CreateUserViewModel",
            layer = ArchitectureLayer.PRESENTATION,
            type = ArchitectureNodeType.VIEWMODEL,
            methods = listOf(NodeMethod("createUser", "Unit", semantics = DataFlowSemantics.Command))
        )

        val loginViewModel = ArchitectureNode(
            id = "vm2",
            name = "LoginViewModel",
            layer = ArchitectureLayer.PRESENTATION,
            type = ArchitectureNodeType.VIEWMODEL,
            methods = listOf(NodeMethod("login", "Unit", semantics = DataFlowSemantics.Command))
        )

        val collectionViewModel = ArchitectureNode(
            id = "vm3",
            name = "CollectionViewModel",
            layer = ArchitectureLayer.PRESENTATION,
            type = ArchitectureNodeType.VIEWMODEL,
            methods = listOf(NodeMethod("loadCollection", "Unit", semantics = DataFlowSemantics.Command)),
            attributes = listOf(
                NodeAttribute(
                    "collection",
                    "Flow<List<Vinyl>>",
                    isReactive = true,
                    semantics = DataFlowSemantics.State
                )
            )
        )

        val vinylDetailsViewModel = ArchitectureNode(
            id = "vm4",
            name = "VinylDetailsViewModel",
            layer = ArchitectureLayer.PRESENTATION,
            type = ArchitectureNodeType.VIEWMODEL,
            methods = listOf(NodeMethod("loadVinylDetails", "Unit", semantics = DataFlowSemantics.Command)),
            attributes = listOf(
                NodeAttribute(
                    "vinyl",
                    "Flow<Vinyl>",
                    isReactive = true,
                    semantics = DataFlowSemantics.State
                )
            )
        )

        // Domain Layer (Use Cases)
        val getCollectionUseCase = ArchitectureNode(
            id = "uc1",
            name = "GetCollectionUseCase",
            layer = ArchitectureLayer.DOMAIN,
            type = ArchitectureNodeType.USE_CASE,
            methods = listOf(NodeMethod("execute", "Flow<List<Vinyl>>", semantics = DataFlowSemantics.Response))
        )

        val getVinylDetailsUseCase = ArchitectureNode(
            id = "uc2",
            name = "GetVinylDetailsUseCase",
            layer = ArchitectureLayer.DOMAIN,
            type = ArchitectureNodeType.USE_CASE,
            methods = listOf(NodeMethod("execute", "Flow<Vinyl>", semantics = DataFlowSemantics.Response))
        )

        // Data Layer
        val userRepository = ArchitectureNode(
            id = "repo1",
            name = "UserRepository",
            layer = ArchitectureLayer.DATA,
            type = ArchitectureNodeType.REPOSITORY,
            methods = listOf(NodeMethod("login", "Flow<User>", semantics = DataFlowSemantics.Response))
        )

        val vinylRepository = ArchitectureNode(
            id = "repo2",
            name = "VinylRepository",
            layer = ArchitectureLayer.DATA,
            type = ArchitectureNodeType.REPOSITORY,
            methods = listOf(
                NodeMethod("getCollection", "Flow<List<Vinyl>>", semantics = DataFlowSemantics.Response),
                NodeMethod("getVinylDetails", "Flow<Vinyl>", semantics = DataFlowSemantics.Response)
            )
        )

        val discogsService = ArchitectureNode(
            id = "api1",
            name = "DiscogsServiceAPI",
            layer = ArchitectureLayer.DATA,
            type = ArchitectureNodeType.API,
            methods = listOf(NodeMethod("searchVinyl", "ApiResponse", semantics = DataFlowSemantics.Request))
        )

        val junoService = ArchitectureNode(
            id = "api2",
            name = "JunoServiceAPI",
            layer = ArchitectureLayer.DATA,
            type = ArchitectureNodeType.API,
            methods = listOf(NodeMethod("fetchDetails", "ApiResponse", semantics = DataFlowSemantics.Request))
        )

        return ArchitectureDatabase()
            // Views
            .addNode(
                splashScreen.copy(
                    dependencies = listOf(
                        NodeDependency(
                            "SplashPresenter",
                            DependencyType.CONSTRUCTOR_INJECTION
                        )
                    )
                )
            )
            .addNode(
                createUserScreen.copy(
                    dependencies = listOf(
                        NodeDependency(
                            "presenter2",
                            DependencyType.CONSTRUCTOR_INJECTION
                        )
                    )
                )
            )
            .addNode(
                loginScreen.copy(
                    dependencies = listOf(
                        NodeDependency(
                            "presenter3",
                            DependencyType.CONSTRUCTOR_INJECTION
                        )
                    )
                )
            )
            .addNode(
                collectionScreen.copy(
                    dependencies = listOf(
                        NodeDependency(
                            "presenter4",
                            DependencyType.USES
                        )
                    )
                )
            )
            .addNode(
                vinylDetailsScreen.copy(
                    dependencies = listOf(
                        NodeDependency(
                            "presenter5",
                            DependencyType.CONSTRUCTOR_INJECTION
                        )
                    )
                )
            )
            .addNode(
                userDetailsScreen.copy(
                    dependencies = listOf(
                        NodeDependency(
                            "presenter6",
                            DependencyType.USES
                        )
                    )
                )
            )

            // Presenters
            .addNode(
                splashPresenter.copy(
                    dependencies = listOf(
                        NodeDependency(
                            "vm1",
                            DependencyType.CONSTRUCTOR_INJECTION
                        )
                    )
                )
            )
            .addNode(
                createUserPresenter.copy(
                    dependencies = listOf(
                        NodeDependency(
                            "vm1",
                            DependencyType.CONSTRUCTOR_INJECTION
                        )
                    )
                )
            )
            .addNode(
                loginPresenter.copy(
                    dependencies = listOf(
                        NodeDependency(
                            "vm2",
                            DependencyType.CONSTRUCTOR_INJECTION
                        )
                    )
                )
            )
            .addNode(
                collectionPresenter.copy(
                    dependencies = listOf(
                        NodeDependency(
                            "vm3",
                            DependencyType.CONSTRUCTOR_INJECTION
                        )
                    )
                )
            )
            .addNode(
                vinylDetailsPresenter.copy(
                    dependencies = listOf(
                        NodeDependency(
                            "vm4",
                            DependencyType.CONSTRUCTOR_INJECTION
                        )
                    )
                )
            )
            .addNode(userDetailsPresenter) // No ViewModel for user details yet

            // ViewModels
            .addNode(createUserViewModel) // No use case dependency defined
            .addNode(
                loginViewModel.copy(
                    dependencies = listOf(
                        NodeDependency("repo1", DependencyType.CONSTRUCTOR_INJECTION)
                    )
                )
            )
            .addNode(
                collectionViewModel.copy(
                    dependencies = listOf(
                        NodeDependency("uc1", DependencyType.CONSTRUCTOR_INJECTION)
                    )
                )
            )
            .addNode(
                vinylDetailsViewModel.copy(
                    dependencies = listOf(
                        NodeDependency("uc2", DependencyType.CONSTRUCTOR_INJECTION)
                    )
                )
            )

            // Use Cases
            .addNode(
                getCollectionUseCase.copy(
                    dependencies = listOf(
                        NodeDependency("repo2", DependencyType.CONSTRUCTOR_INJECTION)
                    )
                )
            )
            .addNode(
                getVinylDetailsUseCase.copy(
                    dependencies = listOf(
                        NodeDependency("repo2", DependencyType.CONSTRUCTOR_INJECTION)
                    )
                )
            )

            // Repositories
            .addNode(userRepository)
            .addNode(
                vinylRepository.copy(
                    dependencies = listOf(
                        NodeDependency("api1", DependencyType.NETWORK_CALL),
                        NodeDependency("api2", DependencyType.NETWORK_CALL)
                    )
                )
            )

            // APIs
            .addNode(discogsService)
            .addNode(junoService)
            .addDataFlow(
                DataFlowConnection(
                    fromNodeId = "vm2",
                    fromSymbol = "login",
                    toNodeId = "view3",
                    toSymbol = "user",
                    semantics = DataFlowSemantics.Response
                )
            )
            .addDataFlow(
                DataFlowConnection(
                    fromNodeId = "vm3",
                    fromSymbol = "collection",
                    toNodeId = "view4",
                    toSymbol = "collection",
                    semantics = DataFlowSemantics.State
                )
            )
            .addDataFlow(
                DataFlowConnection(
                    fromNodeId = "vm4",
                    fromSymbol = "vinyl",
                    toNodeId = "view5",
                    toSymbol = "vinyl",
                    semantics = DataFlowSemantics.State
                )
            )
            .addDataFlow(
                DataFlowConnection(
                    fromNodeId = "view2",
                    fromSymbol = "onCreateClick",
                    toNodeId = "vm1",
                    toSymbol = "createUser",
                    semantics = DataFlowSemantics.Command
                )
            )
            .addDataFlow(
                DataFlowConnection(
                    fromNodeId = "view3",
                    fromSymbol = "onLoginClick",
                    toNodeId = "vm2",
                    toSymbol = "login",
                    semantics = DataFlowSemantics.Command
                )
            )
            .addDataFlow(
                DataFlowConnection(
                    fromNodeId = "view4",
                    fromSymbol = "onVinylSelected",
                    toNodeId = "vm4",
                    toSymbol = "loadVinylDetails",
                    semantics = DataFlowSemantics.Command
                )
            )
            .addDataFlow(
                DataFlowConnection(
                    fromNodeId = "uc1",
                    fromSymbol = "execute",
                    toNodeId = "vm3",
                    toSymbol = "loadCollection",
                    semantics = DataFlowSemantics.Response
                )
            )
            .addDataFlow(
                DataFlowConnection(
                    fromNodeId = "uc2",
                    fromSymbol = "execute",
                    toNodeId = "vm4",
                    toSymbol = "loadVinylDetails",
                    semantics = DataFlowSemantics.Response
                )
            )
    }
}


