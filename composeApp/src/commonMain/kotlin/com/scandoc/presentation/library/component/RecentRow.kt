package com.scandoc.presentation.library.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.scandoc.domain.model.Document
import com.scandoc.presentation.component.DocumentCard
import com.scandoc.presentation.library.documentBadge
import com.scandoc.presentation.library.documentPageSummary
import com.scandoc.presentation.library.hasOcrText
import com.scandoc.presentation.theme.ScanDocDimens

@Composable
fun RecentRow(
    documents: List<Document>,
    onOpen: (Document) -> Unit,
    onRename: (Document) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (documents.isEmpty()) return

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceSm),
    ) {
        Text(
            text = "Recent",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = ScanDocDimens.spaceXl),
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = ScanDocDimens.spaceXl),
            horizontalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceMd),
        ) {
            items(documents, key = { it.id }) { document ->
                val hasOcrText = document.hasOcrText()
                DocumentCard(
                    document = document,
                    summary = documentPageSummary(
                        pageCount = document.pages.size,
                        hasOcrText = hasOcrText,
                    ),
                    badge = documentBadge(
                        pageCount = document.pages.size,
                        hasOcrText = hasOcrText,
                    ),
                    onClick = { onOpen(document) },
                    onLongClick = { onRename(document) },
                    modifier = Modifier
                        .width(ScanDocDimens.recentCardWidth)
                        .height(ScanDocDimens.recentCardHeight),
                )
            }
        }
    }
}
