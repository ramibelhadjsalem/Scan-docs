package com.scandoc.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.scandoc.presentation.theme.ScanDocColors
import com.scandoc.presentation.theme.ScanDocDimens
import com.scandoc.presentation.theme.ScanDocShapes
import com.scandoc.presentation.theme.ScanDocTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

sealed interface ScanDocButtonVariant {
    data object Primary : ScanDocButtonVariant
    data object Secondary : ScanDocButtonVariant
    data object Ghost : ScanDocButtonVariant
    data object Danger : ScanDocButtonVariant
}

@Composable
fun ScanDocButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ScanDocButtonVariant = ScanDocButtonVariant.Primary,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    isLoading: Boolean = false,
) {
    val containerColor: Color
    val contentColor: Color

    when (variant) {
        ScanDocButtonVariant.Primary -> {
            containerColor = ScanDocColors.Signal
            contentColor = ScanDocColors.Ink
        }
        ScanDocButtonVariant.Secondary -> {
            containerColor = ScanDocColors.Ink3
            contentColor = ScanDocColors.Text
        }
        ScanDocButtonVariant.Ghost -> {
            containerColor = Color.Transparent
            contentColor = ScanDocColors.Signal
        }
        ScanDocButtonVariant.Danger -> {
            containerColor = ScanDocColors.Danger
            contentColor = Color.White
        }
    }

    if (variant == ScanDocButtonVariant.Ghost) {
        TextButton(
            onClick = onClick,
            modifier = modifier.height(ScanDocDimens.buttonHeight),
            enabled = enabled && !isLoading,
            shape = ScanDocShapes.Pill,
            colors = ButtonDefaults.textButtonColors(
                contentColor = contentColor,
                disabledContentColor = contentColor.copy(alpha = 0.38f),
            ),
        ) {
            ButtonContent(
                text = text,
                leadingIcon = leadingIcon,
                isLoading = isLoading,
                contentColor = contentColor,
            )
        }
    } else {
        Button(
            onClick = onClick,
            modifier = modifier.height(ScanDocDimens.buttonHeight),
            enabled = enabled && !isLoading,
            shape = ScanDocShapes.Pill,
            colors = ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = contentColor,
                disabledContainerColor = containerColor.copy(alpha = 0.38f),
                disabledContentColor = contentColor.copy(alpha = 0.38f),
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
                disabledElevation = 0.dp,
            ),
        ) {
            ButtonContent(
                text = text,
                leadingIcon = leadingIcon,
                isLoading = isLoading,
                contentColor = contentColor,
            )
        }
    }
}

@Composable
private fun ButtonContent(
    text: String,
    leadingIcon: ImageVector?,
    isLoading: Boolean,
    contentColor: Color,
) {
    if (isLoading) {
        CircularProgressIndicator(
            modifier = Modifier.size(20.dp),
            color = contentColor,
            strokeWidth = 2.dp,
        )
    } else {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier.size(ScanDocDimens.iconSize),
                )
                Spacer(modifier = Modifier.width(ScanDocDimens.spaceXs))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@Preview
@Composable
fun ScanDocButtonDarkPreview() {
    ScanDocTheme {
        Surface(color = ScanDocColors.Ink) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(ScanDocDimens.spaceMd),
                verticalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceXs),
            ) {
                ScanDocButton(
                    text = "Primary",
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    variant = ScanDocButtonVariant.Primary,
                )
                ScanDocButton(
                    text = "Secondary",
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    variant = ScanDocButtonVariant.Secondary,
                )
                ScanDocButton(
                    text = "Ghost",
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    variant = ScanDocButtonVariant.Ghost,
                )
                ScanDocButton(
                    text = "Danger",
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    variant = ScanDocButtonVariant.Danger,
                )
                ScanDocButton(
                    text = "With Icon",
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    variant = ScanDocButtonVariant.Primary,
                    leadingIcon = Icons.Default.Add,
                )
                ScanDocButton(
                    text = "Loading",
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    variant = ScanDocButtonVariant.Primary,
                    isLoading = true,
                )
            }
        }
    }
}

@Preview
@Composable
fun ScanDocButtonLightPreview() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(ScanDocDimens.spaceMd),
                verticalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceXs),
            ) {
                ScanDocButton(
                    text = "Primary",
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    variant = ScanDocButtonVariant.Primary,
                )
                ScanDocButton(
                    text = "Secondary",
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    variant = ScanDocButtonVariant.Secondary,
                )
                ScanDocButton(
                    text = "Ghost",
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    variant = ScanDocButtonVariant.Ghost,
                )
                ScanDocButton(
                    text = "Danger",
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    variant = ScanDocButtonVariant.Danger,
                )
            }
        }
    }
}
