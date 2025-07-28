package myanalogcodegenerator.domain.dsl

import domain.repository.ArchitectureDatabase

/** Top-level builder. Call it from a `.kt` or `.kts` script. */
fun architecture(
    name: String = "Unnamed",
    build: ArchitectureScope.() -> Unit
): ArchitectureDatabase =
    ArchitectureScope(name).apply(build).build()