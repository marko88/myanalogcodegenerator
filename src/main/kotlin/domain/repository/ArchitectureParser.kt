package domain.repository

import domain.model.ArchitectureDefinitionModel
import domain.model.DependencyType

/**
 * Interface for parsing architecture components and their dependencies from source code
 */
interface ArchitectureParser {
    /**
     * Parses a source file or directory to extract architecture information
     * @param sourcePath Path to the source file or directory
     * @return ArchitectureDefinitionModel containing all components and their relationships
     */
    fun parse(sourcePath: String): ArchitectureDefinitionModel

    /**
     * Analyzes a single class to extract its dependencies from constructor parameters
     * @param className Fully qualified name of the class
     * @return List of dependency information (target class and relationship type)
     */
    fun analyzeDependencies(className: String): List<DependencyInfo>
}

/**
 * Information about a dependency relationship
 */
data class DependencyInfo(
    val targetClassName: String,
    val type: DependencyType,
    val isExternal: Boolean = false  // Whether the dependency is outside our architecture
) 