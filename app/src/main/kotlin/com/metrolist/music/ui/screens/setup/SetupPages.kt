package com.metrolist.music.ui.screens.setup

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.alpha
import coil3.compose.AsyncImage
import androidx.navigation.NavController
import com.metrolist.music.R
import com.metrolist.music.constants.*
import com.metrolist.music.ui.component.Material3SettingsGroup
import com.metrolist.music.ui.component.Material3SettingsItem
import com.metrolist.music.ui.screens.settings.DarkMode
import com.metrolist.music.ui.screens.settings.PaletteColors
import com.metrolist.music.ui.screens.settings.PaletteItem
import com.metrolist.music.ui.theme.DefaultThemeColor
import com.metrolist.music.utils.rememberEnumPreference
import com.metrolist.music.utils.rememberPreference

@Composable
fun SetupPageBase(title: String, description: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(text = title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = description, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(24.dp))
        content()
        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun LookAndFeelPage() {
    SetupPageBase(
        title = stringResource(R.string.setup_look_and_feel),
        description = stringResource(R.string.setup_look_and_feel_desc)
    ) {
        val (darkMode, setDarkMode) = rememberEnumPreference(DarkModeKey, defaultValue = DarkMode.AUTO)
        val (pureBlack, setPureBlack) = rememberPreference(PureBlackKey, defaultValue = false)
        val (selectedThemeColorInt, onSelectedThemeColorChange) = rememberPreference(SelectedThemeColorKey, DefaultThemeColor.toArgb())
        val (_, onDynamicThemeChange) = rememberPreference(DynamicThemeKey, defaultValue = true)
        val selectedThemeColor = Color(selectedThemeColorInt)
        
        Material3SettingsGroup(
            title = stringResource(R.string.theme),
            items = buildList {
                add(Material3SettingsItem(
                    title = { Text("Dark Mode") },
                    description = { Text("Switch between light and dark themes") },
                    icon = painterResource(R.drawable.bedtime),
                    onClick = { setDarkMode(if (darkMode == DarkMode.ON) DarkMode.OFF else DarkMode.ON) },
                    trailingContent = {
                        Switch(checked = darkMode == DarkMode.ON, onCheckedChange = { setDarkMode(if (it) DarkMode.ON else DarkMode.OFF) })
                    }
                ))
                add(Material3SettingsItem(
                    title = { Text("Pure Black") },
                    description = { Text("Use pure black for dark theme backgrounds") },
                    icon = painterResource(R.drawable.contrast),
                    onClick = { setPureBlack(!pureBlack) },
                    trailingContent = {
                        Switch(checked = pureBlack, onCheckedChange = { setPureBlack(it) })
                    }
                ))
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Accent Color",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(PaletteColors) { palette ->
                val isDynamicPalette = palette.seedColor == Color.Transparent
                val isSelected = if (isDynamicPalette) {
                    selectedThemeColor == DefaultThemeColor
                } else {
                    selectedThemeColor == palette.seedColor
                }
                
                PaletteItem(
                    palette = palette,
                    isSelected = isSelected,
                    onClick = {
                        val colorToSave = if (isDynamicPalette) DefaultThemeColor else palette.seedColor
                        onSelectedThemeColorChange(colorToSave.toArgb())
                        onDynamicThemeChange(isDynamicPalette)
                    }
                )
            }
        }
    }
}

@Composable
fun ConnectLibraryPage(navController: NavController) {
    SetupPageBase(
        title = stringResource(R.string.setup_connect_library),
        description = stringResource(R.string.setup_connect_library_desc)
    ) {
        // YouTube Provider Card
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(painter = painterResource(R.drawable.account), contentDescription = null, tint = Color(0xFFFF0000), modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("YouTube Music", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text("Sync your library, playlists, and recommendations directly from Google.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { navController.navigate("login") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Sign in with Google")
                }
            }
        }

        // Spotify Provider Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(painter = painterResource(R.drawable.spotify), contentDescription = null, tint = Color(0xFF1DB954), modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Spotify", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text("Connect to seamlessly import your Spotify playlists and liked songs.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { navController.navigate("settings/spotify/login") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DB954), contentColor = Color.Black)
                ) {
                    Text("Sign in to Spotify")
                }
            }
        }
    }
}

