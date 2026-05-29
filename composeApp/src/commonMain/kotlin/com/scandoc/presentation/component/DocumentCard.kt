package com.scandoc.presentation.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.scandoc.domain.model.Document
import com.scandoc.presentation.theme.ScanDocColors
import com.scandoc.presentation.theme.ScanDocDimens

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DocumentCard(
    document: Document,
    summary: String,
    badge: String,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val borderColor = if (isSelected) {
        ScanDocColors.Teal
    } else {
        ScanDocColors.Text.copy(alpha = 0.09f)
    }
    val borderWidth = if (isSelected) 2.dp else 1.dp
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(ScanDocDimens.cardRadius))
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
            .border(
                width = borderWidth,
                color = borderColor,
                shape = RoundedCornerShape(ScanDocDimens.cardRadius),
            )
            .background(ScanDocColors.Text.copy(alpha = 0.045f))
            .padding(ScanDocDimens.spaceSm),
        horizontalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceSm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PaperThumbnail(pageCount = document.pages.size)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = document.name,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
            )
            Spacer(modifier = Modifier.height(ScanDocDimens.spaceXxs))
            Text(
                text = summary,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
            )
        }
        ScanDocPill(text = badge)
    }
}
