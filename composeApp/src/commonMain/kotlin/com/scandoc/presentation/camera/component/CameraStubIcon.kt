package com.scandoc.presentation.camera.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.scandoc.presentation.theme.ScanDocDimens

@Composable
fun CameraStubIcon(
    imageVector: ImageVector,
    contentDescription: String,
) {
    IconButton(
        onClick = {},
        modifier = Modifier.size(ScanDocDimens.cameraControlButtonSize),
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onSurface,
        )
    }
}
