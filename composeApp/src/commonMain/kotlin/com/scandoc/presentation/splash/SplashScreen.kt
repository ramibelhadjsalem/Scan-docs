package com.scandoc.presentation.splash

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.scandoc.presentation.component.PaperStackMark
import com.scandoc.presentation.component.ScanDocButton
import com.scandoc.presentation.component.ScanDocGridBackground
import com.scandoc.presentation.theme.ScanDocDimens

@Composable
fun SplashScreen(
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ScanDocGridBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = ScanDocDimens.spaceXl, vertical = ScanDocDimens.space3xl),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceSm)) {
                Text(
                    text = "ScanDoc",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Private document scanning",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            PaperStackMark(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(width = 280.dp, height = 340.dp),
                animatedScan = true,
            )

            Column(verticalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceMd)) {
                Text(
                    text = "Scan cleaner.\nFinish faster.",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Edge detection, OCR, and export stay one thumb away from the first open.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Spacer(modifier = Modifier.height(ScanDocDimens.spaceXs))
                ScanDocButton(
                    text = "Start scanning",
                    onClick = onContinue,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
