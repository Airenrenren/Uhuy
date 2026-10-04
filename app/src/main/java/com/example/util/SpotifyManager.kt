package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

data class SpotifyJournalPlaylist(
    val id: String,
    val title: String,
    val description: String,
    val iconType: String,
    val iconEmoji: String = "",
    val spotifyUri: String,
    val webUrl: String
)

object SpotifyManager {
    val JOURNAL_PLAYLISTS: List<SpotifyJournalPlaylist> = listOf(
        SpotifyJournalPlaylist(
            id = "lofi_journal",
            title = "Lofi Cozy Journaling",
            description = "Beat lembut santai mengiringi jemari menghias",
            iconType = "headphones",
            spotifyUri = "spotify:playlist:37i9dQZF1DXdLEN7aqioXM",
            webUrl = "https://open.spotify.com/playlist/37i9dQZF1DXdLEN7aqioXM"
        ),
        SpotifyJournalPlaylist(
            id = "peaceful_piano",
            title = "Peaceful Piano Reflections",
            description = "Alunan piano syahdu menenangkan pikiran",
            iconType = "piano",
            spotifyUri = "spotify:playlist:37i9dQZF1DX4sWSpwq3LiO",
            webUrl = "https://open.spotify.com/playlist/37i9dQZF1DX4sWSpwq3LiO"
        ),
        SpotifyJournalPlaylist(
            id = "coffeehouse_chill",
            title = "Coffeehouse Acoustic Chill",
            description = "Petikan gitar akustik hangat suasana kafe",
            iconType = "coffee",
            spotifyUri = "spotify:playlist:37i9dQZF1DX6ziVCJnEm59",
            webUrl = "https://open.spotify.com/playlist/37i9dQZF1DX6ziVCJnEm59"
        ),
        SpotifyJournalPlaylist(
            id = "autumn_jazz",
            title = "Autumn Rainy Jazz",
            description = "Irama jazz klasik cocok untuk suasana malam & senja",
            iconType = "autumn_leaf",
            spotifyUri = "spotify:playlist:37i9dQZF1DXbITWG1ZJKYt",
            webUrl = "https://open.spotify.com/playlist/37i9dQZF1DXbITWG1ZJKYt"
        ),
        SpotifyJournalPlaylist(
            id = "deep_focus",
            title = "Deep Focus Calming Ambience",
            description = "Suara latar lembut tanpa lirik agar menjurnal lebih rileks",
            iconType = "lamp",
            spotifyUri = "spotify:playlist:37i9dQZF1DWZeKCadgRdKQ",
            webUrl = "https://open.spotify.com/playlist/37i9dQZF1DWZeKCadgRdKQ"
        )
    )

    /**
     * Membuka langsung ke aplikasi Spotify resmi untuk memutar lagu secara PENUH (Full Track)
     * tanpa batas pratinjau 30 detik.
     */
    fun openSpotify(context: Context, playlist: SpotifyJournalPlaylist) {
        openUriOrWeb(context, playlist.spotifyUri, playlist.webUrl)
    }

    /**
     * Membuka lagu atau playlist kustom pengguna di Spotify secara penuh
     */
    fun openCustomSpotify(context: Context, customInput: String) {
        val trimmed = customInput.trim()
        val uri = if (trimmed.startsWith("spotify:")) {
            trimmed
        } else if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
            trimmed
        } else {
            "spotify:search:$trimmed"
        }

        val webFallback = if (trimmed.startsWith("http")) trimmed
        else "https://open.spotify.com/search/${Uri.encode(trimmed)}"

        openUriOrWeb(context, uri, webFallback)
    }

    private fun openUriOrWeb(context: Context, uriString: String, webUrl: String) {
        // 1. Coba luncurkan aplikasi Spotify resmi secara langsung dengan package target
        try {
            val spotifyAppIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uriString)).apply {
                setPackage("com.spotify.music")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(spotifyAppIntent)
            Toast.makeText(context, "Membuka Spotify (Putar Lagu Lengkap)...", Toast.LENGTH_SHORT).show()
            return
        } catch (_: Exception) {}

        // 2. Jika intent khusus package gagal, coba intent URI umum
        try {
            val generalIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uriString)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(generalIntent)
            Toast.makeText(context, "Membuka Spotify...", Toast.LENGTH_SHORT).show()
            return
        } catch (_: Exception) {}

        // 3. Fallback ke Spotify Web Player browser agar pengguna bisa login dan mendengar lagu full
        try {
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(webUrl)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(webIntent)
            Toast.makeText(context, "Membuka Spotify Web Player...", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Tidak dapat membuka Spotify: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }
}
