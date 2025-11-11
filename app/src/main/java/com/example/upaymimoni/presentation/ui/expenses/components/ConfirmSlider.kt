package com.example.upaymimoni.presentation.ui.expenses.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.upaymimoni.presentation.ui.theme.UPayMiMoniTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun ConfirmSlider(
    sliderText: String = "Confirm",
    onConfirmed: () -> Unit
) {
    //sizes and offsets, used for movement and detecting reached end
    var barSize by remember { mutableStateOf(IntSize.Zero) }
    var handleSize by remember { mutableStateOf(IntSize.Zero) }
    val dragOffset = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    var confirmed by remember { mutableStateOf(false) }

    //Bar
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(60.dp)
            .onGloballyPositioned { barSize = it.size }
            .background(MaterialTheme.colorScheme.surfaceContainerHighest, RoundedCornerShape(50))
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.surfaceDim,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(50),
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        val maxOffset = (barSize.width - handleSize.width).toFloat()

        // Text in center
        Text(
            text = "Slide to $sliderText",
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.align(Alignment.Center)
        )

        // Draggable handle
        Box(
            modifier = Modifier
                .offset {
                    //unlock position if not confirmed, else lock
                    if (!confirmed) {
                        IntOffset(dragOffset.value.roundToInt(), 0)
                    } else {
                        IntOffset(maxOffset.roundToInt(), 0)
                    }
                }
                .size(56.dp)
                .onGloballyPositioned { handleSize = it.size }
                .background(
                    MaterialTheme.colorScheme.primary,
                    androidx.compose.foundation.shape.RoundedCornerShape(50)
                )
                .pointerInput(maxOffset) {
                    if (!confirmed) {
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
                                        confirmed = true
                                        dragOffset.animateTo(maxOffset, tween(180))
                                        onConfirmed()
                                    } else {
                                        dragOffset.animateTo(0f, tween(200))
                                    }
                                }
                            }
                        )
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            if (confirmed) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "ConfirmArrow",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewConfirmSlider() {
    UPayMiMoniTheme(darkTheme = false) {
        ConfirmSlider(
            sliderText = "Slide to Confirm",
            onConfirmed = {}
        )
    }
}