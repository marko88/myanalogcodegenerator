package myanalogcodegenerator.domain.command.use_cases

import domain.model.ArchitectureNode
import myanalogcodegenerator.domain.command.CommandContext
import myanalogcodegenerator.domain.repository.ArchitectureRepository

class CreateNodeCommand(
    private val repository: ArchitectureRepository,
    private val node: ArchitectureNode
) : BaseCommand() {

    override suspend fun before(context: CommandContext): Boolean {
        // Optional validation (e.g., check for ID uniqueness)
        return repository.model.value.getNodeById(node.id) == null
    }

    override suspend fun execute(context: CommandContext) {
        repository.addNode(node)
    }

    override suspend fun undo(context: CommandContext) {
        repository.removeNode(node.id)
    }
}
