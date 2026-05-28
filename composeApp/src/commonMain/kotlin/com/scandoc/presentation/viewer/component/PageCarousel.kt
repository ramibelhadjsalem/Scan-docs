package com.scandoc.presentation.viewer.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.scandoc.domain.model.Page
import com.scandoc.presentation.theme.ScanDocColors
import com.scandoc.presentation.theme.ScanDocDimens
import com.scandoc.presentation.theme.ScanDocShapes
import com.scandoc.presentation.theme.ScanDocTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PageCarousel(
    pages: List<Page>,
    currentPageIndex: Int,
    onPageChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(
        initialPage = currentPageIndex,
        pageCount = { pages.size },
    )

    LaunchedEffect(pagerState.currentPage) {
        onPageChanged(pagerState.currentPage)
    }

    LaunchedEffect(currentPageIndex) {
        if (currentPageIndex != pagerState.currentPage) {
            pagerState.scrollToPage(currentPageIndex)
        }
    }

    Column(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
        ) { pageIndex ->
            val page = pages.getOrNull(pageIndex)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(ScanDocColors.Ink3),
            ) {
                if (page != null) {
                    Text(
                        text = "Page ${page.orderIndex + 1}",
                        style = MaterialTheme.typography.titleLarge,
                        color = ScanDocColors.Text3,
                    )
                }
            }
        }

        if (pages.size > 1) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceXxs),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = ScanDocDimens.spaceSm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(
                            space = ScanDocDimens.spaceXxs,
                            alignment = Alignment.CenterHorizontally,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        pages.forEachIndexed { index, _ ->
                            val isActive = index == pagerState.currentPage
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(ScanDocShapes.Pill)
                                    .background(
                                        color = if (isActive) ScanDocColors.Signal else ScanDocColors.Line,
                                    ),
                            )
                        }
                    }
                }
            }
        }
    }
}

private val previewPages = listOf(
    Page(id = "p1", orderIndex = 0, imagePath = "/fake/p1.jpg", ocrResult = null, width = 1080, height = 1920),
    Page(id = "p2", orderIndex = 1, imagePath = "/fake/p2.jpg", ocrResult = null, width = 1080, height = 1920),
    Page(id = "p3", orderIndex = 2, imagePath = "/fake/p3.jpg", ocrResult = null, width = 1080, height = 1920),
)

@Preview
@Composable
fun PageCarouselDarkPreview() {
    ScanDocTheme {
        Surface(
            color = ScanDocColors.Ink,
            modifier = Modifier.size(width = 360.dp, height = 480.dp),
        ) {
            PageCarousel(
                pages = previewPages,
                currentPageIndex = 0,
                onPageChanged = {},
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Preview
@Composable
fun PageCarouselLightPreview() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        Surface(modifier = Modifier.size(width = 360.dp, height = 480.dp)) {
            PageCarousel(
                pages = previewPages,
                currentPageIndex = 1,
                onPageChanged = {},
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
