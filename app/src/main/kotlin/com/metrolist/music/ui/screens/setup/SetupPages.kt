package com.metrolist.music.ui.screens.setup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.metrolist.music.R
import com.metrolist.music.constants.*
import com.metrolist.music.ui.component.Material3SettingsGroup
import com.metrolist.music.ui.component.Material3SettingsItem
import com.metrolist.music.ui.screens.settings.DarkMode
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
        Text(text = title, style = MaterialTheme.typography.headlineMedium)
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
        
        Material3SettingsGroup(
            title = "Theme",
            items = buildList {
                add(Material3SettingsItem(
                    title = { Text("Dark Mode") },
                    description = { Text("Force dark mode or follow system") },
                    icon = painterResource(R.drawable.palette),
                    trailingContent = {
                        Switch(checked = darkMode == DarkMode.ON, onCheckedChange = { setDarkMode(if (it) DarkMode.ON else DarkMode.OFF) })
                    },
                    onClick = { setDarkMode(if (darkMode == DarkMode.ON) DarkMode.OFF else DarkMode.ON) }
                ))
                add(Material3SettingsItem(
                    title = { Text("Pure Black") },
                    description = { Text("AMOLED optimized pitch black") },
                    icon = painterResource(R.drawable.palette),
                    trailingContent = {
                        Switch(checked = pureBlack, onCheckedChange = { setPureBlack(it) })
                    },
                    onClick = { setPureBlack(!pureBlack) }
                ))
            }
        )
    }
}

@Composable
fun ConnectLibraryPage() {
    SetupPageBase(
        title = stringResource(R.string.setup_connect_library),
        description = stringResource(R.string.setup_connect_library_desc)
    ) {
        Material3SettingsGroup(
            title = "Integrations",
            items = buildList {
                add(Material3SettingsItem(
                    title = { Text("YouTube Music") },
                    description = { Text("Connect to your YouTube Music account") },
                    icon = painterResource(R.drawable.account),
                    onClick = { /* TODO */ }
                ))
                add(Material3SettingsItem(
                    title = { Text("Spotify") },
                    description = { Text("Connect to your Spotify account") },
                    icon = painterResource(R.drawable.account),
                    onClick = { /* TODO */ }
                ))
            }
        )
    }
}

@Composable
fun PlayerDesignPage() {
    SetupPageBase(
        title = stringResource(R.string.setup_player_design),
        description = stringResource(R.string.setup_player_design_desc)
    ) {
        val (outline, setOutline) = rememberPreference(MiniPlayerOutlineKey, defaultValue = true)
        
        Material3SettingsGroup(
            title = "Mini Player",
            items = buildList {
                add(Material3SettingsItem(
                    title = { Text("Mini Player Outline") },
                    description = { Text("Show outline border on mini player") },
                    icon = painterResource(R.drawable.settings),
                    trailingContent = {
                        Switch(checked = outline, onCheckedChange = { setOutline(it) })
                    },
                    onClick = { setOutline(!outline) }
                ))
            }
        )
    }
}

@Composable
fun BottomNavPage() {
    SetupPageBase(
        title = stringResource(R.string.setup_bottom_nav),
        description = stringResource(R.string.setup_bottom_nav_desc)
    ) {
        val (shifting, setShifting) = rememberPreference(ShiftingBottomNavigationKey, defaultValue = false)
        val (glass, setGlass) = rememberPreference(GlassBottomNavigationKey, defaultValue = false)
        
        Material3SettingsGroup(
            title = "Navigation Style",
            items = buildList {
                add(Material3SettingsItem(
                    title = { Text(stringResource(R.string.pref_shifting_bottom_nav_title)) },
                    description = { Text(stringResource(R.string.pref_shifting_bottom_nav_summary)) },
                    icon = painterResource(R.drawable.settings),
                    trailingContent = {
                        Switch(checked = shifting, onCheckedChange = { setShifting(it) })
                    },
                    onClick = { setShifting(!shifting) }
                ))
                add(Material3SettingsItem(
                    title = { Text(stringResource(R.string.pref_glass_bottom_nav_title)) },
                    description = { Text(stringResource(R.string.pref_glass_bottom_nav_summary)) },
                    icon = painterResource(R.drawable.palette),
                    trailingContent = {
                        Switch(checked = glass, onCheckedChange = { setGlass(it) })
                    },
                    onClick = { setGlass(!glass) }
                ))
            }
        )
    }
}

@Composable
fun AudioPlaybackPage() {
    SetupPageBase(
        title = stringResource(R.string.setup_audio_playback),
        description = stringResource(R.string.setup_audio_playback_desc)
    ) {
        val (normalization, setNormalization) = rememberPreference(AudioNormalizationKey, defaultValue = false)
        val (skipSilence, setSkipSilence) = rememberPreference(SkipSilenceKey, defaultValue = false)
        
        Material3SettingsGroup(
            title = "Audio Engine",
            items = buildList {
                add(Material3SettingsItem(
                    title = { Text(stringResource(R.string.audio_normalization)) },
                    description = { Text("Balance track volumes to a consistent level") },
                    icon = painterResource(R.drawable.settings),
                    trailingContent = {
                        Switch(checked = normalization, onCheckedChange = { setNormalization(it) })
                    },
                    onClick = { setNormalization(!normalization) }
                ))
                add(Material3SettingsItem(
                    title = { Text(stringResource(R.string.skip_silence)) },
                    description = { Text(stringResource(R.string.skip_silence_desc)) },
                    icon = painterResource(R.drawable.settings),
                    trailingContent = {
                        Switch(checked = skipSilence, onCheckedChange = { setSkipSilence(it) })
                    },
                    onClick = { setSkipSilence(!skipSilence) }
                ))
            }
        )
    }
}

@Composable
fun ContentDataPage() {
    SetupPageBase(
        title = stringResource(R.string.setup_content_data),
        description = stringResource(R.string.setup_content_data_desc)
    ) {
        val (sponsorBlock, setSponsorBlock) = rememberPreference(SponsorBlockEnabledKey, defaultValue = true)
        
        Material3SettingsGroup(
            title = "Data & Content",
            items = buildList {
                add(Material3SettingsItem(
                    title = { Text(stringResource(R.string.sponsorblock)) },
                    description = { Text(stringResource(R.string.sponsorblock_desc)) },
                    icon = painterResource(R.drawable.settings),
                    trailingContent = {
                        Switch(checked = sponsorBlock, onCheckedChange = { setSponsorBlock(it) })
                    },
                    onClick = { setSponsorBlock(!sponsorBlock) }
                ))
            }
        )
    }
}
