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
package io.fbada006.artmaker.actions

import io.fbada006.artmaker.models.PointsData

/**
 * Represents drawing type during the undo, redo phase before or after pressing the eraser button
 * @property BeforeErase The User is performing an UndoRedoAction and has not pressed the erase button during this session
 * @property AfterErase The User is performing an UndoRedoAction and has pressed the erase button during this session
 */
internal sealed class UndoRedoEventType {
    data class BeforeErase(val pathData: PointsData) : UndoRedoEventType()
    data class AfterErase(val oldState: List<PointsData>, val newState: List<PointsData>) : UndoRedoEventType()
}
