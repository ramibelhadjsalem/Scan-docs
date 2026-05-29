package com.scandoc.presentation.camera.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.scandoc.presentation.camera.CameraFeature
import com.scandoc.presentation.theme.ScanDocColors
import com.scandoc.presentation.theme.ScanDocDimens

@Composable
fun FeatureStrip(
    features: List<CameraFeature>,
    selected: CameraFeature,
    onSelect: (CameraFeature) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier.height(ScanDocDimens.cameraFeatureStripHeight),
        contentPadding = PaddingValues(horizontal = ScanDocDimens.spaceXl),
        horizontalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceXl),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items(items = features, key = { it.label }) { feature ->
            FeatureItem(
                feature = feature,
                selected = feature == selected,
                onSelect = onSelect,
            )
        }
    }
}

@Composable
private fun FeatureItem(
    feature: CameraFeature,
    selected: Boolean,
    onSelect: (CameraFeature) -> Unit,
) {
    Column(
        modifier = Modifier.clickable { onSelect(feature) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceXxs),
    ) {
        Text(
            text = feature.label,
            color = if (selected) ScanDocColors.Teal else MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium,
        )
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .width(ScanDocDimens.spaceLg)
                .height(ScanDocDimens.cameraSelectionIndicatorHeight)
                .background(if (selected) ScanDocColors.Teal else androidx.compose.ui.graphics.Color.Transparent),
        )
    }
}
