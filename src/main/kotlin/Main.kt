import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import domain.blueprint.CleanArchitectureBlueprint
import domain.model.ArchitectureLayer
import ui.components.canvas.NodeLayout
import ui.components.canvas.Canvas as InteractiveCanvas

@Composable
fun App() {
    val architecture = CleanArchitectureBlueprint.create()
    val nodesByLayer = ArchitectureLayer.values().map { layer ->
        layer to architecture.getNodesByType(layer).toList()
    }

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1A1B26))
        ) {
            InteractiveCanvas {
                Box(modifier = Modifier.fillMaxSize()) {
                    NodeLayout(
                        nodesByLayer = nodesByLayer,
                        architecture = architecture,
                        onNodeSelected = { /* Handle node selection if needed */ }
                    )
                }
            }
        }
    }
}

fun main() = androidx.compose.ui.window.application {
    androidx.compose.ui.window.Window(
        onCloseRequest = ::exitApplication,
        title = "Architecture Visualization"
    ) {
        App()
    }
}
