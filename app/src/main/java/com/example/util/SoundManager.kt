package com.example.util

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.AudioTrack
import android.media.SoundPool
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.SoundEffectConstants
import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sin

object SoundManager {
    private var isMuted = false
    private var toneGenerator: ToneGenerator? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 45)
        } catch (_: Exception) {
            toneGenerator = null
        }
    }

    fun isMuted(): Boolean = isMuted

    fun toggleMute(): Boolean {
        isMuted = !isMuted
        return isMuted
    }

    fun setMuted(muted: Boolean) {
        isMuted = muted
    }

    /**
     * Efek suara klik tombol / seleksi umum
     */
    fun playClick(view: View? = null) {
        if (isMuted) return
        try {
            view?.playSoundEffect(SoundEffectConstants.CLICK) ?: run {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 30)
            }
        } catch (_: Exception) {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 30)
        }
    }

    /**
     * Efek suara menempelkan stiker ke lembaran (plop / stick)
     */
    fun playStick(context: Context? = null) {
        if (isMuted) return
        scope.launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 60)
            } catch (_: Exception) {}
        }
        vibrateGentle(context, 20)
    }

    /**
     * Efek suara saat menggeser stiker / elemen di atas lembaran
     */
    private var lastDragSoundTime = 0L
    fun playDragTick(context: Context? = null) {
        if (isMuted) return
        val now = System.currentTimeMillis()
        // Throttle so dragging produces a rhythmic tactile tick without audio clutter
        if (now - lastDragSoundTime > 80) {
            lastDragSoundTime = now
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP2, 18)
            } catch (_: Exception) {}
            vibrateGentle(context, 8)
        }
    }

    /**
     * Efek suara saat memutar atau mengubah ukuran stiker
     */
    fun playTransformTick() {
        if (isMuted) return
        val now = System.currentTimeMillis()
        if (now - lastDragSoundTime > 100) {
            lastDragSoundTime = now
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_CDMA_KEYPAD_VOLUME_KEY_LITE, 25)
            } catch (_: Exception) {}
        }
    }

    /**
     * Efek suara menghapus elemen
     */
    fun playDelete(context: Context? = null) {
        if (isMuted) return
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_NACK, 70)
        } catch (_: Exception) {}
        vibrateGentle(context, 35)
    }

    /**
     * Efek suara reaksi kolaborasi (hati, bintang, pelukan hangat)
     */
    fun playReaction(context: Context? = null) {
        if (isMuted) return
        scope.launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_PROMPT, 90)
            } catch (_: Exception) {}
        }
        vibrateGentle(context, 30)
    }

    /**
     * Efek suara berganti tema warna / kertas notebook
     */
    fun playPageFlip(context: Context? = null) {
        if (isMuted) return
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 40)
        } catch (_: Exception) {}
        vibrateGentle(context, 15)
    }

    private fun vibrateGentle(context: Context?, durationMs: Long) {
        if (context == null) return
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(durationMs)
            }
        } catch (_: Exception) {}
    }
}
