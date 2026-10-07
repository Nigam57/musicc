package com.metrolist.music.constants
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey

val AudioTrackPlaybackParamsKey = booleanPreferencesKey("audio_track_playback_params")
val AutoplayKey = booleanPreferencesKey("autoplay")
val LoudnessLevelKey = stringPreferencesKey("loudness_level")
val DiscordStateTemplateKey = stringPreferencesKey("discord_state_template")
val DiscordDetailsTemplateKey = stringPreferencesKey("discord_details_template")
val DiscordButton1EnabledKey = booleanPreferencesKey("discord_button1_enabled")
val DiscordButton1LabelKey = stringPreferencesKey("discord_button1_label")
val DiscordButton2EnabledKey = booleanPreferencesKey("discord_button2_enabled")
val DiscordButton2LabelKey = stringPreferencesKey("discord_button2_label")
val DiscordUserStatusKey = stringPreferencesKey("discord_user_status")
val AutoRadioQueueKey = booleanPreferencesKey("auto_radio_queue")
val InnerTubeAuthUserKey = stringPreferencesKey("innertube_auth_user")
val EnableLandscapeScalingKey = booleanPreferencesKey("enable_landscape_scaling")
val EnableZemerKey = booleanPreferencesKey("enable_zemer")
val ShowMostStatsPlaylistsKey = booleanPreferencesKey("show_most_stats_playlists")

val AndroidAutoSearchLocalLimitKey = androidx.datastore.preferences.core.intPreferencesKey("android_auto_search_local_limit")

enum class LoudnessLevel(val targetLufs: Float) {
    NORMAL(-14f),
    LOUD(-11f),
    QUIET(-23f),
    BALANCED(-14f),
    AGGRESSIVE(-11f)
}
