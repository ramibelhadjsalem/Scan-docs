package com.scandoc.presentation.me

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.scandoc.presentation.component.ScanDocGridBackground
import com.scandoc.presentation.theme.ScanDocColors
import com.scandoc.presentation.theme.ScanDocDimens

@Composable
fun MeScreen(modifier: Modifier = Modifier) {
    ScanDocGridBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(horizontal = ScanDocDimens.spaceXl, vertical = ScanDocDimens.space3xl),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Me",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(ScanDocDimens.space3xl))

            Box(
                modifier = Modifier
                    .size(ScanDocDimens.space6xl)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(ScanDocDimens.space4xl),
                )
            }

            Spacer(Modifier.height(ScanDocDimens.spaceMd))

            Text(
                text = "Guest User",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = "Sign in to sync your documents",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(Modifier.height(ScanDocDimens.space3xl))

            val rows = listOf(
                Icons.Filled.Person to "Account",
                Icons.Filled.Share to "Storage",
                Icons.Filled.Settings to "Preferences",
                Icons.Filled.Info to "About",
            )
            rows.forEach { (icon, label) ->
                MeRow(icon = icon, label = label)
            }
        }
    }
}

@Composable
private fun MeRow(
    icon: ImageVector,
    label: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(ScanDocDimens.space4xl),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceMd),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(ScanDocDimens.iconSize),
        )
        Text(
            text = label,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f),
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
    HorizontalDivider(color = ScanDocColors.Text.copy(alpha = 0.1f))
}
