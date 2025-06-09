package myanalogcodegenerator.domain.command.use_cases

import myanalogcodegenerator.domain.command.Command
import myanalogcodegenerator.domain.repository.ArchitectureRepository
import myanalogcodegenerator.ui.components.canvas.SelectableEntity

class SelectEntityCommand(
    private val repository: ArchitectureRepository,
    private val newSelection: Set<SelectableEntity>,
) : Command {

    private var previousSelection: Set<SelectableEntity> = emptySet()

    override fun execute() {
        previousSelection = repository.selection.value
        repository.setSelection(newSelection)
    }

    override fun undo() {
        repository.setSelection(previousSelection)
    }
}