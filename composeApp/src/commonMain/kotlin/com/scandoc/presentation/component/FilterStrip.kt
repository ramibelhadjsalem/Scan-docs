package com.scandoc.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.scandoc.domain.model.Filter
import com.scandoc.presentation.theme.ScanDocColors
import com.scandoc.presentation.theme.ScanDocDimens
import com.scandoc.presentation.theme.ScanDocTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

private fun Filter.displayLabel(): String = when (this) {
    Filter.Auto -> "Auto"
    Filter.BlackWhite -> "B&W"
    Filter.Ink -> "Ink"
    Filter.Photo -> "Photo"
    Filter.Grayscale -> "Gray"
    Filter.Original -> "Original"
}

@Composable
fun FilterStrip(
    filters: List<Filter>,
    activeFilter: Filter,
    onFilterSelected: (Filter) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceXs),
        contentPadding = PaddingValues(horizontal = ScanDocDimens.spaceMd),
    ) {
        items(items = filters, key = { it.name }) { filter ->
            ScanDocChip(
                label = filter.displayLabel(),
                selected = filter == activeFilter,
                onClick = { onFilterSelected(filter) },
            )
        }
    }
}

@Preview
@Composable
fun FilterStripDarkPreview() {
    ScanDocTheme {
        Surface(color = ScanDocColors.Ink) {
            var active by remember { mutableStateOf(Filter.Auto) }
            FilterStrip(
                filters = Filter.entries,
                activeFilter = active,
                onFilterSelected = { active = it },
            )
        }
    }
}

@Preview
@Composable
fun FilterStripLightPreview() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        Surface {
            var active by remember { mutableStateOf(Filter.Photo) }
            FilterStrip(
                filters = Filter.entries,
                activeFilter = active,
                onFilterSelected = { active = it },
            )
        }
    }
}
