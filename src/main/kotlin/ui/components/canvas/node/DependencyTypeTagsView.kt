package myanalogcodegenerator.ui.components.canvas.node

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.model.DependencyType
import myanalogcodegenerator.ui.components.canvas.node.style.DependencyTypeStyles

@Composable
fun DependencyTypeTagsView(types: Set<DependencyType>) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        types.forEach { type ->
            Text(
                text = type.value,
                fontSize = 10.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .height(30.dp)
                    .background(
                        color = DependencyTypeStyles.backgroundFor(type),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 6.dp)
            )
        }
    }
}