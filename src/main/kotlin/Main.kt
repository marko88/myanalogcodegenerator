package myanalogcodegenerator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import domain.model.ArchitectureLayer
import domain.blueprint.ArchitectureUseCase
import domain.repository.ArchitectureDatabase
import kotlinx.coroutines.delay
import myanalogcodegenerator.domain.repository.ArchitectureRepository
import ui.components.canvas.NodeLayout
import ui.components.canvas.Canvas as InteractiveCanvas

@Composable
fun App(architectureRepository: ArchitectureRepository) {
    val architecture by architectureRepository.model.collectAsState()
    val nodesByLayer = ArchitectureLayer.values().map { layer ->
        layer to architecture.getAllNodes().filter { it.layer == layer }
    }

    val architectureUseCase = ArchitectureUseCase(architectureRepository)

    // Add delay and create Library feature
    var number = 1
    LaunchedEffect(Unit) {
        delay(1000)
        println("Creating Library feature...")
        architectureUseCase.createFeature("Library)"+number++, includeViewModel = true)
        println("Library feature created. Total nodes: ${architecture.getAllNodes().size}")
        println("Nodes by layer:")
        ArchitectureLayer.values().forEach { layer ->
            println("${layer.name}: ${architecture.getNodesByType(layer).size} nodes")
        }
    }

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1A1B26))
        ) {
            InteractiveCanvas(architectureRepository) {
                Box(modifier = Modifier.fillMaxSize()) {
                    NodeLayout(
                        nodesByLayer = nodesByLayer,
                        architecture = architecture,
                        architectureRepository = architectureRepository,
                        onNodeSelected = { /* Handle node selection if needed */ }
                    )
                }
            }
        }
    }
}

fun main() = androidx.compose.ui.window.application {
    val architectureRepository = ArchitectureRepository()

    androidx.compose.ui.window.Window(
        onCloseRequest = ::exitApplication,
        title = "Architecture Visualization"
    ) {
        App(architectureRepository)
    }
}
