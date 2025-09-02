package com.kaii.photos.helpers.editing

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.unit.dp
import com.kaii.photos.R
import kotlin.math.ceil
import kotlin.math.floor


object ExtendedMaterialTheme {
    val colorScheme: ExtendedColorScheme
        @Composable
        get() = ExtendedColorScheme(
            dialogSurface = brightenColor(MaterialTheme.colorScheme.surface, 0.05f),
            expandableDialogBackground = darkenColor(MaterialTheme.colorScheme.surfaceVariant, 0.2f)
        )
}

data class ExtendedColorScheme(
    val dialogSurface: Color,
    val expandableDialogBackground: Color
)

fun brightenColor(color: Color, percentage: Float): Color {
    val r = (color.red * 255).toInt()
    val g = (color.green * 255).toInt()
    val b = (color.blue * 255).toInt()

    val newR = (r + (255 - r) * percentage).toInt().coerceIn(0, 255)
    val newG = (g + (255 - g) * percentage).toInt().coerceIn(0, 255)
    val newB = (b + (255 - b) * percentage).toInt().coerceIn(0, 255)

    return Color(newR, newG, newB)
}

fun darkenColor(color: Color, percentage: Float): Color {
    val r = (color.red * 255).toInt()
    val g = (color.green * 255).toInt()
    val b = (color.blue * 255).toInt()

    val newR = (r * (1 - percentage)).toInt().coerceIn(0, 255)
    val newG = (g * (1 - percentage)).toInt().coerceIn(0, 255)
    val newB = (b * (1 - percentage)).toInt().coerceIn(0, 255)

    return Color(newR, newG, newB)
}

object DrawingColors {
    /** white white */
    val White = Color.White

    /** black black */
    val Black = Color.Black

    /** poppy red */
    val Red = Color(red = 227, green = 83, blue = 53)

    /** lemon yellow */
    val Yellow = Color(red = 250, green = 250, blue = 51)

    /** emerald green */
    val Green = Color(red = 80, green = 200, blue = 120)

    /** bright blue */
    val Blue = Color(red = 0, green = 150, blue = 255)

    /** lavender purple */
    val Purple = Color(red = 204, green = 204, blue = 255)
}

@Composable
fun ColorIndicator(
    color: Color,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(
                if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
            )
            .clickable {
                onClick()
            }
            .then(modifier)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(color)
                .align(Alignment.Center)
        )
    }
}

val gradientColorList = listOf(
    Color.Blue,
    Color.Red,
    Color.Green,
    Color.Yellow
)

fun getColorFromLinearGradientList(
    value: Float,
    colorList: List<Color>
): Color {
    val positionInList = (colorList.size - 1f) * value
    val lowerColor = colorList[floor(positionInList).toInt()]
    val upperColor = colorList[ceil(positionInList).toInt()]
    val mixRatio = positionInList - colorList.indexOf(lowerColor)

    val resolvedColor = Color(
        red = lowerColor.red * (1 - mixRatio) + upperColor.red * mixRatio,
        green = lowerColor.green * (1 - mixRatio) + upperColor.green * mixRatio,
        blue = lowerColor.blue * (1 - mixRatio) + upperColor.blue * mixRatio
    )

    return resolvedColor
}

