package myanalogcodegenerator.ui.components.canvas.node.style

import androidx.compose.ui.graphics.Color
import domain.model.DependencyType

object DependencyTypeStyles {
    fun backgroundFor(type: DependencyType): Color = when (type) {
        DependencyType.CONSTRUCTOR_INJECTION -> Color(0xFF4CAF50)
        DependencyType.FIELD_INJECTION       -> Color(0xFF8BC34A)
        DependencyType.METHOD_INJECTION      -> Color(0xFFCDDC39)
        DependencyType.NETWORK_CALL          -> Color(0xFFFF9800)
        DependencyType.IMPLEMENTS            -> Color(0xFF3F51B5)
        DependencyType.EXTENDS               -> Color(0xFF9C27B0)
        DependencyType.INJECTS, DependencyType.USES -> Color(0xFF607D8B)
    }
}