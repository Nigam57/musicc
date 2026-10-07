package com.metrolist.music.utils
import android.content.Context
import com.metrolist.music.db.entities.Song
class DiscordRPC(val context: Context, token: String) {
    suspend fun updateSong(song: Song, currentPlaybackTimeMillis: Long, playbackSpeed: Float = 1.0f, useDetails: Boolean = false, status: String = "online", button1Text: String = "", button1Visible: Boolean = true, button1Url: String = "", button2Text: String = "", button2Visible: Boolean = true, button2Url: String = "", activityType: String = "listening", activityName: String = "") {}
    suspend fun close() {}
    companion object {
        fun resolveVariables(template: String, song: Song): String {
            return template
        }
    }
}
