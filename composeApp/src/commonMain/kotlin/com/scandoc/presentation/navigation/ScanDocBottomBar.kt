package com.scandoc.presentation.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.scandoc.presentation.theme.ScanDocColors
import com.scandoc.presentation.theme.ScanDocDimens

@Composable
fun ScanDocBottomBar(
    selectedTab: NavTab,
    onTabSelected: (NavTab) -> Unit,
    onCameraClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter,
    ) {
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars),
            containerColor = ScanDocColors.NavSurface,
            tonalElevation = 0.dp,
        ) {
            NavBarTab(
                tab = NavTab.Home,
                label = "Home",
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
                isSelected = selectedTab == NavTab.Home,
                onSelect = onTabSelected,
            )

            NavBarTab(
                tab = NavTab.Files,
                label = "Files",
                selectedIcon = Icons.Filled.Folder,
                unselectedIcon = Icons.Outlined.FolderOpen,
                isSelected = selectedTab == NavTab.Files,
                onSelect = onTabSelected,
            )

            // Center spacer — the Camera FAB floats above this gap
            Spacer(Modifier.weight(1f))

            NavBarTab(
                tab = NavTab.Tools,
                label = "Tools",
                selectedIcon = Icons.Filled.GridView,
                unselectedIcon = Icons.Outlined.GridView,
                isSelected = selectedTab == NavTab.Tools,
                onSelect = onTabSelected,
            )

            NavBarTab(
                tab = NavTab.Me,
                label = "Me",
                selectedIcon = Icons.Filled.Person,
                unselectedIcon = Icons.Outlined.Person,
                isSelected = selectedTab == NavTab.Me,
                onSelect = onTabSelected,
            )
        }

        // Camera FAB — centered, floating above the bar
        FloatingActionButton(
            onClick = onCameraClick,
            modifier = Modifier
                .size(ScanDocDimens.navFabSize)
                .offset(y = (-10).dp),
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = ScanDocDimens.navFabElevation,
            ),
        ) {
            Icon(
                imageVector = Icons.Filled.CameraAlt,
                contentDescription = "Scan document",
                modifier = Modifier.size(26.dp),
            )
        }
    }
}

@Composable
private fun RowScope.NavBarTab(
    tab: NavTab,
    label: String,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    isSelected: Boolean,
    onSelect: (NavTab) -> Unit,
) {
    val iconTint by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else ScanDocColors.NavIconInactive,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "tab_tint_$label",
    )

    NavigationBarItem(
        selected = isSelected,
        onClick = { onSelect(tab) },
        icon = {
            Icon(
                imageVector = if (isSelected) selectedIcon else unselectedIcon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(ScanDocDimens.iconSize),
            )
        },
        label = {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = iconTint,
            )
        },
        alwaysShowLabel = true,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            unselectedIconColor = ScanDocColors.NavIconInactive,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            unselectedTextColor = ScanDocColors.NavIconInactive,
            indicatorColor = Color.Transparent,
        ),
    )
}
