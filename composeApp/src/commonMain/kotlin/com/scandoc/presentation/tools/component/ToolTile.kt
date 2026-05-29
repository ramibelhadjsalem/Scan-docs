package com.scandoc.presentation.tools.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.scandoc.presentation.theme.ScanDocDimens
import com.scandoc.presentation.tools.ToolItem

@Composable
fun ToolTile(
    item: ToolItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .width(ScanDocDimens.toolItemWidth)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceXs),
    ) {
        Box(
            modifier = Modifier
                .size(ScanDocDimens.toolIconContainerSize)
                .background(item.bgTint, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = item.iconTint,
                modifier = Modifier.size(ScanDocDimens.toolIconSize),
            )
        }

        Text(
            text = item.label,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 13.sp,
            maxLines = 2,
            textAlign = TextAlign.Center,
        )
    }
}
