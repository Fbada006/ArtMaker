/*
 * Copyright 2024 ArtMaker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.artmaker.actions

import io.artmaker.models.PointsData

/**
 * Represents actions that can be undone or redone in a drawing application.
 */
internal sealed class UndoRedoAction {
    data class Draw(val pathData: PointsData) : UndoRedoAction()
    data class Erase(val oldState: List<PointsData>, val newState: List<PointsData>) : UndoRedoAction()
}
