package io.artmaker.actions

import io.artmaker.models.PointsData

/**
 * Represents actions that can be undone or redone in a drawing application.
 */
internal sealed class UndoRedoAction {
    data class Draw(val pathData: PointsData) : UndoRedoAction()
    data class Erase(val oldState: List<PointsData>, val newState: List<PointsData>) : UndoRedoAction()
}