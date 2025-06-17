package myanalogcodegenerator.domain.command.use_cases

import myanalogcodegenerator.domain.command.CommandContext
import myanalogcodegenerator.domain.repository.ArchitectureRepository

class RenameNodeCommand(
    private val repository: ArchitectureRepository,
    private val nodeId: String,
    private val newName: String
) : BaseCommand() {

    private lateinit var oldName: String

    override suspend fun before(context: CommandContext): Boolean {
        val node = repository.getNodeById(nodeId)
        oldName = node?.name ?: ""
        return true
    }

    override suspend fun execute(context: CommandContext) {
        repository.renameNode(nodeId, newName)
    }

    override suspend fun undo(context: CommandContext) {
        repository.renameNode(nodeId, oldName)
    }
}
