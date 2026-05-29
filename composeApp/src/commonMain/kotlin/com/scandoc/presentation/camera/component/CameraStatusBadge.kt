package com.scandoc.presentation.camera.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.scandoc.presentation.theme.ScanDocDimens

@Composable
fun CameraStatusBadge(
    text: String,
    isError: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.72f),
                shape = RoundedCornerShape(ScanDocDimens.cardRadius),
            )
            .padding(horizontal = ScanDocDimens.spaceLg, vertical = ScanDocDimens.spaceSm),
    ) {
        Text(
            text = text,
            color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
