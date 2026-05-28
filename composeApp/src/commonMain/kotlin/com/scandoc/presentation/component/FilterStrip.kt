package com.scandoc.presentation.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.scandoc.domain.model.Filter
import com.scandoc.presentation.theme.ScanDocDimens

@Composable
fun FilterStrip(
    selected: Filter,
    onSelected: (Filter) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceXs),
    ) {
        Filter.entries.forEach { filter ->
            ScanDocChip(
                text = filter.label,
                selected = filter == selected,
                onClick = { onSelected(filter) },
            )
        }
    }
}

val Filter.label: String
    get() = when (this) {
        Filter.Auto -> "Auto"
        Filter.BlackWhite -> "B&W"
        Filter.Ink -> "Ink"
        Filter.Photo -> "Photo"
        Filter.Grayscale -> "Gray"
        Filter.Original -> "Original"
    }
