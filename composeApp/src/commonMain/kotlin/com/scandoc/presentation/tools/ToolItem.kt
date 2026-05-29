package com.scandoc.presentation.tools

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class ToolItem(
    val id: String,
    val label: String,
    val icon: ImageVector,
    val bgTint: Color,
    val iconTint: Color,
)

data class ToolSection(
    val title: String,
    val items: List<ToolItem>,
)
