package com.fbada006.shared.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("RedundantVisibilityModifier")
public val Redo: ImageVector
	get() {
		if (redo != null) {
			return redo!!
		}
		redo = ImageVector.Builder(
            name = "Redo",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
			path(
    			fill = null,
    			fillAlpha = 1.0f,
    			stroke = SolidColor(Color(0xFF000000)),
    			strokeAlpha = 1.0f,
    			strokeLineWidth = 2f,
    			strokeLineCap = StrokeCap.Round,
    			strokeLineJoin = StrokeJoin.Round,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(15f, 14f)
				lineToRelative(5f, -5f)
				lineToRelative(-5f, -5f)
			}
			path(
    			fill = null,
    			fillAlpha = 1.0f,
    			stroke = SolidColor(Color(0xFF000000)),
    			strokeAlpha = 1.0f,
    			strokeLineWidth = 2f,
    			strokeLineCap = StrokeCap.Round,
    			strokeLineJoin = StrokeJoin.Round,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(20f, 9f)
				horizontalLineTo(9.5f)
				arcTo(5.5f, 5.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 4f, 14.5f)
				arcTo(5.5f, 5.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 9.5f, 20f)
				horizontalLineTo(13f)
			}
		}.build()
		return redo!!
	}

private var redo: ImageVector? = null
