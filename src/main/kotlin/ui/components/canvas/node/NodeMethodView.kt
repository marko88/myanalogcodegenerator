package myanalogcodegenerator.ui.components.canvas.node

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.model.ArchitectureNode
import domain.model.NodeMethod
import myanalogcodegenerator.ui.components.canvas.node.style.NodeItemStyles
import ui.components.canvas.NodeSelectionState

@Preview
@Composable
fun NodeMethodView(
    parent: ArchitectureNode,
    method: NodeMethod,
    selectionState: NodeSelectionState = NodeSelectionState.DEFAULT,
    onClick: () -> Unit
) {
    val style = NodeItemStyles.fromSelection(selectionState)

    Row(modifier = Modifier.clickable {
        onClick()
    }) {
        NodePinView(
        pinId = "${parent.id}#${method.name}",
        color = style.textColor,
            modifier = Modifier.align(Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.width(6.dp))

        val signature = if (method.parameters.isNotEmpty()) {
            val params = method.parameters.joinToString(", ") { (name, type) -> "$name: $type" }
            "${method.name}($params): ${method.returnType}"
        } else {
            "${method.name}(): ${method.returnType}"
        }

        Text(
            text = signature,
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