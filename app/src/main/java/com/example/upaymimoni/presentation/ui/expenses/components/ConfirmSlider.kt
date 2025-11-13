package com.example.upaymimoni.presentation.ui.expenses.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.upaymimoni.presentation.ui.theme.UPayMiMoniTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun ConfirmSlider(
    sliderText: String = "Confirm",
    onConfirmed: () -> Unit,
    isConfirmed: Boolean,
    onSetSliderConfirmed: () -> Unit
) {
    //sizes and offsets, used for movement and detecting reached end
    var barSize by remember { mutableStateOf(IntSize.Zero) }
    var handleSize by remember { mutableStateOf(IntSize.Zero) }
    val dragOffset = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    // Track Elevation
    val trackTonalElevation = 16.dp
    println("ISCONFIRMED : $isConfirmed")
    // Reset the drag offset, if the state changes from confirmed to false
    LaunchedEffect(isConfirmed) {
        if (!isConfirmed) {
            println("drag : ${dragOffset.value}")
            // Reset to start position if changed to false
            dragOffset.animateTo(0f, tween(300))
            println("drag : ${dragOffset.value}")
        }
    }

    //Bar
    Surface(
        tonalElevation = trackTonalElevation,
        color = MaterialTheme.colorScheme.surfaceContainerHighest,
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(60.dp)
            .onGloballyPositioned { barSize = it.size }
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.surfaceDim,
                shape = RoundedCornerShape(50),
            )
    ) {
        // calculate maximum drag offset / at end of track
        val maxOffset = (barSize.width - handleSize.width).toFloat()

        // calculate the current position for the handle
        // unlock position if not confirmed, else lock
        val handleXPosition = if (isConfirmed) {
            maxOffset
        } else {
            dragOffset.value
        }

        // Calculate the drag progress (0f to 1f)
        val dragProgress = if (maxOffset > 0) (handleXPosition / maxOffset)
            .coerceIn(0f, 1f) else 0f
        //Icon Rotation (0 to 180 degrees)
        val rotationAngle = dragProgress * 180f
        //animating text opacity to drag process
        val textOpacity = (1f - (dragProgress * 2f)).coerceIn(0f, 1f)
        // Calculates the width of the progress fill
        val fillWidthPx = (handleXPosition + handleSize.width / 2f) + 50 // +50 to middle

        Box(contentAlignment = Alignment.CenterStart) {
            Surface(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .height(60.dp)
                    .width(with(density) { fillWidthPx.toDp() })
            ) {}

            // Text in center
            Text(
                text = "Slide to $sliderText",
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    // animate opacity
                    .graphicsLayer { alpha = textOpacity },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )

            // Draggable handle
            Surface(
                color = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape,
                modifier = Modifier
                    .offset {
                        // set handle x pos with offset
                        IntOffset(handleXPosition.roundToInt(),0)
                    }
                    .size(56.dp)
                    .onGloballyPositioned { handleSize = it.size }
                    .pointerInput(maxOffset, isConfirmed) {
                        if (!isConfirmed) {
                            detectDragGestures(
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    scope.launch {
                                        dragOffset.snapTo(
                                            (dragOffset.value + dragAmount.x)
                                                .coerceIn(0f, maxOffset)
                                        )
                                    }
                                },
                                onDragEnd = {
                                    scope.launch {
                                        val shouldConfirm = dragOffset.value > maxOffset * 0.75f
                                        // Trigger confirm if released at end
                                        if (shouldConfirm) {
                                            onSetSliderConfirmed()
                                            delay(500L) // just to view the change
                                            onConfirmed()
                                            // animate to end
                                            dragOffset.animateTo(maxOffset, tween(180))
                                        } else {
                                            dragOffset.animateTo(0f, tween(200))
                                        }
                                    }
                                }
                            )
                        }
                    },
            ) {
                if (isConfirmed) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(8.dp)
                    )
                } else {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "ConfirmArrow",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(8.dp)
                            .graphicsLayer { rotationZ = rotationAngle }
                    )
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewConfirmSlider() {
    UPayMiMoniTheme(darkTheme = false) {
        ConfirmSlider(
            sliderText = "Confirm",
            onConfirmed = {},
            isConfirmed = false,
            onSetSliderConfirmed = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewConfirmSliderIsConfirmed() {
    UPayMiMoniTheme(darkTheme = true) {
        ConfirmSlider(
            sliderText = "Confirm",
            onConfirmed = {},
            isConfirmed = true,
            onSetSliderConfirmed = {}
        )
    }
}