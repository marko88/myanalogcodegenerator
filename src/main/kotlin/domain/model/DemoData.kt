package myanalogcodegenerator.domain.model

import domain.model.*
import domain.repository.ArchitectureDatabase

object DemoData {

    fun createDemoArchitecture(): ArchitectureDatabase {
        val presenter1 = ArchitectureNode(
            id = "presenter1",
            name = "LibraryPresenter",
            layer = ArchitectureLayer.PRESENTATION,
            type = ArchitectureNodeType.PRESENTER,
            methods = listOf(
                NodeMethod("renderLibrary", "Unit", semantics = DataFlowSemantics.Event)
            )
        )

        val presenter2 = ArchitectureNode(
            id = "presenter2",
            name = "AccountPresenter",
            layer = ArchitectureLayer.PRESENTATION,
            type = ArchitectureNodeType.PRESENTER,
            methods = listOf(
                NodeMethod("renderUser", "Unit", semantics = DataFlowSemantics.Event)
            )
        )

        val view1 = ArchitectureNode(
            id = "view1",
            name = "LibraryScreen",
            layer = ArchitectureLayer.PRESENTATION,
            type = ArchitectureNodeType.VIEW,
            methods = listOf(
                NodeMethod("onBookClick", "Unit", listOf("bookId" to "String"), semantics = DataFlowSemantics.Event)
            ),
            attributes = listOf(
                NodeAttribute("books", "List<Book>", isReactive = true, semantics = DataFlowSemantics.State)
            )
        )

        val view2 = ArchitectureNode(
            id = "view2",
            name = "AccountScreen",
            layer = ArchitectureLayer.PRESENTATION,
            type = ArchitectureNodeType.VIEW,
            methods = listOf(
                NodeMethod("onLogout", "Unit", semantics = DataFlowSemantics.Command)
            ),
            attributes = listOf(
                NodeAttribute("userName", "String", isReactive = true, semantics = DataFlowSemantics.State)
            )
        )

        val viewModel1 = ArchitectureNode(
            id = "viewModel1",
            name = "LibraryViewModel",
            layer = ArchitectureLayer.PRESENTATION,
            type = ArchitectureNodeType.VIEWMODEL,
            attributes = listOf(
                NodeAttribute("books", "Flow<List<Book>>", isReactive = true, semantics = DataFlowSemantics.State),
                NodeAttribute("loading", "Boolean", isReactive = true, semantics = DataFlowSemantics.State)
            ),
            methods = listOf(
                NodeMethod("loadBooks", "Unit", semantics = DataFlowSemantics.Command),
                NodeMethod("refreshBooks", "Unit", semantics = DataFlowSemantics.Command)
            )
        )

        val viewModel2 = ArchitectureNode(
            id = "viewModel2",
            name = "AccountViewModel",
            layer = ArchitectureLayer.PRESENTATION,
            type = ArchitectureNodeType.VIEWMODEL,
            attributes = listOf(
                NodeAttribute("user", "Flow<User>", isReactive = true, semantics = DataFlowSemantics.State)
            ),
            methods = listOf(
                NodeMethod("loadUser", "Unit", semantics = DataFlowSemantics.Command)
            )
        )

        val useCase1 = ArchitectureNode(
            id = "useCase1",
            name = "GetBooksUseCase",
            layer = ArchitectureLayer.DOMAIN,
            type = ArchitectureNodeType.USE_CASE,
            methods = listOf(
                NodeMethod("execute", "Flow<List<Book>>", semantics = DataFlowSemantics.Response)
            )
        )

        val useCase2 = ArchitectureNode(
            id = "useCase2",
            name = "GetUserUseCase",
            layer = ArchitectureLayer.DOMAIN,
            type = ArchitectureNodeType.USE_CASE,
            methods = listOf(
                NodeMethod("execute", "Flow<User>", semantics = DataFlowSemantics.Response)
            )
        )

        val repository1 = ArchitectureNode(
            id = "repo1",
            name = "BookRepository",
            layer = ArchitectureLayer.DATA,
            type = ArchitectureNodeType.REPOSITORY,
            methods = listOf(
                NodeMethod("getAllBooks", "Flow<List<Book>>", semantics = DataFlowSemantics.Response)
            )
        )

        val repository2 = ArchitectureNode(
            id = "repo2",
            name = "UserRepository",
            layer = ArchitectureLayer.DATA,
            type = ArchitectureNodeType.REPOSITORY,
            methods = listOf(
                NodeMethod("getUser", "Flow<User>", semantics = DataFlowSemantics.Response)
            )
        )

        val database = ArchitectureNode(
            id = "db1",
            name = "AppDatabase",
            layer = ArchitectureLayer.DATA,
            type = ArchitectureNodeType.DATABASE,
            attributes = listOf(
                NodeAttribute("books", "List<Book>", isReactive = true, semantics = DataFlowSemantics.State)
            )
        )

        // Define data flow connections
        val dataFlow1 = DataFlowConnection(
            fromNodeId = "viewModel1",
            fromSymbol = "books",
            toNodeId = "view1",
            toSymbol = "books",
            semantics = DataFlowSemantics.State
        )

        val dataFlow2 = DataFlowConnection(
            fromNodeId = "viewModel2",
            fromSymbol = "user",
            toNodeId = "view2",
            toSymbol = "userName",
            semantics = DataFlowSemantics.State
        )

        val dataFlow3 = DataFlowConnection(
            fromNodeId = "useCase1",
            fromSymbol = "execute",
            toNodeId = "viewModel1",
            toSymbol = "loadBooks",
            semantics = DataFlowSemantics.Response
        )

        val dataFlow4 = DataFlowConnection(
            fromNodeId = "view1",
            fromSymbol = "onBookClick",
            toNodeId = "viewModel1",
            toSymbol = "loadBooks",
            semantics = DataFlowSemantics.Command
        )

        return ArchitectureDatabase()
            .addNode(presenter1.copy(dependencies = listOf(NodeDependency("viewModel1", DependencyType.CONSTRUCTOR_INJECTION))))
            .addNode(presenter2.copy(dependencies = listOf(NodeDependency("viewModel2", DependencyType.CONSTRUCTOR_INJECTION))))
            .addNode(view1.copy(dependencies = listOf(NodeDependency("viewModel1", DependencyType.OBSERVES))))
            .addNode(view2.copy(dependencies = listOf(NodeDependency("viewModel2", DependencyType.OBSERVES))))
            .addNode(viewModel1.copy(dependencies = listOf(NodeDependency("useCase1", DependencyType.CONSTRUCTOR_INJECTION))))
            .addNode(viewModel2.copy(dependencies = listOf(NodeDependency("useCase2", DependencyType.CONSTRUCTOR_INJECTION))))
            .addNode(useCase1.copy(dependencies = listOf(NodeDependency("repo1", DependencyType.CONSTRUCTOR_INJECTION))))
            .addNode(useCase2.copy(dependencies = listOf(NodeDependency("repo2", DependencyType.CONSTRUCTOR_INJECTION))))
            .addNode(repository1.copy(dependencies = listOf(NodeDependency("db1", DependencyType.CONSTRUCTOR_INJECTION))))
            .addNode(repository2.copy(dependencies = listOf(NodeDependency("db1", DependencyType.CONSTRUCTOR_INJECTION))))
            .addNode(database)
            .addDataFlow(dataFlow1)
            .addDataFlow(dataFlow2)
            .addDataFlow(dataFlow3)
            .addDataFlow(dataFlow4)
    }
}
