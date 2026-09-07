/**
 * Metrolist Project (C) 2026
 * Licensed under GPL-3.0 | See git history for contributors
 */

package com.metrolist.music.ui.component
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.metrolist.music.LocalPlayerConnection
import com.metrolist.music.constants.GlassBottomNavigationKey
import com.metrolist.music.constants.ShiftingBottomNavigationKey
import com.metrolist.music.ui.screens.Screens
import com.metrolist.music.utils.rememberPreference
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest












@Immutable
private data class NavItemState(
    val isSelected: Boolean,
    val iconRes: Int
)

@Stable
private fun isRouteSelected(currentRoute: String?, screenRoute: String, navigationItems: List<Screens>): Boolean {
    if (currentRoute == null) return false
    if (currentRoute == screenRoute) return true
    if (navigationItems.any { it.route == screenRoute } &&
        currentRoute.startsWith("$screenRoute/")) return true

    // Fix: match the route template, not the resolved route
    if (screenRoute == "search_input" &&
        (currentRoute.startsWith("search/") || currentRoute == "search/{query}")) return true

    return false
}

@Composable
fun AppNavigationRail(
    navigationItems: List<Screens>,
    currentRoute: String?,
    onItemClick: (Screens, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    pureBlack: Boolean = false,
    onSearchLongClick: (() -> Unit)? = null
) {
    val containerColor = if (pureBlack) Color.Black else MaterialTheme.colorScheme.surfaceContainer
    val haptics = LocalHapticFeedback.current
    val viewConfiguration = LocalViewConfiguration.current

    NavigationRail(
        modifier = modifier,
        containerColor = containerColor
    ) {
        Spacer(modifier = Modifier.weight(1f))

        navigationItems.forEach { screen ->
            val isSelected = remember(currentRoute, screen.route) {
                isRouteSelected(currentRoute, screen.route, navigationItems)
            }
            val currentIsSelected by rememberUpdatedState(isSelected)
            val iconRes = remember(isSelected, screen) {
                if (isSelected) screen.iconIdActive else screen.iconIdInactive
            }

            val isSearchItem = screen == Screens.Search && onSearchLongClick != null
            val interactionSource = remember { MutableInteractionSource() }

            // Long press detection using InteractionSource
            if (isSearchItem) {
                LaunchedEffect(interactionSource) {
                    var isLongClick = false
                    interactionSource.interactions.collectLatest { interaction ->
                        when (interaction) {
                            is PressInteraction.Press -> {
                                isLongClick = false
                                delay(viewConfiguration.longPressTimeoutMillis)
                                isLongClick = true
                                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                onSearchLongClick.invoke()
                            }
                            is PressInteraction.Release -> {
                                if (!isLongClick) {
                                    onItemClick(screen, currentIsSelected)
                                }
                            }
                            is PressInteraction.Cancel -> {
                                isLongClick = false
                            }
                        }
                    }
                }
            }

            NavigationRailItem(
                selected = isSelected,
                onClick = {
                    if (!isSearchItem) {
                        onItemClick(screen, currentIsSelected)
                    }
                    // For search item, click is handled via InteractionSource
                },
                interactionSource = interactionSource,
                icon = {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = stringResource(screen.titleId)
                    )
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun AppNavigationBar(
    navigationItems: List<Screens>,
    currentRoute: String?,
    onItemClick: (Screens, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    pureBlack: Boolean = false,
    slimNav: Boolean = false,
    onSearchLongClick: (() -> Unit)? = null
) {
    val haptics = LocalHapticFeedback.current
    val viewConfiguration = LocalViewConfiguration.current
    val density = LocalDensity.current
    
    val playerConnection = LocalPlayerConnection.current
    val mediaMetadata by playerConnection?.mediaMetadata?.collectAsState(initial = null) ?: remember { mutableStateOf(null) }
    
    val (shiftingBottomNav) = rememberPreference(ShiftingBottomNavigationKey, defaultValue = true)
    val (glassMode) = rememberPreference(GlassBottomNavigationKey, defaultValue = false)
    
    val baseContainerColor = if (pureBlack) Color.Black else MaterialTheme.colorScheme.surfaceContainer
    
    if (shiftingBottomNav) {
        val containerColor = if (glassMode) baseContainerColor.copy(alpha = 0.55f) else baseContainerColor
        val contentColor = if (pureBlack) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
        val activeBackgroundColor = MaterialTheme.colorScheme.primaryContainer
        val activeContentColor = MaterialTheme.colorScheme.onPrimaryContainer

        val bottomPadding = with(density) { WindowInsets.systemBars.getBottom(this).toDp() }

        Box(
            modifier = modifier.fillMaxWidth(), // Inherits height constraints from MainActivity
            contentAlignment = Alignment.BottomCenter
        ) {
            // Inner pill container protected from system navigation bar
            Box(
                modifier = Modifier
                    .padding(bottom = bottomPadding + 8.dp)
                    .padding(horizontal = 16.dp)
                    .height(72.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(36.dp))
                    .background(containerColor),
                contentAlignment = Alignment.Center
            ) {
                if (glassMode && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                    mediaMetadata?.thumbnailUrl?.let { url ->
                        AsyncImage(
                            model = url,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .blur(60.dp)
                        )
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.45f))
                        )
                    }
                }

                BoxWithConstraints(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    val totalWidth = maxWidth
                    val gap = 8.dp
                    val totalGaps = gap * (navigationItems.size - 1)
                    val availableWidth = totalWidth - totalGaps
                    
                    val inactiveTabWidth = 56.dp // Perfect circle 56x56
                    val activeTabWidth = availableWidth - (inactiveTabWidth * (navigationItems.size - 1))

                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.spacedBy(gap),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        navigationItems.forEach { screen ->
                            val isSelected = remember(currentRoute, screen.route) {
                                isRouteSelected(currentRoute, screen.route, navigationItems)
                            }
                            val currentIsSelected by rememberUpdatedState(isSelected)
                            val iconRes = remember(isSelected, screen) {
                                if (isSelected) screen.iconIdActive else screen.iconIdInactive
                            }

                            val isSearchItem = screen == Screens.Search && onSearchLongClick != null
                            val interactionSource = remember { MutableInteractionSource() }

                            if (isSearchItem) {
                                LaunchedEffect(interactionSource) {
                                    var isLongClick = false
                                    interactionSource.interactions.collectLatest { interaction ->
                                        when (interaction) {
                                            is PressInteraction.Press -> {
                                                isLongClick = false
                                                delay(viewConfiguration.longPressTimeoutMillis)
                                                isLongClick = true
                                                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                                onSearchLongClick.invoke()
                                            }
                                            is PressInteraction.Release -> {
                                                if (!isLongClick) {
                                                    onItemClick(screen, currentIsSelected)
                                                }
                                            }
                                            is PressInteraction.Cancel -> {
                                                isLongClick = false
                                            }
                                        }
                                    }
                                }
                            }

                            val bgColor by animateColorAsState(
                                targetValue = if (isSelected) activeBackgroundColor else Color.Transparent,
                                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
                                label = "bgColor"
                            )
                            
                            val tintColor by animateColorAsState(
                                targetValue = if (isSelected) activeContentColor else contentColor,
                                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
                                label = "tintColor"
                            )
                            
                            // Highly optimized explicit width animation (zero layout thrashing)
                            val targetWidth = if (isSelected) activeTabWidth else inactiveTabWidth
                            val tabWidth by animateDpAsState(
                                targetValue = targetWidth,
                                animationSpec = tween(300, easing = FastOutSlowInEasing),
                                label = "tabWidth"
                            )
                            
                            val textAlpha by animateFloatAsState(
                                targetValue = if (isSelected && !slimNav) 1f else 0f,
                                animationSpec = tween(300, easing = FastOutSlowInEasing),
                                label = "textAlpha"
                            )

                            Box(
                                modifier = Modifier
                                    .width(tabWidth)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(28.dp))
                                    .background(bgColor)
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null,
                                        role = Role.Tab
                                    ) {
                                        if (!isSearchItem) {
                                            onItemClick(screen, currentIsSelected)
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Icon(
                                        painter = painterResource(id = iconRes),
                                        contentDescription = stringResource(screen.titleId),
                                        tint = tintColor
                                    )
                                    
                                    // Text stays in tree to prevent jitter, width clips naturally
                                    if (!slimNav) {
                                        Text(
                                            text = stringResource(screen.titleId),
                                            color = tintColor,
                                            maxLines = 1,
                                            softWrap = false,
                                            modifier = Modifier
                                                .padding(start = 8.dp)
                                                .alpha(textAlpha),
                                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    } else {
        // Classic Standard Material 3 Navigation Bar
        val contentColor = if (pureBlack) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
        NavigationBar(
            modifier = modifier,
            containerColor = baseContainerColor,
            contentColor = contentColor
        ) {
            navigationItems.forEach { screen ->
                val isSelected = remember(currentRoute, screen.route) {
                    isRouteSelected(currentRoute, screen.route, navigationItems)
                }
                val currentIsSelected by rememberUpdatedState(isSelected)
                val iconRes = remember(isSelected, screen) {
                    if (isSelected) screen.iconIdActive else screen.iconIdInactive
                }

                val isSearchItem = screen == Screens.Search && onSearchLongClick != null
                val interactionSource = remember { MutableInteractionSource() }

                if (isSearchItem) {
                    LaunchedEffect(interactionSource) {
                        var isLongClick = false
                        interactionSource.interactions.collectLatest { interaction ->
                            when (interaction) {
                                is PressInteraction.Press -> {
                                    isLongClick = false
                                    delay(viewConfiguration.longPressTimeoutMillis)
                                    isLongClick = true
                                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onSearchLongClick.invoke()
                                }
                                is PressInteraction.Release -> {
                                    if (!isLongClick) {
                                        onItemClick(screen, currentIsSelected)
                                    }
                                }
                                is PressInteraction.Cancel -> {
                                    isLongClick = false
                                }
                            }
                        }
                    }
                }

                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        if (!isSearchItem) {
                            onItemClick(screen, currentIsSelected)
                        }
                    },
                    interactionSource = interactionSource,
                    icon = {
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = stringResource(screen.titleId)
                        )
                    },
                    label = if (!slimNav) {
                        {
                            Text(
                                text = stringResource(screen.titleId),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    } else null,
                    alwaysShowLabel = !slimNav
                )
            }
        }
    }
}
