package com.scandoc.presentation.camera.component

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import com.scandoc.presentation.theme.ScanDocColors
import com.scandoc.presentation.theme.ScanDocDimens

@Composable
fun ShutterButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.92f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "shutter_scale",
    )

    Box(
        modifier = modifier
            .size(ScanDocDimens.shutterSize)
            .scale(scale)
            .border(
                width = ScanDocDimens.shutterRingWidth,
                color = ScanDocColors.Teal,
                shape = CircleShape,
            )
            .background(Color.Transparent, CircleShape)
            .pointerInput(onClick) {
                detectTapGestures(
                    onPress = {
                        pressed = true
                        val released = tryAwaitRelease()
                        pressed = false
                        if (released) onClick()
                    },
                )
            },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(ScanDocDimens.shutterInnerSize)
                .background(MaterialTheme.colorScheme.onSurface, CircleShape),
        )
    }
}
