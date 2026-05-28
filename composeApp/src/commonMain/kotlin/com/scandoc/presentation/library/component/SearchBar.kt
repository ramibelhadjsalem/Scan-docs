package com.scandoc.presentation.library.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import com.scandoc.presentation.theme.ScanDocColors
import com.scandoc.presentation.theme.ScanDocDimens
import com.scandoc.presentation.theme.ScanDocShapes
import com.scandoc.presentation.theme.ScanDocTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search documents…",
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyMedium,
                color = ScanDocColors.Text3,
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = ScanDocColors.Text3,
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear search",
                        tint = ScanDocColors.Text3,
                    )
                }
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = ScanDocColors.Ink2,
            unfocusedContainerColor = ScanDocColors.Ink2,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = ScanDocColors.Signal,
            focusedTextColor = ScanDocColors.Text,
            unfocusedTextColor = ScanDocColors.Text,
        ),
        shape = ScanDocShapes.Pill,
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        textStyle = MaterialTheme.typography.bodyMedium,
    )
}

@Preview
@Composable
fun SearchBarDarkPreview() {
    ScanDocTheme {
        Surface(color = ScanDocColors.Ink) {
            SearchBar(
                query = "Invoice",
                onQueryChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(ScanDocDimens.spaceMd),
            )
        }
    }
}

@Preview
@Composable
fun SearchBarLightPreview() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        Surface {
            SearchBar(
                query = "",
                onQueryChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(ScanDocDimens.spaceMd),
            )
        }
    }
}
