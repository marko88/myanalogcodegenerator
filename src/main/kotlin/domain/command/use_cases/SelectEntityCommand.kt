package myanalogcodegenerator.domain.command.use_cases

import myanalogcodegenerator.domain.command.CommandContext
import myanalogcodegenerator.domain.repository.ArchitectureRepository
import myanalogcodegenerator.ui.components.canvas.SelectableEntity

class SelectEntityCommand(
    private val repository: ArchitectureRepository,
    private val newSelection: Set<SelectableEntity>,
) : BaseCommand() {

    private var previousSelection: Set<SelectableEntity> = emptySet()

    override suspend fun execute(context: CommandContext) {
        previousSelection = repository.selection.value
        repository.setSelection(newSelection)
    }

    override suspend fun undo(context: CommandContext) {
        repository.setSelection(previousSelection)
    }
}
