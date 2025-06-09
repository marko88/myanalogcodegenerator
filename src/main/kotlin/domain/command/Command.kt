package myanalogcodegenerator.domain.command

interface Command {
    fun execute()
    fun undo()
}