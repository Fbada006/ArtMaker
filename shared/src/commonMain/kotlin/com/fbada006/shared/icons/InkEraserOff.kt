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
package com.fbada006.shared.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val InkEraserOff: ImageVector
    get() {
        if (inkEraserOff != null) {
            return inkEraserOff!!
        }
        inkEraserOff = ImageVector.Builder(
            name = "Ink_eraser_off",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero,
            ) {
                moveTo(791f, 905f)
                lineTo(602f, 716f)
                lineToRelative(-82f, 84f)
                horizontalLineTo(190f)
                lineToRelative(-85f, -85f)
                quadToRelative(-23f, -23f, -23.5f, -57f)
                reflectiveQuadToRelative(22.5f, -58f)
                lineToRelative(188f, -194f)
                lineTo(55f, 169f)
                lineToRelative(57f, -57f)
                lineToRelative(736f, 736f)
                close()
                moveTo(224f, 720f)
                horizontalLineToRelative(262f)
                lineToRelative(59f, -61f)
                lineToRelative(-197f, -197f)
                lineToRelative(-188f, 194f)
                close()
                moveToRelative(491f, -119f)
                lineToRelative(-57f, -57f)
                lineToRelative(142f, -146f)
                lineToRelative(-198f, -198f)
                lineToRelative(-142f, 146f)
                lineToRelative(-56f, -56f)
                lineToRelative(140f, -146f)
                quadToRelative(23f, -24f, 56.5f, -24f)
                reflectiveQuadToRelative(56.5f, 23f)
                lineToRelative(199f, 199f)
                quadToRelative(23f, 23f, 23f, 57f)
                reflectiveQuadToRelative(-23f, 57f)
                close()
                moveToRelative(-268f, -41f)
            }
        }.build()
        return inkEraserOff!!
    }

private var inkEraserOff: ImageVector? = null
