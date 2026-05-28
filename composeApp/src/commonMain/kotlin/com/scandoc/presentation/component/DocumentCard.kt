package com.scandoc.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
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
fun DocumentCard(
    document: Document,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = ScanDocShapes.Medium,
        colors = CardDefaults.cardColors(containerColor = ScanDocColors.Ink2),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3f / 4f)
                    .background(color = ScanDocColors.Ink3, shape = ScanDocShapes.Medium),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    modifier = Modifier.size(ScanDocDimens.iconSizeLg),
                    tint = ScanDocColors.Text3,
                )
            }

            Column(
                modifier = Modifier.padding(ScanDocDimens.spaceSm),
                verticalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceXxs),
            ) {
                Text(
                    text = document.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = ScanDocColors.Text,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    val pageCount = document.pages.size
                    val pageLabel = "$pageCount page${if (pageCount == 1) "" else "s"}"
                    Text(
                        text = pageLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = ScanDocColors.Text3,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = document.updatedAt.toString().take(10),
                        style = MaterialTheme.typography.labelSmall,
                        color = ScanDocColors.Text3,
                    )
                }
            }
        }
    }
}

private fun previewDocument(): Document = Document(
    id = "doc-preview-1",
    name = "Invoice Q4",
    createdAt = Instant.parse("2024-10-01T09:00:00Z"),
    updatedAt = Instant.parse("2024-12-15T14:30:00Z"),
    pages = listOf(
        Page(
            id = "p1",
            orderIndex = 0,
            imagePath = "/path/to/page.jpg",
            ocrResult = null,
            width = 1080,
            height = 1920,
        ),
    ),
    thumbnailPath = null,
    tags = listOf("finance", "q4"),
)

@Preview
@Composable
fun DocumentCardDarkPreview() {
    ScanDocTheme {
        Surface(color = ScanDocColors.Ink) {
            DocumentCard(
                document = previewDocument(),
                onClick = {},
                modifier = Modifier
                    .padding(ScanDocDimens.spaceMd)
                    .size(width = 160.dp, height = 240.dp),
            )
        }
    }
}

@Preview
@Composable
fun DocumentCardLightPreview() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        Surface {
            DocumentCard(
                document = previewDocument().copy(
                    name = "Contract Draft — a very long document name that should truncate",
                    pages = listOf(
                        Page(id = "p1", orderIndex = 0, imagePath = "/p1.jpg", ocrResult = null, width = 1080, height = 1920),
                        Page(id = "p2", orderIndex = 1, imagePath = "/p2.jpg", ocrResult = null, width = 1080, height = 1920),
                    ),
                ),
                onClick = {},
                modifier = Modifier
                    .padding(ScanDocDimens.spaceMd)
                    .size(width = 160.dp, height = 240.dp),
            )
        }
    }
}
