package com.example.compose.jetchat.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.Layout
import kotlin.math.roundToInt

/**
 * author：yangweichao@reworldgame.com
 * data: 2022/3/21 14:35
 * Fab 动画
 */
@Composable
fun AnimatingFabContent(
    icon: @Composable () -> Unit,
    text: @Composable () -> Unit,
    modifier: Modifier = Modifier, extended: Boolean = true
) {
    val currentState = if (extended) {
        ExpandableFabStates.Extended
    } else {
        ExpandableFabStates.Collapsed
    }

    val transition = updateTransition(targetState = currentState)
    val textOpacity by transition.animateFloat(transitionSpec = {
        if (targetState == ExpandableFabStates.Collapsed) {
            tween(
                easing = LinearEasing,
                durationMillis = (transitionDuration / 12f * 5).roundToInt()
            )
        } else {
            tween(
                easing = LinearEasing, delayMillis = (transitionDuration / 3f).roundToInt(),
                durationMillis = (transitionDuration / 12f * 5).roundToInt()
            )
        }
    }) { progress ->
        if (progress == ExpandableFabStates.Collapsed) {
            0f
        } else {
            1f
        }
    }

    val fabWidthFactor by transition.animateFloat(transitionSpec = {
        if (targetState == ExpandableFabStates.Collapsed) {
            tween(easing = FastOutLinearInEasing, durationMillis = transitionDuration)
        } else {
            tween(easing = FastOutSlowInEasing, durationMillis = transitionDuration)
        }
    }) { progress ->
        if (progress == ExpandableFabStates.Collapsed) {
            0f
        } else {
            1f
        }
    }

    IconAndTextRow(
        icon, text, { textOpacity }, { fabWidthFactor }, modifier = modifier
    )


}

@Composable
fun IconAndTextRow(
    icon: @Composable () -> Unit,
    text: @Composable () -> Unit,
    opacityProgress: () -> Float,
    widthProgress: () -> Float,
    modifier: Modifier
) {
    Layout(modifier = modifier, content = {
        icon()
        Box(modifier = Modifier.alpha(opacityProgress())) {
            text()
        }
    }) { measurables, constraints ->
        val iconPlaceable = measurables[0].measure(constraints)
        val textPlaceable = measurables[1].measure(constraints)
        val height = constraints.maxHeight
        //FAB 的纵横比为 1，因此初始宽度是高度
        val initialWidth = height.toFloat()
        val iconPadding = (initialWidth.minus(iconPlaceable.width)) / 2f
        val expandedWidth = iconPlaceable.width + textPlaceable.width + iconPadding * 3
        //应用动画因子以从 initialWidth 变为 fullWidth
        val with = androidx.compose.ui.util.lerp(initialWidth, expandedWidth, widthProgress())
        layout(with.roundToInt(), height) {
            iconPlaceable.place(
                iconPadding.roundToInt(),
                constraints.maxHeight / 2 - iconPlaceable.height / 2
            )
            textPlaceable.place(
                (iconPlaceable.width + iconPadding * 2).roundToInt(),
                constraints.maxHeight / 2 - textPlaceable.height / 2
            )
        }


    }
}

private enum class ExpandableFabStates { Collapsed, Extended }

private const val transitionDuration = 200