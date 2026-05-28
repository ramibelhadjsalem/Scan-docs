package com.scandoc.presentation.crop.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.scandoc.domain.model.Filter
import com.scandoc.presentation.theme.ScanDocColors
import com.scandoc.presentation.theme.ScanDocDimens
import com.scandoc.presentation.theme.ScanDocShapes
import com.scandoc.presentation.theme.ScanDocTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

private fun filterDisplayName(filter: Filter): String = when (filter) {
    Filter.Auto -> "Auto"
    Filter.BlackWhite -> "B&W"
    Filter.Ink -> "Ink"
    Filter.Photo -> "Photo"
    Filter.Grayscale -> "Gray"
    Filter.Original -> "Orig"
}

private fun filterPreviewColor(filter: Filter): Color = when (filter) {
    Filter.Auto -> ScanDocColors.Ink3
    Filter.BlackWhite -> Color(0xFF2D2D2D)
    Filter.Ink -> Color(0xFF1A2040)
    Filter.Photo -> Color(0xFF1A3040)
    Filter.Grayscale -> Color(0xFF404040)
    Filter.Original -> ScanDocColors.Ink2
}

@Composable
fun FilterPicker(
    filters: List<Filter>,
    activeFilter: Filter,
    onFilterSelected: (Filter) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = ScanDocDimens.spaceMd),
        horizontalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceXs),
    ) {
        items(filters) { filter ->
            val isActive = filter == activeFilter

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onFilterSelected(filter) },
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            color = filterPreviewColor(filter),
                            shape = ScanDocShapes.Small,
                        ),
                )
                Spacer(modifier = Modifier.height(ScanDocDimens.spaceXxs))
                Text(
                    text = filterDisplayName(filter),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isActive) ScanDocColors.Signal else ScanDocColors.Text3,
                )
                Spacer(modifier = Modifier.height(ScanDocDimens.spaceXxs))
                if (isActive) {
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .background(
                                color = ScanDocColors.Signal,
                                shape = CircleShape,
                            ),
                    )
                } else {
                    Box(modifier = Modifier.size(4.dp))
                }
            }
        }
    }
}

@Preview
@Composable
private fun FilterPickerDarkPreview() {
    ScanDocTheme {
        FilterPicker(
            filters = Filter.entries,
            activeFilter = Filter.Auto,
            onFilterSelected = {},
        )
    }
}

@Preview
@Composable
private fun FilterPickerLightPreview() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        Surface {
            FilterPicker(
                filters = Filter.entries,
                activeFilter = Filter.Photo,
                onFilterSelected = {},
            )
        }
    }
}
