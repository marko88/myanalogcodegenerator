package myanalogcodegenerator.ui.components.canvas.tool

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import myanalogcodegenerator.ui.components.canvas.UiActions
import myanalogcodegenerator.ui.components.canvas.UiState
import myanalogcodegenerator.ui.components.canvas.ViewMode

@Composable
fun TopBar(state: UiState, actions: UiActions) {
    Row(
        Modifier.fillMaxWidth().height(56.dp).background(Color(0xFF14151E)).padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = state.search,
            onValueChange = actions.setSearch,
            modifier = Modifier.width(280.dp).padding(end = 12.dp),
            label = { Text("Search") },
            singleLine = true
        )

        Button(onClick = actions.zoomIn, modifier = Modifier.padding(end = 8.dp)) { Text("+ Zoom") }
        Button(onClick = actions.zoomOut, modifier = Modifier.padding(end = 8.dp)) { Text("- Zoom") }
        Button(onClick = actions.fitToScreen, modifier = Modifier.padding(end = 16.dp)) { Text("Fit") }

        Text("View:", color = Color.White, modifier = Modifier.padding(end = 8.dp))
        ViewModeDropdown(state.viewMode, actions.setViewMode)

        Spacer(Modifier.weight(1f))

        Button(onClick = actions.toggleHighlight) {
            Text(if (state.highlightNeighbors) "Highlight: On" else "Highlight: Off")
        }
    }
}

@Composable
private fun ViewModeDropdown(current: ViewMode, onChange: (ViewMode) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        // ultra-simple selector; replace with proper dropdown later
        listOf(ViewMode.FULL, ViewMode.DI_ONLY, ViewMode.REF_ONLY, ViewMode.FEATURE).forEach { mode ->
            Row(
                Modifier.padding(horizontal = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(selected = current == mode, onClick = { onChange(mode) })
                Text(mode.name, color = Color.White)
            }
        }
    }
}