package myanalogcodegenerator.domain.command

import myanalogcodegenerator.domain.repository.ArchitectureRepository

interface Command {
    suspend fun before(context: CommandContext) : Boolean = true
    suspend fun execute(context: CommandContext)
    suspend fun after(context: CommandContext) = Unit
    suspend fun undo(context: CommandContext)
}

data class CommandContext(
    val repository: ArchitectureRepository
)