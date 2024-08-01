package com.artmaker.actions

import androidx.compose.ui.geometry.Offset

/**
 * Events that happen during drawing
 */
sealed interface DrawEvent {
    data class AddNewShape(val offset: Offset) : DrawEvent
    data class UpdateCurrentShape(val offset: Offset) : DrawEvent
    data object UndoLastShapePoint : DrawEvent
}