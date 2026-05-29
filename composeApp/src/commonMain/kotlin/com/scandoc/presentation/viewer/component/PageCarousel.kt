package com.scandoc.presentation.viewer.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.scandoc.domain.model.Page
import com.scandoc.presentation.component.PaperPreview
import com.scandoc.presentation.component.ScanDocSection
import com.scandoc.presentation.component.fittedPreviewSize

@Composable
fun PageCarousel(
    pages: List<Page>,
    currentPageIndex: Int,
    modifier: Modifier = Modifier,
) {
    ScanDocSection(modifier = modifier) {
        val label = if (pages.isEmpty()) {
            "No pages"
        } else {
            "Page ${currentPageIndex + 1} of ${pages.size}"
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            BoxWithConstraints(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                val preview = fittedPreviewSize(
                    maxWidth = maxWidth.value,
                    maxHeight = maxHeight.value,
                    preferredWidth = 210f,
                    preferredHeight = 286f,
                )
            PaperPreview(
                modifier = Modifier
                        .width(preview.width.dp)
                        .height(preview.height.dp)
                    .graphicsLayer { rotationZ = -2f },
            )
            }
            Text(
                text = label,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}
