package com.metrolist.music.constants
import androidx.datastore.preferences.core.stringPreferencesKey
enum class AddToPlaylistPosition(val prepend: Boolean) {
    BEGINNING(true),
    END(false)
}
val AddToPlaylistPositionKey = stringPreferencesKey("add_to_playlist_position")
