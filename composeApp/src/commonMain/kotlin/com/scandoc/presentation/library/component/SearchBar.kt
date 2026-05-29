package com.scandoc.presentation.library.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.scandoc.presentation.theme.ScanDocColors

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        shape = MaterialTheme.shapes.small,
        placeholder = { Text("Search text, title, or OCR") },
        leadingIcon = { Text("⌕") },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = ScanDocColors.Text.copy(alpha = 0.12f),
            focusedContainerColor = ScanDocColors.Text.copy(alpha = 0.055f),
            unfocusedContainerColor = ScanDocColors.Text.copy(alpha = 0.055f),
            cursorColor = MaterialTheme.colorScheme.primary,
        ),
    )
}
