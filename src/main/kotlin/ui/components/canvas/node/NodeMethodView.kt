package myanalogcodegenerator.ui.components.canvas.node

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.model.NodeMethod
import myanalogcodegenerator.ui.components.canvas.node.style.NodeItemStyles
import ui.components.canvas.NodeSelectionState

@Preview
@Composable
fun NodeMethodView(
    method: NodeMethod,
    selectionState: NodeSelectionState = NodeSelectionState.DEFAULT,
    onPinPositioned: ((String, Offset) -> Unit)? = null
) {
    val style = NodeItemStyles.fromSelection(selectionState)

    Row {
        NodePinView(
            color = style.textColor,
            modifier = Modifier.align(Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = method.name,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Medium,
            color = style.textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.height(30.dp).align(Alignment.CenterVertically)
        )
    }
}