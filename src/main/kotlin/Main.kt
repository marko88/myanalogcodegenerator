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
import myanalogcodegenerator.parser.TreeSitterParser
import ui.components.canvas.CanvasView
import java.nio.file.Paths


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
    val architectureRepository = ArchitectureRepository().apply {
        updateModel(VinylArchitecture)
        val out = Paths.get("src/generated/kotlin")
        println("Generating shells into $out")
        ShellGenerator.generate(VinylArchitecture, out)
        TreeSitterParser().parse()

    }

    CommandManager.initialize(architectureRepository)

    Window(
        onCloseRequest = ::exitApplication,
        title = "Architecture Visualization"
    ) {
        App(architectureRepository)
    }
}
