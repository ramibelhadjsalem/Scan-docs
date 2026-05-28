package com.scandoc.presentation.crop.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.scandoc.domain.model.Filter
import com.scandoc.presentation.component.FilterStrip

@Composable
fun FilterPicker(
    selected: Filter,
    onSelected: (Filter) -> Unit,
    modifier: Modifier = Modifier,
) {
    FilterStrip(
        selected = selected,
        onSelected = onSelected,
        modifier = modifier,
    )
}
