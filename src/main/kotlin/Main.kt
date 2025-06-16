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
import myanalogcodegenerator.domain.model.DemoData
import myanalogcodegenerator.domain.repository.ArchitectureRepository
import ui.components.canvas.CanvasView

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
        updateModel(DemoData.createDemoArchitecture())
    }

    CommandManager.initialize(architectureRepository)

    Window(
        onCloseRequest = ::exitApplication,
        title = "Architecture Visualization"
    ) {
        App(architectureRepository)
    }
}
