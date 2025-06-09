package myanalogcodegenerator.domain.command

interface ArchitectureCommand {
    fun execute()
    fun undo()
}