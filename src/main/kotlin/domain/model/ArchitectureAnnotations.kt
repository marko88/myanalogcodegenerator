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
    OTHER(
        packageName = "other",
        color = 0xFFFFB74D
    )      // External frameworks, libraries
}

/**
 * Types of dependencies between components
 */
enum class DependencyType(val value: String) {
    INJECTS("INJ"),       // Component is injected into another
    IMPLEMENTS("IMPL"),    // Component implements an interface
    EXTENDS("EXT"),       // Component extends another class
    USES("USES"),          // Component uses another component
    // More specific for generation/visual clarity
    CONSTRUCTOR_INJECTION("C_INJ"),   // Injected via constructor (e.g., Dagger/Hilt/Koin)
    FIELD_INJECTION("F_INJ"),         // Injected via property/field
    METHOD_INJECTION("M_INJ"),         // Injected via method or setter
    NETWORK_CALL("N_CALL")
}