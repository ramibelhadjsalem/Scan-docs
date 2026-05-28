package com.scandoc.presentation.camera.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.scandoc.presentation.theme.ScanDocDimens

@Composable
fun ModeSwitcher(
    autoCapture: Boolean,
    onAutoCaptureChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceSm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("Auto")
        Switch(checked = autoCapture, onCheckedChange = onAutoCaptureChange)
    }
}