@Composable
fun PlayerPage() {
    SetupPageBase(
        title = "Player Design",
        description = "Customize the 'Now Playing' screen."
    ) {
        val (bgStyle, setBgStyle) = rememberEnumPreference(PlayerBackgroundStyleKey, defaultValue = PlayerBackgroundStyle.DEFAULT)
        val (btnStyle, setBtnStyle) = rememberEnumPreference(PlayerButtonsStyleKey, defaultValue = PlayerButtonsStyle.DEFAULT)
        val (lyricsStyle, setLyricsStyle) = rememberEnumPreference(LyricsAnimationStyleKey, defaultValue = LyricsAnimationStyle.FADE)
        val (experimentalLyrics, setExperimentalLyrics) = rememberPreference(ExperimentalLyricsKey, defaultValue = false)
        
        // LIVE PREVIEW
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            val albumArtUrl = "https://images.unsplash.com/photo-1614613535308-eb5fbd3d2c17?auto=format&fit=crop&q=80&w=600"
            
            // Backgrounds
            if (bgStyle == PlayerBackgroundStyle.GRADIENT) {
                Box(modifier = Modifier.fillMaxSize().background(
                    androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), MaterialTheme.colorScheme.surfaceContainerHighest)
                    )
                ))
            }
            if (bgStyle == PlayerBackgroundStyle.BLUR) {
                AsyncImage(
                    model = albumArtUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().blur(24.dp).alpha(0.6f),
                    contentScale = ContentScale.Crop
                )
            }

            Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Bottom) {
                // Mock Album Art & Lyrics
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = albumArtUrl,
                        contentDescription = null,
                        modifier = Modifier.size(120.dp).clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    // Dummy Lyrics Representation
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = if (experimentalLyrics) Alignment.CenterHorizontally else Alignment.Start
                    ) {
                        val activeColor = MaterialTheme.colorScheme.onSurface
                        val inactiveColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        
                        val textStyle = if (experimentalLyrics) {
                            MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        } else {
                            MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        }
                        
                        val glowShadow = if (lyricsStyle == LyricsAnimationStyle.GLOW) {
                            androidx.compose.ui.graphics.Shadow(
                                color = MaterialTheme.colorScheme.primary,
                                blurRadius = 12f
                            )
                        } else null
                        
                        Text(
                            "This is my",
                            color = inactiveColor,
                            style = textStyle,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            "active lyric",
                            color = activeColor,
                            style = textStyle.copy(
                                shadow = glowShadow,
                                color = if (lyricsStyle == LyricsAnimationStyle.KARAOKE) MaterialTheme.colorScheme.primary else activeColor
                            )
                        )
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Mock Slider
                Slider(
                    value = 0.3f,
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth().height(24.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Mock Controls
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                    val iconColor = when (btnStyle) {
                        PlayerButtonsStyle.DEFAULT -> MaterialTheme.colorScheme.onSurfaceVariant
                        PlayerButtonsStyle.PRIMARY -> MaterialTheme.colorScheme.primary
                        PlayerButtonsStyle.TERTIARY -> MaterialTheme.colorScheme.tertiary
                    }
                    val btnBgColor = when (btnStyle) {
                        PlayerButtonsStyle.DEFAULT -> MaterialTheme.colorScheme.surfaceVariant
                        PlayerButtonsStyle.PRIMARY -> MaterialTheme.colorScheme.primary
                        PlayerButtonsStyle.TERTIARY -> MaterialTheme.colorScheme.tertiary
                    }
                    val btnIconColor = when (btnStyle) {
                        PlayerButtonsStyle.DEFAULT -> MaterialTheme.colorScheme.onSurfaceVariant
                        PlayerButtonsStyle.PRIMARY -> MaterialTheme.colorScheme.onPrimary
                        PlayerButtonsStyle.TERTIARY -> MaterialTheme.colorScheme.onTertiary
                    }
                    
                    Icon(painter = painterResource(R.drawable.skip_previous), contentDescription = null, tint = iconColor, modifier = Modifier.size(32.dp))
                    
                    Box(
                        modifier = Modifier.size(64.dp).clip(CircleShape).background(btnBgColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(painter = painterResource(R.drawable.play), contentDescription = null, tint = btnIconColor, modifier = Modifier.size(36.dp))
                    }
                    
                    Icon(painter = painterResource(R.drawable.skip_next), contentDescription = null, tint = iconColor, modifier = Modifier.size(32.dp))
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))

        Text("Player Background Style", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(
                Triple(PlayerBackgroundStyle.DEFAULT, "Solid", null),
                Triple(PlayerBackgroundStyle.GRADIENT, "Gradient", null),
                Triple(PlayerBackgroundStyle.BLUR, "Blur", null)
            ).forEach { (style, label, _) ->
                val isSelected = bgStyle == style
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface)
                        .border(1.dp, if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent, RoundedCornerShape(12.dp))
                        .clickable { setBgStyle(style) }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(label, color = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text("Button Colors", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(
                Triple(PlayerButtonsStyle.DEFAULT, "Default", null),
                Triple(PlayerButtonsStyle.PRIMARY, "Primary", null),
                Triple(PlayerButtonsStyle.TERTIARY, "Tertiary", null)
            ).forEach { (style, label, _) ->
                val isSelected = btnStyle == style
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface)
                        .border(1.dp, if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent, RoundedCornerShape(12.dp))
                        .clickable { setBtnStyle(style) }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(label, color = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text("Lyrics Style", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(
                Triple("Classic", LyricsAnimationStyle.FADE, false),
                Triple("Glow", LyricsAnimationStyle.GLOW, false),
                Triple("Apple", LyricsAnimationStyle.APPLE, false),
                Triple("Experimental", LyricsAnimationStyle.FADE, true)
            ).forEach { (label, animStyle, isExperimental) ->
                val isSelected = lyricsStyle == animStyle && experimentalLyrics == isExperimental
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface)
                        .border(1.dp, if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent, RoundedCornerShape(12.dp))
                        .clickable { 
                            setLyricsStyle(animStyle)
                            setExperimentalLyrics(isExperimental)
                        }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(label, color = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}
@Composable
fun NavigationPage() {
    SetupPageBase(
        title = "Navigation Design",
        description = "Choose how you want to navigate around the app."
    ) {
        val (shifting, setShifting) = rememberPreference(ShiftingBottomNavigationKey, defaultValue = false)
        val (glass, setGlass) = rememberPreference(GlassBottomNavigationKey, defaultValue = false)
        val (slimNav, setSlimNav) = rememberPreference(SlimNavBarKey, defaultValue = false)

        val demoBackgroundUrl = "https://images.unsplash.com/photo-1620641788415-62d74c3d35f6?auto=format&fit=crop&q=80&w=600"

        // LIVE PREVIEW
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.BottomCenter
        ) {
            // App background simulation
            AsyncImage(
                model = demoBackgroundUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            // Dimmer for background
            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)))

            // Mock Nav Bar container
            val containerHeight = if (shifting) 72.dp else if (slimNav) 64.dp else 80.dp
            
            Box(
                modifier = Modifier
                    .padding(if (shifting) 16.dp else 0.dp)
                    .padding(bottom = if (shifting) 8.dp else 0.dp)
                    .fillMaxWidth()
                    .height(containerHeight)
                    .clip(if (shifting) RoundedCornerShape(36.dp) else RoundedCornerShape(0.dp))
                    .background(
                        if (glass && shifting) MaterialTheme.colorScheme.surface.copy(alpha = 0.55f)
                        else if (glass) MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
                        else MaterialTheme.colorScheme.surface
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Mocking the real glass effect which duplicates and blurs the album art
                if (glass) {
                    AsyncImage(
                        model = demoBackgroundUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize().blur(60.dp)
                    )
                    Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.45f)))
                }

                if (shifting) {
                    // MOCK SHIFTING BOTTOM NAV
                    BoxWithConstraints(modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp, vertical = 8.dp)) {
                        val totalWidth = maxWidth
                        val gap = 8.dp
                        val inactiveTabWidth = 56.dp
                        val totalGaps = gap * 2
                        val activeTabWidth = totalWidth - totalGaps - (inactiveTabWidth * 2)
                        
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.spacedBy(gap),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Active Tab (Home)
                            Box(
                                modifier = Modifier
                                    .width(activeTabWidth)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(28.dp))
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                                    Icon(painter = painterResource(R.drawable.home_filled), contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
                                    if (!slimNav) {
                                        Text("Home", color = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.padding(start = 8.dp), style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                                    }
                                }
                            }
                            // Inactive Tab (Search)
                            Box(modifier = Modifier.width(inactiveTabWidth).fillMaxHeight(), contentAlignment = Alignment.Center) {
                                Icon(painter = painterResource(R.drawable.search), contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            // Inactive Tab (Library)
                            Box(modifier = Modifier.width(inactiveTabWidth).fillMaxHeight(), contentAlignment = Alignment.Center) {
                                Icon(painter = painterResource(R.drawable.library_music), contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                } else {
                    // MOCK STANDARD BOTTOM NAV
                    Row(
                        modifier = Modifier.fillMaxWidth().height(containerHeight),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Home
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier.size(64.dp, 32.dp).clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.secondaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(painter = painterResource(R.drawable.home_filled), contentDescription = null, tint = MaterialTheme.colorScheme.onSecondaryContainer)
                            }
                            if (!slimNav) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Home", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                        // Search
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.weight(1f)) {
                            Box(modifier = Modifier.size(64.dp, 32.dp), contentAlignment = Alignment.Center) {
                                Icon(painter = painterResource(R.drawable.search), contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            if (!slimNav) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Search", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        // Library
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.weight(1f)) {
                            Box(modifier = Modifier.size(64.dp, 32.dp), contentAlignment = Alignment.Center) {
                                Icon(painter = painterResource(R.drawable.library_music), contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            if (!slimNav) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Library", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))

        Material3SettingsGroup(
            title = "Navigation Bar Options",
            items = buildList {
                add(Material3SettingsItem(
                    title = { Text(stringResource(R.string.pref_shifting_bottom_nav_title)) },
                    description = { Text(stringResource(R.string.pref_shifting_bottom_nav_summary)) },
                    icon = painterResource(R.drawable.arrow_upward),
                    onClick = { setShifting(!shifting) },
                    trailingContent = { Switch(checked = shifting, onCheckedChange = { setShifting(it) }) }
                ))
                add(Material3SettingsItem(
                    title = { Text(stringResource(R.string.pref_glass_bottom_nav_title)) },
                    description = { Text(stringResource(R.string.pref_glass_bottom_nav_summary)) },
                    icon = painterResource(R.drawable.palette),
                    onClick = { setGlass(!glass) },
                    trailingContent = { Switch(checked = glass, onCheckedChange = { setGlass(it) }) }
                ))
                add(Material3SettingsItem(
                    title = { Text("Slim Navigation Bar") },
                    description = { Text("Reduce the height of the bottom navigation bar") },
                    icon = painterResource(R.drawable.expand_less),
                    onClick = { setSlimNav(!slimNav) },
                    trailingContent = { Switch(checked = slimNav, onCheckedChange = { setSlimNav(it) }) }
                ))
            }
        )
    }
}

@Composable
fun PlaybackAudioPage() {
    SetupPageBase(
        title = "Playback & Audio",
        description = "Fine-tune your listening experience."
    ) {
        val (audioQuality, setAudioQuality) = rememberEnumPreference(AudioQualityKey, defaultValue = AudioQuality.AUTO)
        val (audioNormalization, setAudioNormalization) = rememberPreference(AudioNormalizationKey, defaultValue = false)
        val (skipSilence, setSkipSilence) = rememberPreference(SkipSilenceKey, defaultValue = false)
        val (crossfade, setCrossfade) = rememberPreference(CrossfadeEnabledKey, defaultValue = false)

        Material3SettingsGroup(
            title = "Audio Settings",
            items = buildList {
                add(Material3SettingsItem(
                    title = { Text("Audio Quality") },
                    description = { Text("Default streaming quality") },
                    icon = painterResource(R.drawable.graphic_eq),
                    onClick = {
                        val next = when (audioQuality) {
                            AudioQuality.AUTO -> AudioQuality.HIGH
                            AudioQuality.HIGH -> AudioQuality.LOW
                            AudioQuality.LOW -> AudioQuality.AUTO
                        }
                        setAudioQuality(next)
                    },
                    trailingContent = {
                        Text(
                            text = when (audioQuality) {
                                AudioQuality.AUTO -> "Auto"
                                AudioQuality.HIGH -> "High"
                                AudioQuality.LOW -> "Low"
                            },
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                ))
                add(Material3SettingsItem(
                    title = { Text("Audio Normalization") },
                    description = { Text("Keep volume consistent across tracks") },
                    icon = painterResource(R.drawable.volume_up),
                    onClick = { setAudioNormalization(!audioNormalization) },
                    trailingContent = { Switch(checked = audioNormalization, onCheckedChange = { setAudioNormalization(it) }) }
                ))
            }
        )
        
        Material3SettingsGroup(
            title = "Playback Settings",
            items = buildList {
                add(Material3SettingsItem(
                    title = { Text("Skip Silence") },
                    description = { Text("Automatically skip silent sections in tracks") },
                    icon = painterResource(R.drawable.skip_next),
                    onClick = { setSkipSilence(!skipSilence) },
                    trailingContent = { Switch(checked = skipSilence, onCheckedChange = { setSkipSilence(it) }) }
                ))
                add(Material3SettingsItem(
                    title = { Text("Crossfade") },
                    description = { Text("Smoothly transition between tracks") },
                    icon = painterResource(R.drawable.shuffle),
                    onClick = { setCrossfade(!crossfade) },
                    trailingContent = { Switch(checked = crossfade, onCheckedChange = { setCrossfade(it) }) }
                ))
            }
        )
    }
}

@Composable
fun ContentDataPage() {
    SetupPageBase(
        title = "Content & Data",
        description = "Tailor what kind of content you want to see."
    ) {
        val (hideExplicit, setHideExplicit) = rememberPreference(HideExplicitKey, defaultValue = false)
        val (hideShorts, setHideShorts) = rememberPreference(HideYoutubeShortsKey, defaultValue = false)
        val (crashReporting, setCrashReporting) = rememberPreference(CrashReportingEnabledKey, defaultValue = false)

        Material3SettingsGroup(
            title = "Content Filters",
            items = buildList {
                add(Material3SettingsItem(
                    title = { Text("Hide Explicit Content") },
                    description = { Text("Filter out tracks marked as explicit") },
                    icon = painterResource(R.drawable.explicit),
                    onClick = { setHideExplicit(!hideExplicit) },
                    trailingContent = { Switch(checked = hideExplicit, onCheckedChange = { setHideExplicit(it) }) }
                ))
                add(Material3SettingsItem(
                    title = { Text("Hide YouTube Shorts") },
                    description = { Text("Remove short videos from feeds") },
                    icon = painterResource(R.drawable.close),
                    onClick = { setHideShorts(!hideShorts) },
                    trailingContent = { Switch(checked = hideShorts, onCheckedChange = { setHideShorts(it) }) }
                ))
            }
        )
        
        Material3SettingsGroup(
            title = "Privacy & Data",
            items = buildList {
                add(Material3SettingsItem(
                    title = { Text("Crash Reporting") },
                    description = { Text("Automatically send crash reports to help improve the app") },
                    icon = painterResource(R.drawable.bug_report),
                    onClick = { setCrashReporting(!crashReporting) },
                    trailingContent = { Switch(checked = crashReporting, onCheckedChange = { setCrashReporting(it) }) }
                ))
            }
        )
    }
}