enum class MediaColorFilters(
    val matrix: ColorMatrix,
    @param:StringRes val title: Int
) {
    None(
        title = R.string.filter_none,
        matrix = ColorMatrix()
    ),

    Inverted(
        title = R.string.filter_inverted,
        matrix = ColorMatrix(
            floatArrayOf(
                -1f, 0f, 0f, 0f, 255f,
                0f, -1f, 0f, 0f, 255f,
                0f, 0f, -1f, 0f, 255f,
                0f, 0f, 0f, 1f, 0f
            )
        )
    ),

    Vibrant(
        title = R.string.filter_vibrant,
        matrix = ColorMatrix().apply {
            setToSaturation(1.4f)
            set(0, 4, 1f)
            set(1, 4, 1f)
            set(2, 4, 1f)
        }
    ),

    BlackAndWhite(
        title = R.string.filter_bw,
        matrix = ColorMatrix(
            floatArrayOf(
                1.25f, 1.25f, 1.25f, 0f, -160f,
                1.25f, 1.25f, 1.25f, 0f, -160f,
                1.25f, 1.25f, 1.25f, 0f, -160f,
                0f, 0f, 0f, 1f, 0f
            )
        )
    ),

    Film(
        title = R.string.filter_film,
        matrix = ColorMatrix(
            floatArrayOf(
                1.438f, -0.062f, -0.062f, 0f, -0.03f * 255f,
                -0.122f, 1.378f, -0.122f, 0f, 0.05f * 255f,
                -0.016f, -0.016f, 1.483f, 0f, -0.02f * 255f,
                0f, 0f, 0f, 1f, 0f
            )
        )
    ),

    Grayscale(
        title = R.string.filter_grayscale,
        matrix = ColorMatrix().apply {
            setToSaturation(0f)
            set(0, 4, 1f)
            set(1, 4, 1f)
            set(2, 4, 1f)
        }
    ),

    Sepia(
        title = R.string.filter_sepia,
        matrix = ColorMatrix(
            floatArrayOf(
                0.393f, 0.769f, 0.189f, 0f, 0f,
                0.349f, 0.686f, 0.168f, 0f, 0f,
                0.272f, 0.534f, 0.131f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )
    ),

    Warm(
        title = R.string.filter_warm,
        matrix = ColorMatrix(
            floatArrayOf(
                1.3f, 0f, 0f, 0f, 0f,
                0.2f, 0.9f, 0f, 0f, 0f,
                0.2f, 0f, 0.9f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )
    ),

    Cool(
        title = R.string.filter_cool,
        matrix = ColorMatrix(
            floatArrayOf(
                0.9f, 0f, 0.2f, 0f, 0f,
                0f, 0.9f, 0.2f, 0f, 0f,
                0f, 0f, 1.3f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )
    ),

    Vintage(
        title = R.string.filter_vintage,
        matrix = ColorMatrix(
            floatArrayOf(
                0.9f, 0.5f, 0.2f, 0f, 0f,
                0.4f, 0.8f, 0.2f, 0f, 0f,
                0.2f, 0.3f, 0.7f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )
    ),

    Posterize(
        title = R.string.filter_posterize,
        matrix = ColorMatrix(
            floatArrayOf(
                0.5f, 0f, 0f, 0f, 56f,
                0f, 0.5f, 0f, 0f, 56f,
                0f, 0f, 0.5f, 0f, 56f,
                0f, 0f, 0f, 1f, 0f
            )
        ).apply {
            setToSaturation(0.8f)
        }
    ),

    Glow(
        title = R.string.filter_glow,
        matrix = ColorMatrix(
            floatArrayOf(
                1.6f, 0f, 0f, 0f, 0f,
                0f, 1.6f, 0f, 0f, 0f,
                0f, 0f, 1.6f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )
    ),

    Peachy(
        title = R.string.filter_peachy,
        matrix = ColorMatrix(
            floatArrayOf(
                1.1f, 0.1f, 0f, 0f, 20f,
                0.05f, 1.05f, 0f, 0f, 10f,
                0f, 0.05f, 1f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )
    ),

    Pastel(
        title = R.string.filter_pastel,
        matrix = ColorMatrix().let {
            it.setToSaturation(0.8f)

            it[0, 0] *= 1.1f
            it[1, 1] *= 1.1f
            it[2, 2] *= 1.1f

            it[0, 2] *= 0.1f

            it[0, 4] = 10f
            it[1, 4] = 10f
            it[2, 4] = 10f

            return@let it
        }
    ),

    Rustic(
        title = R.string.filter_rustic,
        matrix = ColorMatrix(
            floatArrayOf(
                1.65f, 0f, 0f, 0f, -45f,
                0f, 1.65f, 0f, 0f, -45f,
                0f, 0f, 1.65f, 0f, -45f,
                0f, 0f, 1f, 0f, 1f
            )
        )
    ),

    Dried(
        title = R.string.filter_dried,
        matrix = ColorMatrix().let {
            it.setToSaturation(0.6f)

            it[0, 0] *= 1.1f
            it[1, 1] *= 1.1f
            it[2, 2] *= 1.1f

            it[0, 4] = 25f
            it[1, 4] = 25f
            it[2, 4] = 25f

            return@let it
        }
    ),

    Toast(
        title = R.string.filter_toast,
        matrix = ColorMatrix().let {
            it.setToSaturation(0.6f)

            it[0, 0] *= 1.25f
            it[1, 0] *= 1.25f
            it[2, 0] *= 1.25f

            it[0, 1] *= 1.25f
            it[1, 1] *= 1.25f
            it[2, 1] *= 1.25f

            it[0, 2] *= 1.25f
            it[1, 2] *= 1.25f
            it[2, 2] *= 1.25f

            it[0, 4] = -45f
            it[1, 4] = -45f
            it[2, 4] = -45f

            return@let it
        }
    ),

    BGR(
        title = R.string.filter_bgr,
        matrix = ColorMatrix(
            floatArrayOf(
                0f, 0f, 1f, 0f, 0f,
                0f, 1f, 0f, 0f, 0f,
                1f, 0f, 0f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )
    ),

    Mushrooms(
        title = R.string.filter_mushrooms,
        matrix = ColorMatrix().apply {
            setToRotateRed(-22f)
            setToRotateGreen(47f)
            setToRotateBlue(62f)
        }
    ),

    Solar(
        title = R.string.filter_solar,
        matrix = ColorMatrix(
            floatArrayOf(
                1.2f, 0f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f, 0f,
                0f, 0f, 1f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        ).let {
            val satMatrix = ColorMatrix().apply {
                setToSaturation(1.5f)
            }

            it[0, 0] *= satMatrix[0, 0]
            it[1, 1] *= satMatrix[1, 1]
            it[2, 2] *= satMatrix[2, 2]

            return@let it
        }
    ),

    Shimmer(
        title = R.string.filter_shimmer,
        matrix = ColorMatrix(
            floatArrayOf(
                34f, 0f, 0f, 0f, -831f,
                0f, 9f, 0f, 0f, -831f,
                0f, 0f, 48f, 0f, -831f,
                0f, 0f, 0f, 1f, 0f
            )
        )
    )
}