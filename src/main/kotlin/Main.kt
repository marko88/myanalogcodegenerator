package myanalogcodegenerator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import myanalogcodegenerator.domain.command.CommandManager
import myanalogcodegenerator.domain.model.VinylArchitecture
import myanalogcodegenerator.domain.repository.ArchitectureRepository
import myanalogcodegenerator.generator.ShellGenerator
import myanalogcodegenerator.parser.KotlinTreeSitterRepository
import myanalogcodegenerator.parser.SourceWatcher
import ui.components.canvas.CanvasView
import java.io.File
import java.nio.file.Paths
import java.nio.file.StandardWatchEventKinds.*


@Composable
fun App(architectureRepository: ArchitectureRepository) {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1A1B26))
        ) {
            CanvasView(architectureRepository)
        }
    }
}

fun main() = application {
    val out = Paths.get("src/generated/kotlin")

    // Init Parser
    val treeSitterParser = KotlinTreeSitterRepository(File(out.toUri()))
    val originalDatabase = treeSitterParser.parseFolderToArchitectureDatabase()

    val architectureRepository = ArchitectureRepository().apply {
        updateModel(originalDatabase)
    }

            println("Generating shells into $out")
            ShellGenerator.generate(VinylArchitecture, out)

    SourceWatcher(out) { file, event ->
        when (event) {
            ENTRY_MODIFY -> {
                val node = treeSitterParser.parseFile(file)

                architectureRepository.updateModel(
                    architectureRepository.model.value.updateNode(node.id) {
                        node
                    }
                )
            }

            ENTRY_CREATE -> {
                val node = treeSitterParser.parseFile(file)
                architectureRepository.updateModel(
                    architectureRepository.model.value.addNode(node)
                )
            }

            ENTRY_DELETE -> {
                architectureRepository.updateModel(
                    architectureRepository.model.value.removeNode(file.name.removeSuffix(".kt"))
                )
            }
        }
        println("$file changed: $event")
    }
    CommandManager.initialize(architectureRepository)

    Window(
        onCloseRequest = ::exitApplication,
        title = "Architecture Visualization"
    ) {
        App(architectureRepository)
    }
}
