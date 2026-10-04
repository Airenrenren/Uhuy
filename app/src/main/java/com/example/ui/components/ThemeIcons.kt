package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.JournalTheme
import com.example.ui.theme.PlayfairFontFamily
import com.example.ui.theme.QuicksandFontFamily

/**
 * Mendapatkan ImageVector representatif untuk setiap tema jurnal tanpa emoji
 */
fun getThemeVectorIcon(theme: JournalTheme): ImageVector {
    return when (theme) {
        JournalTheme.CALM -> Icons.Outlined.Coffee
        JournalTheme.FALL -> Icons.Outlined.Park
        JournalTheme.HALLOWEEN -> Icons.Outlined.Nightlight
        JournalTheme.SNOW -> Icons.Outlined.AcUnit
        JournalTheme.SIMPLE -> Icons.Outlined.Spa
        JournalTheme.EARTH -> Icons.Outlined.Eco
        JournalTheme.PASTEL -> Icons.Outlined.LocalFlorist
        JournalTheme.NEON -> Icons.Outlined.Bolt
        JournalTheme.RANDOM -> Icons.Outlined.Palette
    }
}

/**
 * Komponen icon tema estetik menggunakan vektor Material
 */
@Composable
fun ThemeVectorIcon(
    theme: JournalTheme,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.primary
) {
    Icon(
        imageVector = getThemeVectorIcon(theme),
        contentDescription = theme.title,
        tint = tint,
        modifier = modifier
    )
}

/**
 * Badge ikon tema berlatar lembut coklat muda/aksen tema
 */
@Composable
fun ThemeBadge(
    theme: JournalTheme,
    size: Dp = 32.dp,
    iconSize: Dp = 18.dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(8.dp))
            .background(theme.accentColor.copy(alpha = 0.16f))
            .border(1.dp, theme.accentColor.copy(alpha = 0.35f), RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = getThemeVectorIcon(theme),
            contentDescription = theme.title,
            tint = theme.accentColor,
            modifier = Modifier.size(iconSize)
        )
    }
}

/**
 * Stempel Karet / Cap Pos Otentik (Tanpa Emoji).
 * Menggunakan border tipografi bergaris ganda, sudut vintage, dan font Playfair/Quicksand.
 */
@Composable
fun VintageRubberStamp(
    title: String,
    subtitle: String = "",
    color: Color,
    scale: Float = 1.0f,
    modifier: Modifier = Modifier
) {
    val cleanTitle = title.replace(Regex("[^a-zA-Z0-9 •/&-]+"), "").trim().ifBlank { "OURJOUR" }
    val cleanSubtitle = subtitle.replace(Regex("[^a-zA-Z0-9 •/&-]+"), "").trim()

    Box(
        modifier = modifier
            .padding((2 * scale).dp)
            .border(
                width = (1.8f * scale).dp,
                color = color.copy(alpha = 0.82f),
                shape = RoundedCornerShape((4 * scale).dp)
            )
            .background(color.copy(alpha = 0.08f), RoundedCornerShape((4 * scale).dp))
            .padding(horizontal = (12 * scale).dp, vertical = (7 * scale).dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = cleanTitle,
                fontFamily = PlayfairFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = (12 * scale).sp,
                letterSpacing = (1.5 * scale).sp,
                color = color.copy(alpha = 0.90f),
                textAlign = TextAlign.Center
            )
            if (cleanSubtitle.isNotBlank()) {
                Spacer(modifier = Modifier.height((2 * scale).dp))
                Text(
                    text = cleanSubtitle,
                    fontFamily = QuicksandFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = (9 * scale).sp,
                    letterSpacing = (1.2 * scale).sp,
                    color = color.copy(alpha = 0.70f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * Simbol reaksi estetis berupa lencana vektor grafis (Bukan keyboard emoji)
 */
val REACTION_KEYS = listOf(
    "HEART" to "Suka",
    "SPARKLE" to "Kilau",
    "FLOWER" to "Bunga",
    "COFFEE" to "Kopi",
    "MAIL" to "Surat"
)

fun getReactionVector(key: String): ImageVector {
    return when (key.uppercase()) {
        "HEART", "❤️" -> Icons.Outlined.Favorite
        "SPARKLE", "✨" -> Icons.Outlined.AutoAwesome
        "FLOWER", "🌸" -> Icons.Outlined.LocalFlorist
        "COFFEE", "☕" -> Icons.Outlined.Coffee
        "MAIL", "💌" -> Icons.Outlined.MailOutline
        else -> Icons.Outlined.AutoAwesome
    }
}

fun getReactionColor(key: String): Color {
    return when (key.uppercase()) {
        "HEART", "❤️" -> Color(0xFFE56B6F)
        "SPARKLE", "✨" -> Color(0xFFE09F3E)
        "FLOWER", "🌸" -> Color(0xFFB56576)
        "COFFEE", "☕" -> Color(0xFFC59B76)
        "MAIL", "💌" -> Color(0xFF8EAF9D)
        else -> Color(0xFFC59B76)
    }
}

@Composable
fun StampedReactionBadge(
    reactionKey: String,
    size: Dp = 38.dp,
    modifier: Modifier = Modifier
) {
    val vector = getReactionVector(reactionKey)
    val color = getReactionColor(reactionKey)

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.15f))
            .border(1.2.dp, color.copy(alpha = 0.45f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = vector,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(size * 0.55f)
        )
    }
}
