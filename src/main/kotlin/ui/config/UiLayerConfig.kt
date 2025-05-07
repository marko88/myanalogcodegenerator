package ui.config

import androidx.compose.ui.graphics.Color

// Data class for a component within a layer
data class NodeViewConfig(
    val name: String
)

// Data class for a layer
data class UiLayerConfig(
    val name: String,
    val color: Color,
    val components: List<NodeViewConfig>
)

object UiArchitectureConfig {
    val layers = listOf(
        UiLayerConfig(
            name = "Presentation",
            color = Color(0xFFB983FF),
            components = listOf(
                NodeViewConfig("View"),
                NodeViewConfig("ViewModel"),
                NodeViewConfig("Presenter")
            )
        ),
        UiLayerConfig(
            name = "Domain",
            color = Color(0xFF5DE2E6),
            components = listOf(
                NodeViewConfig("UseCase"),
                NodeViewConfig("Interactor")
            )
        ),
        UiLayerConfig(
            name = "Data",
            color = Color(0xFFFAED7D),
            components = listOf(
                NodeViewConfig("Repository"),
                NodeViewConfig("DataSource")
            )
        )
    )
} 