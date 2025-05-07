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
enum class ArchitectureLayer {
    PRESENTATION,  // UI, ViewModels, Presenters
    DOMAIN,        // Use Cases, Domain Models
    DATA,          // Repositories, Data Sources
    FRAMEWORK      // External frameworks, libraries
}

/**
 * Types of dependencies between components
 */
enum class DependencyType {
    INJECTS,       // Component is injected into another
    IMPLEMENTS,    // Component implements an interface
    EXTENDS,       // Component extends another class
    USES,          // Component uses another component
    OBSERVES       // Component observes another component (e.g., LiveData, Flow)
} 