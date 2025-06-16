package myanalogcodegenerator.domain.command

import myanalogcodegenerator.domain.repository.ArchitectureRepository

object CommandManager {

    private val commandStack = mutableListOf<Command>()
    private val undoStack = mutableListOf<Command>()

    lateinit var context: CommandContext

    fun initialize(repository: ArchitectureRepository) {
        context = CommandContext(repository = repository)
    }

    suspend fun execute(command: Command) {
        command.before(context)
        command.execute(context)
        command.after(context)
        commandStack.add(command)
        undoStack.clear()
    }

    suspend fun undo() {
        if (commandStack.isNotEmpty()) {
            val command = commandStack.removeLast()
            command.undo(context)
            undoStack.add(command)
        }
    }

    suspend fun redo() {
        if (undoStack.isNotEmpty()) {
            val command = undoStack.removeLast()
            execute(command)
        }
    }
}