package com.scandoc.presentation.library.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.scandoc.domain.model.Document
import com.scandoc.domain.model.Page
import com.scandoc.presentation.theme.ScanDocColors
import com.scandoc.presentation.theme.ScanDocDimens
import com.scandoc.presentation.theme.ScanDocShapes
import com.scandoc.presentation.theme.ScanDocTheme
import kotlinx.datetime.Instant
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DocumentRow(
    document: Document,
    onOpen: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onOpen,
        color = Color.Transparent,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = ScanDocDimens.spaceMd,
                        vertical = ScanDocDimens.spaceSm,
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    color = ScanDocColors.Ink3,
                    shape = ScanDocShapes.Small,
                    modifier = Modifier.size(48.dp),
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(48.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = ScanDocColors.Text3,
                            modifier = Modifier.size(ScanDocDimens.iconSize),
                        )
                    }
                }

                Spacer(modifier = Modifier.width(ScanDocDimens.spaceMd))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = document.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = ScanDocColors.Text,
                        maxLines = 1,
                    )
                    Text(
                        text = "${document.pages.size} pages · ${document.updatedAt.toString().take(10)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ScanDocColors.Text3,
                        maxLines = 1,
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete document",
                        tint = ScanDocColors.Text3,
                    )
                }
            }

            HorizontalDivider(
                color = ScanDocColors.Line,
                modifier = Modifier.padding(horizontal = ScanDocDimens.spaceMd),
            )
        }
    }
}

private val previewDocument = Document(
    id = "doc-001",
    name = "Q4 Financial Report.pdf",
    createdAt = Instant.parse("2024-11-01T09:00:00Z"),
    updatedAt = Instant.parse("2024-12-15T14:30:00Z"),
    pages = listOf(
        Page(id = "p1", orderIndex = 0, imagePath = "/fake/p1.jpg", ocrResult = null, width = 1080, height = 1920),
        Page(id = "p2", orderIndex = 1, imagePath = "/fake/p2.jpg", ocrResult = null, width = 1080, height = 1920),
    ),
    thumbnailPath = null,
    tags = listOf("finance"),
)

@Preview
@Composable
fun DocumentRowDarkPreview() {
    ScanDocTheme {
        Surface(color = ScanDocColors.Ink) {
            DocumentRow(
                document = previewDocument,
                onOpen = {},
                onDelete = {},
            )
        }
    }
}

@Preview
@Composable
fun DocumentRowLightPreview() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        Surface {
            DocumentRow(
                document = previewDocument,
                onOpen = {},
                onDelete = {},
            )
        }
    }
}
