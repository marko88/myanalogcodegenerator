package myanalogcodegenerator.ui.components.canvas.node

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.model.NodeMethod
import ui.components.canvas.NodeSelectionState

@Composable
fun NodeMethodView(
    method: NodeMethod,
    selectionState: NodeSelectionState = NodeSelectionState.DEFAULT
) {
    val style = NodeItemStyles.methodStyle(selectionState == NodeSelectionState.SELECTED)

    Text(
        text = method.name,
        color = style.textColor,
        fontSize = 9.sp,
        fontWeight = FontWeight.Medium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .padding(bottom = 1.dp)
    )
}