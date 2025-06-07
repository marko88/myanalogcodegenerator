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
            id = "presenter1",
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
            .addNode(splashScreen)
            .addNode(createUserScreen)
            .addNode(loginScreen)
            .addNode(collectionScreen)
            .addNode(vinylDetailsScreen)
            .addNode(userDetailsScreen)

            .addNode(splashPresenter)
            .addNode(createUserPresenter)
            .addNode(loginPresenter)
            .addNode(collectionPresenter)
            .addNode(vinylDetailsPresenter)
            .addNode(userDetailsPresenter)

            .addNode(createUserViewModel)
            .addNode(
                loginViewModel.copy(
                    dependencies = listOf(
                        NodeDependency(
                            "repo1",
                            DependencyType.CONSTRUCTOR_INJECTION
                        )
                    )
                )
            )
            .addNode(
                collectionViewModel.copy(
                    dependencies = listOf(
                        NodeDependency(
                            "uc1",
                            DependencyType.CONSTRUCTOR_INJECTION
                        )
                    )
                )
            )
            .addNode(
                vinylDetailsViewModel.copy(
                    dependencies = listOf(
                        NodeDependency(
                            "uc2",
                            DependencyType.CONSTRUCTOR_INJECTION
                        )
                    )
                )
            )

            .addNode(
                getCollectionUseCase.copy(
                    dependencies = listOf(
                        NodeDependency(
                            "repo2",
                            DependencyType.CONSTRUCTOR_INJECTION
                        )
                    )
                )
            )
            .addNode(
                getVinylDetailsUseCase.copy(
                    dependencies = listOf(
                        NodeDependency(
                            "repo2",
                            DependencyType.CONSTRUCTOR_INJECTION
                        )
                    )
                )
            )

            .addNode(userRepository)
            .addNode(
                vinylRepository.copy(
                    dependencies = listOf(
                        NodeDependency("api1", DependencyType.NETWORK_CALL),
                        NodeDependency("api2", DependencyType.NETWORK_CALL)
                    )
                )
            )

            .addNode(discogsService)
            .addNode(junoService)
    }
}


