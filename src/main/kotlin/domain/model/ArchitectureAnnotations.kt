package domain.model

/**
 * Marks a class as a component that should be visualized in the architecture diagram.
 * @property layer The architectural layer this component belongs to
 * @property description Optional description of the component's purpose
 * @property color Optional custom color for visualization (hex format)
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class ArchitectureComponent(
    val layer: ArchitectureLayer,
    val description: String = "",
    val color: String = ""
)

/**
 * Represents the architectural layers in Clean Architecture
 */
enum class ArchitectureLayer(
    val packageName: String,
    val color: Long
) {
    PRESENTATION(
        packageName = "presentation",
        color = 0xFFB983FF
    ),  // UI, ViewModels, Presenters
    DOMAIN(
        packageName = "domain",
        color = 0xFF5DE2E6
    ),        // Use Cases, Domain Models
    DATA(
        packageName = "data",
        color = 0xFFFAED7D
    ),          // Repositories, Data Sources
    FRAMEWORK(
        packageName = "framework",
        color = 0xFFFFB74D
    )      // External frameworks, libraries
}

/**
 * Types of dependencies between components
 */
enum class DependencyType {
    INJECTS,       // Component is injected into another
    IMPLEMENTS,    // Component implements an interface
    EXTENDS,       // Component extends another class
    USES,          // Component uses another component
    OBSERVES,       // Component observes another component (e.g., LiveData, Flow)
    // More specific for generation/visual clarity
    CONSTRUCTOR_INJECTION,   // Injected via constructor (e.g., Dagger/Hilt/Koin)
    FIELD_INJECTION,         // Injected via property/field
    METHOD_INJECTION         // Injected via method or setter
} 