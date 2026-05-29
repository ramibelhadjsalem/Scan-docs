package com.scandoc.presentation.tools.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun ToolSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier,
    )
}
