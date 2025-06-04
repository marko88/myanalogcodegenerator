package domain.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

object TestData {
    fun createBookLibraryArchitecture(): ArchitectureDefinitionModel {
        val architecture = ArchitectureDefinitionModel()

        // Presentation Layer
        val booksLibraryViewModel = ArchitectureNode(
            id = "books_library_vm",
            name = "BooksLibraryViewModel",
            layer = ArchitectureLayer.PRESENTATION,
            description = "Main view model for the books library screen",
            position = Offset(400f, 100f),
            dependencies = listOf(
                NodeDependency(
                    targetId = "get_all_books_uc",
                    type = DependencyType.USES,
                    description = "Fetches all books"
                ),
                NodeDependency(
                    targetId = "get_rented_books_uc",
                    type = DependencyType.USES,
                    description = "Fetches rented books"
                ),
                NodeDependency(
                    targetId = "rent_book_uc",
                    type = DependencyType.USES,
                    description = "Handles book rental"
                )
            ),
            attributes = listOf("books: List<Book>", "selectedBook: Book?", "isLoading: Boolean", "error: String?"),
            methods = listOf("loadBooks()", "selectBook(book: Book)", "rentBook(book: Book)", "showError(message: String)")
        )

        // Domain Layer - Use Cases
        val getAllBooksUseCase = ArchitectureNode(
            id = "get_all_books_uc",
            name = "GetAllBooksUseCase",
            layer = ArchitectureLayer.DOMAIN,
            description = "Use case for retrieving all books",
            position = Offset(200f, 200f),
            dependencies = listOf(
                NodeDependency(
                    targetId = "books_repository",
                    type = DependencyType.USES,
                    description = "Gets books from repository"
                )
            ),
            attributes = listOf("repository: BooksRepository", "cache: List<Book>"),
            methods = listOf("execute(): List<Book>", "clearCache()")
        )

        val getRentedBooksUseCase = ArchitectureNode(
            id = "get_rented_books_uc",
            name = "GetRentedBooksUseCase",
            layer = ArchitectureLayer.DOMAIN,
            description = "Use case for retrieving rented books",
            position = Offset(400f, 200f),
            dependencies = listOf(
                NodeDependency(
                    targetId = "books_repository",
                    type = DependencyType.USES,
                    description = "Gets rented books from repository"
                )
            ),
            attributes = listOf("repository: BooksRepository", "rentedBooks: List<Book>"),
            methods = listOf("execute(): List<Book>", "filterRentedBooks(): List<Book>")
        )

        val rentBookUseCase = ArchitectureNode(
            id = "rent_book_uc",
            name = "RentBookUseCase",
            layer = ArchitectureLayer.DOMAIN,
            description = "Use case for renting a book",
            position = Offset(600f, 200f),
            dependencies = listOf(
                NodeDependency(
                    targetId = "books_repository",
                    type = DependencyType.USES,
                    description = "Updates book rental status"
                ),
                NodeDependency(
                    targetId = "analytics_service",
                    type = DependencyType.USES,
                    description = "Tracks book rental events"
                )
            ),
            attributes = listOf("repository: BooksRepository", "analytics: AnalyticsService", "lastRented: Book?"),
            methods = listOf("execute(book: Book)", "validateRental(book: Book): Boolean")
        )

        // Data Layer - Repository
        val booksRepository = ArchitectureNode(
            id = "books_repository",
            name = "BooksRepository",
            layer = ArchitectureLayer.DATA,
            description = "Repository for book data operations",
            position = Offset(400f, 300f),
            dependencies = listOf(
                NodeDependency(
                    targetId = "books_local_ds",
                    type = DependencyType.USES,
                    description = "Local data source for books"
                ),
                NodeDependency(
                    targetId = "books_remote_ds",
                    type = DependencyType.USES,
                    description = "Remote data source for books"
                )
            ),
            attributes = listOf("localDataSource: BooksLocalDataSource", "remoteDataSource: BooksRemoteDataSource", "cache: List<Book>"),
            methods = listOf("getBooks(): List<Book>", "rentBook(book: Book)", "clearCache()", "findBookById(id: String): Book?")
        )

        // Data Layer - Data Sources
        val booksLocalDataSource = ArchitectureNode(
            id = "books_local_ds",
            name = "BooksLocalDataSource",
            layer = ArchitectureLayer.DATA,
            description = "Local data source for books using Room",
            position = Offset(200f, 400f),
            dependencies = listOf(
                NodeDependency(
                    targetId = "room_database",
                    type = DependencyType.USES,
                    description = "Room database for local storage"
                )
            ),
            attributes = listOf("db: RoomDatabase", "lastSync: Long"),
            methods = listOf("getBooks(): List<Book>", "saveBook(book: Book)", "deleteBook(book: Book)")
        )

        val booksRemoteDataSource = ArchitectureNode(
            id = "books_remote_ds",
            name = "BooksRemoteDataSource",
            layer = ArchitectureLayer.DATA,
            description = "Remote data source for books using Retrofit",
            position = Offset(600f, 400f),
            dependencies = listOf(
                NodeDependency(
                    targetId = "books_api",
                    type = DependencyType.USES,
                    description = "Books API service"
                )
            ),
            attributes = listOf("api: BooksApiService", "lastFetched: Long"),
            methods = listOf("getBooks(): List<Book>", "fetchBookById(id: String): Book?")
        )

        // Framework Layer
        val roomDatabase = ArchitectureNode(
            id = "room_database",
            name = "BooksDatabase",
            layer = ArchitectureLayer.FRAMEWORK,
            description = "Room database for local storage",
            position = Offset(200f, 500f),
            dependencies = emptyList(),
            attributes = listOf("tables: List<Table>", "version: Int"),
            methods = listOf("query(sql: String): Cursor", "upgradeSchema(newVersion: Int)")
        )

        val booksApi = ArchitectureNode(
            id = "books_api",
            name = "BooksApiService",
            layer = ArchitectureLayer.FRAMEWORK,
            description = "Retrofit API service for books",
            position = Offset(600f, 500f),
            dependencies = emptyList(),
            attributes = listOf("baseUrl: String", "timeout: Int"),
            methods = listOf("getBooks(): List<Book>", "getBookById(id: String): Book?")
        )

        val analyticsService = ArchitectureNode(
            id = "analytics_service",
            name = "AnalyticsService",
            layer = ArchitectureLayer.FRAMEWORK,
            description = "Service for tracking analytics events",
            position = Offset(400f, 500f),
            dependencies = emptyList(),
            attributes = listOf("events: List<Event>", "enabled: Boolean"),
            methods = listOf("trackEvent(event: Event)", "enableTracking()", "disableTracking()")
        )

        // Add all nodes to the architecture
        listOf(
            booksLibraryViewModel,
            getAllBooksUseCase,
            getRentedBooksUseCase,
            rentBookUseCase,
            booksRepository,
            booksLocalDataSource,
            booksRemoteDataSource,
            roomDatabase,
            booksApi,
            analyticsService
        ).forEach { architecture.addNode(it) }

        return architecture
    }
} 