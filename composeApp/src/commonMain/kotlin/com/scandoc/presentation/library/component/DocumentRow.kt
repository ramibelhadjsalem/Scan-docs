package com.scandoc.presentation.library.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.scandoc.domain.model.Document
import com.scandoc.presentation.component.DocumentCard
import com.scandoc.presentation.library.documentBadge
import com.scandoc.presentation.library.documentPageSummary
import com.scandoc.presentation.library.hasOcrText

@Composable
fun DocumentRow(
    document: Document,
    onOpen: () -> Unit,
    onRename: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
        onClick = onOpen,
        onLongClick = onRename,
        modifier = modifier,
    )
}
