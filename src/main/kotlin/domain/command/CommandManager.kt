package myanalogcodegenerator.domain.command

object CommandManager {
    private val undoStack = mutableListOf<Command>()
    private val redoStack = mutableListOf<Command>()

    fun execute(command: Command) {
        command.execute()
        undoStack.add(command)
        redoStack.clear()
    }

    fun undo() {
        val command = undoStack.removeLastOrNull() ?: return
        command.undo()
        redoStack.add(command)
    }

    fun redo() {
        val command = redoStack.removeLastOrNull() ?: return
        command.execute()
        undoStack.add(command)
    }
}