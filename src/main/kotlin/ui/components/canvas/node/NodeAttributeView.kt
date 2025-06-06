package myanalogcodegenerator.ui.components.canvas.node


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.model.NodeAttribute
import myanalogcodegenerator.ui.components.canvas.node.style.NodeItemStyles
import ui.components.canvas.NodeSelectionState

@Composable
fun NodeAttributeView(
    attribute: NodeAttribute,
    selectionState: NodeSelectionState = NodeSelectionState.DEFAULT,
    onPinPositioned: ((String, Offset) -> Unit)? = null
) {
    val style = NodeItemStyles.fromSelection(selectionState)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        // Left pin (for connecting lines)
        NodePinView(color = style.textColor)

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = "${attribute.name}: ${attribute.type}",
            fontSize = 10.sp,
            color = style.textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}