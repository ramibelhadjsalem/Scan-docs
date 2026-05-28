package com.scandoc.presentation.library.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.scandoc.domain.model.Document
import com.scandoc.presentation.component.DocumentCard

@Composable
fun DocumentRow(
    document: Document,
    onOpen: () -> Unit,
    onRename: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DocumentCard(
        document = document,
        onClick = onOpen,
        onLongClick = onRename,
        modifier = modifier,
    )
}
