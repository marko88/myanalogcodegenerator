package myanalogcodegenerator.domain.command.use_cases

import myanalogcodegenerator.domain.command.Command
import myanalogcodegenerator.domain.command.CommandContext

abstract class BaseCommand: Command {
    override suspend fun before(context: CommandContext) : Boolean = true
    override suspend fun after(context: CommandContext) = Unit
}