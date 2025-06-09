package myanalogcodegenerator.domain.repository

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.layout.LayoutCoordinates

object PinPositionRegistry {
    private val _positions = mutableStateMapOf<String, LayoutCoordinates>()
    val positions: Map<String, LayoutCoordinates> get() = _positions

    fun updatePosition(pinId: String, offset: LayoutCoordinates) {
        _positions[pinId] = offset
    }

    fun getPosition(pinId: String): LayoutCoordinates? = _positions[pinId]

    fun clear() {
        _positions.clear()
    }
}
