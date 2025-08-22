package myanalogcodegenerator.ui.components.canvas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import myanalogcodegenerator.domain.repository.ArchitectureRepository
import myanalogcodegenerator.ui.components.canvas.graph.CanvasView
import myanalogcodegenerator.ui.components.canvas.tool.TopBar

@Composable
fun ArchDesignerApp(architectureRepository: ArchitectureRepository) {
    var state by remember { mutableStateOf(UiState()) }

    val actions = remember {
        UiActions(
            setViewMode = { state = state.copy(viewMode = it) },
            setSearch = { state = state.copy(search = it) },
            toggleDi = { state = state.copy(showDi = !state.showDi) },
            toggleRef = { state = state.copy(showRef = !state.showRef) },
            toggleExternal = { state = state.copy(showExternal = !state.showExternal) },
            toggleOrphans = { state = state.copy(showOrphans = !state.showOrphans) },
            toggleHighlight = { state = state.copy(highlightNeighbors = !state.highlightNeighbors) },

            // zoom/pan controls
            setScale = { s -> state = state.copy(scale = s.coerceIn(0.2f, 4f)) },
            setPan   = { p -> state = state.copy(pan = p) },
            zoomIn   = { state = state.copy(scale = (state.scale * 1.1f).coerceIn(0.2f, 4f)) },
            zoomOut  = { state = state.copy(scale = (state.scale / 1.1f).coerceIn(0.2f, 4f)) },
            fitToScreen = {
                // TODO: compute from repo graph bounds; for now reset to defaults
                state = state.copy(scale = 1f, pan = Offset.Zero)
            }
        )
    }

    MaterialTheme {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color(0xFF1A1B26))
        ) {
            TopBar(state, actions)

            // Center area (canvas) fills the remaining height
            CanvasView(
                architectureRepository = architectureRepository,
                scale = state.scale,
                pan = state.pan,
                onTransform = { newScale, newPan ->
                    actions.setScale(newScale)
                    actions.setPan(newPan)
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
        }
    }
}

/* ---------- UI state & actions ---------- */

enum class ViewMode { FULL, DI_ONLY, REF_ONLY, FEATURE }

data class UiState(
    val viewMode: ViewMode = ViewMode.FULL,
    val search: String = "",
    val showDi: Boolean = true,
    val showRef: Boolean = true,
    val showExternal: Boolean = false,
    val showOrphans: Boolean = false,
    val highlightNeighbors: Boolean = false,

    // pan & zoom
    val scale: Float = 1f,
    val pan: Offset = Offset.Zero
)

class UiActions(
    val setViewMode: (ViewMode) -> Unit,
    val setSearch: (String) -> Unit,
    val toggleDi: () -> Unit,
    val toggleRef: () -> Unit,
    val toggleExternal: () -> Unit,
    val toggleOrphans: () -> Unit,
    val toggleHighlight: () -> Unit,

    // pan & zoom controls
    val setScale: (Float) -> Unit,
    val setPan: (Offset) -> Unit,
    val zoomIn: () -> Unit,
    val zoomOut: () -> Unit,
    val fitToScreen: () -> Unit
)