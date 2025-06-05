package myanalogcodegenerator.domain.model

import androidx.compose.ui.geometry.Offset
import domain.model.*
import domain.repository.ArchitectureDatabase

object DemoData {

    fun createDemoArchitecture(): ArchitectureDatabase {
        val presenter = ArchitectureNode(
            id = "presenter1",
            name = "LibraryPresenter",
            layer = ArchitectureLayer.PRESENTATION,
            position = Offset(100f, 100f),
            methods = listOf(
                NodeMethod("renderLibrary", "Unit", semantics = DataFlowSemantics.Event)
            )
        )

        val viewModel = ArchitectureNode(
            id = "viewModel1",
            name = "LibraryViewModel",
            layer = ArchitectureLayer.PRESENTATION,
            position = Offset(250f, 100f),
            methods = listOf(
                NodeMethod("loadBooks", "Unit", semantics = DataFlowSemantics.Command),
                NodeMethod("observeBooks", "Flow<List<Book>>", semantics = DataFlowSemantics.State)
            )
        )

        val useCase = ArchitectureNode(
            id = "useCase1",
            name = "GetBooksUseCase",
            layer = ArchitectureLayer.DOMAIN,
            position = Offset(400f, 200f),
            methods = listOf(
                NodeMethod("execute", "Flow<List<Book>>", semantics = DataFlowSemantics.Response)
            )
        )

        val repository = ArchitectureNode(
            id = "repo1",
            name = "BookRepository",
            layer = ArchitectureLayer.DATA,
            position = Offset(550f, 300f),
            methods = listOf(
                NodeMethod("getAllBooks", "Flow<List<Book>>", semantics = DataFlowSemantics.Response)
            )
        )

        val database = ArchitectureNode(
            id = "db1",
            name = "BookDatabase",
            layer = ArchitectureLayer.DATA,
            position = Offset(700f, 400f),
            attributes = listOf(
                NodeAttribute("books", "List<Book>", isReactive = true, semantics = DataFlowSemantics.State)
            )
        )

        return ArchitectureDatabase()
            .addNode(presenter.copy(dependencies = listOf(
                NodeDependency("viewModel1", DependencyType.CONSTRUCTOR_INJECTION)
            )))
            .addNode(viewModel.copy(dependencies = listOf(
                NodeDependency("useCase1", DependencyType.CONSTRUCTOR_INJECTION)
            )))
            .addNode(useCase.copy(dependencies = listOf(
                NodeDependency("repo1", DependencyType.CONSTRUCTOR_INJECTION)
            )))
            .addNode(repository.copy(dependencies = listOf(
                NodeDependency("db1", DependencyType.CONSTRUCTOR_INJECTION)
            )))
            .addNode(database)
            .addDataFlow(
                DataFlowConnection(
                    fromNodeId = "useCase1",
                    fromSymbol = "execute",
                    toNodeId = "viewModel1",
                    toSymbol = "observeBooks",
                    semantics = DataFlowSemantics.Stream
                )
            )
            .addDataFlow(
                DataFlowConnection(
                    fromNodeId = "viewModel1",
                    fromSymbol = "observeBooks",
                    toNodeId = "presenter1",
                    toSymbol = "renderLibrary",
                    semantics = DataFlowSemantics.Event
                )
            )
    }
}
