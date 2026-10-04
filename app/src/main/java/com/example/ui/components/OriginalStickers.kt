package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PlayfairFontFamily
import com.example.ui.theme.QuicksandFontFamily
import kotlin.math.cos
import kotlin.math.sin

/**
 * Sistem rendering stiker grafis original OurJour (bukan emoji).
 * Mendukung stiker bawaan katalog dan stiker kustom buatan pengguna.
 */
@Composable
fun OriginalStickerView(
    stickerKey: String,
    primaryColor: Color,
    scale: Float = 1.0f,
    modifier: Modifier = Modifier
) {
    val baseSize = (58 * scale).dp

    Box(
        modifier = modifier
            .size(baseSize)
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        if (stickerKey.startsWith("custom_sticker:") || stickerKey.startsWith("custom:")) {
            RenderCustomSticker(stickerKey, primaryColor)
        } else {
            when (stickerKey) {
                // Kafe & Santai
                "coffee_cup" -> CoffeeCupIllustration(primaryColor)
                "teapot_vintage" -> TeapotIllustration(primaryColor)
                "journal_book" -> JournalBookIllustration(primaryColor)
                "croissant" -> CroissantIllustration(primaryColor)

                // Botani & Alam
                "botanical_sprig" -> BotanicalSprigIllustration(primaryColor)
                "monstera_leaf" -> MonsteraLeafIllustration(primaryColor)
                "ginkgo_leaf" -> GinkgoLeafIllustration(primaryColor)
                "daisy_flower" -> DaisyFlowerIllustration(primaryColor)
                "lavender_sprig" -> LavenderSprigIllustration(primaryColor)

                // Vintage & Ephemera
                "wax_seal" -> WaxSealIllustration(primaryColor)
                "postage_stamp" -> PostageStampIllustration(primaryColor)
                "polaroid_photo" -> PolaroidCardIllustration(primaryColor)
                "cassette_tape" -> CassetteTapeIllustration(primaryColor)
                "paperclip" -> PaperclipIllustration(primaryColor)
                "ticket_stub" -> TicketStubIllustration(primaryColor)

                // Musim (Halloween, Fall, Snow)
                "ghost" -> CuteGhostIllustration(primaryColor)
                "pumpkin" -> CarvedPumpkinIllustration(primaryColor)
                "black_cat" -> BlackCatIllustration(primaryColor)
                "maple_leaf" -> MapleLeafIllustration(primaryColor)
                "acorn" -> AcornIllustration(primaryColor)
                "snowflake" -> SnowflakeIllustration(primaryColor)
                "snowman" -> SnowmanIllustration(primaryColor)

                // Manis & Neon
                "ribbon_bow" -> RibbonBowIllustration(primaryColor)
                "fluffy_cloud" -> FluffyCloudIllustration(primaryColor)
                "strawberry" -> StrawberryIllustration(primaryColor)
                "butterfly" -> ButterflyIllustration(primaryColor)
                "neon_bolt" -> NeonBoltIllustration(primaryColor)
                "sparkle_cluster" -> SparkleClusterIllustration(primaryColor)

                else -> FallbackIllustration(stickerKey, primaryColor)
            }
        }
    }
}

/**
 * Render Stiker Kustom buatan pengguna
 * Format: "custom:SHAPE:SYMBOL:TEXT:BG_HEX:ACCENT_HEX"
 */
@Composable
fun RenderCustomSticker(data: String, defaultColor: Color) {
    val parts = data.removePrefix("custom_sticker:").removePrefix("custom:").split(":")
    val shape = parts.getOrNull(0) ?: "CIRCLE"
    val symbol = parts.getOrNull(1) ?: "COFFEE"
    val text = parts.getOrNull(2) ?: ""
    val bgHex = parts.getOrNull(3)?.toLongOrNull() ?: defaultColor.value.toLong()
    val accentHex = parts.getOrNull(4)?.toLongOrNull() ?: 0xFFFFFFFF

    val bgColor = Color(bgHex)
    val accentColor = Color(accentHex)

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // 1. Gambar Bentuk Lencana (Shape)
        when (shape) {
            "STAMP" -> {
                // Perangko pos bergerigi
                drawRoundRect(
                    color = bgColor,
                    topLeft = Offset(w * 0.08f, h * 0.08f),
                    size = Size(w * 0.84f, h * 0.84f),
                    cornerRadius = CornerRadius(w * 0.04f, h * 0.04f)
                )
                // Gigi-gigi tepi
                val toothRadius = w * 0.04f
                val countX = 5
                for (i in 0..countX) {
                    val x = (w * 0.84f / countX) * i + w * 0.08f
                    drawCircle(Color.Transparent, radius = toothRadius, center = Offset(x, h * 0.08f), blendMode = BlendMode.Clear)
                    drawCircle(Color.Transparent, radius = toothRadius, center = Offset(x, h * 0.92f), blendMode = BlendMode.Clear)
                }
            }
            "WAX_SEAL" -> {
                // Segel lilin meleleh organik
                val seal = Path().apply {
                    moveTo(w * 0.20f, h * 0.35f)
                    cubicTo(w * 0.12f, h * 0.60f, w * 0.30f, h * 0.88f, w * 0.50f, h * 0.88f)
                    cubicTo(w * 0.70f, h * 0.90f, w * 0.88f, h * 0.72f, w * 0.84f, h * 0.48f)
                    cubicTo(w * 0.86f, h * 0.24f, w * 0.68f, h * 0.12f, w * 0.48f, h * 0.14f)
                    cubicTo(w * 0.30f, h * 0.12f, w * 0.18f, h * 0.24f, w * 0.20f, h * 0.35f)
                    close()
                }
                drawPath(seal, color = bgColor)
                drawCircle(accentColor.copy(alpha = 0.4f), radius = w * 0.25f, center = Offset(w * 0.5f, h * 0.5f), style = Stroke(width = w * 0.04f))
            }
            "HEXAGON" -> {
                val hex = Path().apply {
                    moveTo(w * 0.50f, h * 0.08f)
                    lineTo(w * 0.88f, h * 0.28f)
                    lineTo(w * 0.88f, h * 0.72f)
                    lineTo(w * 0.50f, h * 0.92f)
                    lineTo(w * 0.12f, h * 0.72f)
                    lineTo(w * 0.12f, h * 0.28f)
                    close()
                }
                drawPath(hex, color = bgColor)
                drawPath(hex, color = accentColor.copy(alpha = 0.5f), style = Stroke(width = w * 0.035f))
            }
            "ARCH" -> {
                val arch = Path().apply {
                    moveTo(w * 0.20f, h * 0.88f)
                    lineTo(w * 0.20f, h * 0.45f)
                    cubicTo(w * 0.20f, h * 0.12f, w * 0.80f, h * 0.12f, w * 0.80f, h * 0.45f)
                    lineTo(w * 0.80f, h * 0.88f)
                    close()
                }
                drawPath(arch, color = bgColor)
                drawPath(arch, color = accentColor.copy(alpha = 0.5f), style = Stroke(width = w * 0.035f))
            }
            else -> { // CIRCLE / BADGE
                drawCircle(color = bgColor, radius = w * 0.42f, center = Offset(w * 0.5f, h * 0.5f))
                drawCircle(color = accentColor.copy(alpha = 0.5f), radius = w * 0.36f, center = Offset(w * 0.5f, h * 0.5f), style = Stroke(width = w * 0.03f))
            }
        }
    }

    // Teks monogram / nama jika ada
    if (text.isNotBlank()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Text(
                text = text,
                fontFamily = QuicksandFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 9.sp,
                color = accentColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 6.dp)
            )
        }
    }
}

// --- Ilustrasi Original Vektor (Botani, Kafe, Vintage, Musim) ---

// 1. Cangkir Kopi Keramik & Uap Hangat
@Composable
fun CoffeeCupIllustration(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        val cupPath = Path().apply {
            moveTo(w * 0.22f, h * 0.38f)
            lineTo(w * 0.72f, h * 0.38f)
            cubicTo(w * 0.70f, h * 0.78f, w * 0.65f, h * 0.88f, w * 0.47f, h * 0.88f)
            cubicTo(w * 0.29f, h * 0.88f, w * 0.24f, h * 0.78f, w * 0.22f, h * 0.38f)
            close()
        }
        drawPath(cupPath, color = color)

        val handlePath = Path().apply {
            moveTo(w * 0.70f, h * 0.44f)
            cubicTo(w * 0.90f, h * 0.44f, w * 0.90f, h * 0.72f, w * 0.66f, h * 0.74f)
        }
        drawPath(handlePath, color = color, style = Stroke(width = w * 0.08f, cap = StrokeCap.Round))

        drawRoundRect(
            color = color.copy(alpha = 0.85f),
            topLeft = Offset(w * 0.16f, h * 0.88f),
            size = Size(w * 0.62f, h * 0.08f),
            cornerRadius = CornerRadius(w * 0.04f, h * 0.04f)
        )

        val steamPath1 = Path().apply {
            moveTo(w * 0.38f, h * 0.32f)
            cubicTo(w * 0.34f, h * 0.24f, w * 0.42f, h * 0.18f, w * 0.38f, h * 0.10f)
        }
        val steamPath2 = Path().apply {
            moveTo(w * 0.54f, h * 0.30f)
            cubicTo(w * 0.58f, h * 0.22f, w * 0.50f, h * 0.16f, w * 0.56f, h * 0.08f)
        }
        drawPath(steamPath1, color = color.copy(alpha = 0.6f), style = Stroke(width = w * 0.04f, cap = StrokeCap.Round))
        drawPath(steamPath2, color = color.copy(alpha = 0.6f), style = Stroke(width = w * 0.04f, cap = StrokeCap.Round))

        drawCircle(color = Color.White.copy(alpha = 0.8f), radius = w * 0.06f, center = Offset(w * 0.44f, h * 0.60f))
        drawCircle(color = Color.White.copy(alpha = 0.8f), radius = w * 0.06f, center = Offset(w * 0.50f, h * 0.60f))
    }
}

// 2. Teko Teh Porselen Klasik
@Composable
fun TeapotIllustration(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val potColor = if (color == Color.Unspecified) Color(0xFFC59B76) else color

        // Badan teko bulat
        drawCircle(potColor, radius = w * 0.28f, center = Offset(w * 0.50f, h * 0.56f))

        // Tutup teko
        drawRoundRect(potColor, topLeft = Offset(w * 0.38f, h * 0.26f), size = Size(w * 0.24f, h * 0.08f), cornerRadius = CornerRadius(4f, 4f))
        drawCircle(Color.White.copy(alpha = 0.8f), radius = w * 0.04f, center = Offset(w * 0.50f, h * 0.24f))

        // Corong teko melengkung
        val spout = Path().apply {
            moveTo(w * 0.28f, h * 0.58f)
            cubicTo(w * 0.14f, h * 0.52f, w * 0.10f, h * 0.40f, w * 0.12f, h * 0.36f)
            lineTo(w * 0.16f, h * 0.38f)
            cubicTo(w * 0.16f, h * 0.46f, w * 0.26f, h * 0.64f, w * 0.32f, h * 0.66f)
            close()
        }
        drawPath(spout, color = potColor)

        // Gagang teko
        val handle = Path().apply {
            moveTo(w * 0.72f, h * 0.46f)
            cubicTo(w * 0.90f, h * 0.46f, w * 0.90f, h * 0.68f, w * 0.72f, h * 0.68f)
        }
        drawPath(handle, color = potColor, style = Stroke(width = w * 0.07f, cap = StrokeCap.Round))
    }
}

// 3. Buku Jurnal Kulit dengan Pita
@Composable
fun JournalBookIllustration(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val bookColor = if (color == Color.Unspecified) Color(0xFF8D5B4C) else color

        // Sampul buku bersudut halus
        drawRoundRect(
            color = bookColor,
            topLeft = Offset(w * 0.18f, h * 0.16f),
            size = Size(w * 0.64f, h * 0.70f),
            cornerRadius = CornerRadius(w * 0.05f, h * 0.05f)
        )
        // Halaman samping
        drawRoundRect(
            color = Color(0xFFFAF6EE),
            topLeft = Offset(w * 0.26f, h * 0.20f),
            size = Size(w * 0.52f, h * 0.62f),
            cornerRadius = CornerRadius(w * 0.03f, h * 0.03f)
        )
        // Pita pembatas buku merah menjuntai
        val ribbon = Path().apply {
            moveTo(w * 0.48f, h * 0.20f)
            lineTo(w * 0.52f, h * 0.20f)
            lineTo(w * 0.52f, h * 0.92f)
            lineTo(w * 0.50f, h * 0.88f)
            lineTo(w * 0.48f, h * 0.92f)
            close()
        }
        drawPath(ribbon, color = Color(0xFFBC4749))
    }
}

// 4. Kue Croissant Hangat
@Composable
fun CroissantIllustration(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val crustColor = Color(0xFFDF9F52)

        // Bentuk bulan sabit croissant berlapis
        val crescent = Path().apply {
            moveTo(w * 0.16f, h * 0.68f)
            cubicTo(w * 0.24f, h * 0.24f, w * 0.76f, h * 0.24f, w * 0.84f, h * 0.68f)
            cubicTo(w * 0.72f, h * 0.58f, w * 0.60f, h * 0.54f, w * 0.50f, h * 0.54f)
            cubicTo(w * 0.40f, h * 0.54f, w * 0.28f, h * 0.58f, w * 0.16f, h * 0.68f)
            close()
        }
        drawPath(crescent, color = crustColor)

        // Garis lekukan pastry
        val line1 = Path().apply {
            moveTo(w * 0.38f, h * 0.34f)
            quadraticTo(w * 0.40f, h * 0.50f, w * 0.32f, h * 0.62f)
        }
        val line2 = Path().apply {
            moveTo(w * 0.62f, h * 0.34f)
            quadraticTo(w * 0.60f, h * 0.50f, w * 0.68f, h * 0.62f)
        }
        drawPath(line1, color = Color(0xFFB5702A), style = Stroke(w * 0.035f, cap = StrokeCap.Round))
        drawPath(line2, color = Color(0xFFB5702A), style = Stroke(w * 0.035f, cap = StrokeCap.Round))
    }
}

// 5. Daun Monstera Estetik
@Composable
fun MonsteraLeafIllustration(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val leafColor = if (color == Color.Unspecified) Color(0xFF4A7C59) else color

        // Daun hati lebar
        val leaf = Path().apply {
            moveTo(w * 0.50f, h * 0.14f)
            cubicTo(w * 0.86f, h * 0.28f, w * 0.84f, h * 0.72f, w * 0.50f, h * 0.84f)
            cubicTo(w * 0.16f, h * 0.72f, w * 0.14f, h * 0.28f, w * 0.50f, h * 0.14f)
            close()
        }
        drawPath(leaf, color = leafColor)

        // Tulang daun tengah
        drawLine(
            color = Color.White.copy(alpha = 0.5f),
            start = Offset(w * 0.50f, h * 0.84f),
            end = Offset(w * 0.50f, h * 0.22f),
            strokeWidth = w * 0.035f,
            cap = StrokeCap.Round
        )

        // Lubang khas monstera
        drawOval(Color(0xFFFAF7F2), topLeft = Offset(w * 0.30f, h * 0.40f), size = Size(w * 0.12f, h * 0.06f))
        drawOval(Color(0xFFFAF7F2), topLeft = Offset(w * 0.58f, h * 0.44f), size = Size(w * 0.14f, h * 0.06f))
        drawOval(Color(0xFFFAF7F2), topLeft = Offset(w * 0.32f, h * 0.56f), size = Size(w * 0.12f, h * 0.05f))
    }
}

// 6. Daun Ginkgo Kipas Emas
@Composable
fun GinkgoLeafIllustration(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val ginkgoColor = Color(0xFFD4A373)

        // Daun bentuk kipas berbelah tengah
        val fan = Path().apply {
            moveTo(w * 0.50f, h * 0.84f) // Pangkal batang
            cubicTo(w * 0.26f, h * 0.60f, w * 0.12f, h * 0.36f, w * 0.28f, h * 0.22f)
            cubicTo(w * 0.38f, h * 0.16f, w * 0.46f, h * 0.24f, w * 0.50f, h * 0.32f) // Belahan tengah
            cubicTo(w * 0.54f, h * 0.24f, w * 0.62f, h * 0.16f, w * 0.72f, h * 0.22f)
            cubicTo(w * 0.88f, h * 0.36f, w * 0.74f, h * 0.60f, w * 0.50f, h * 0.84f)
            close()
        }
        drawPath(fan, color = ginkgoColor)

        // Tangkai daun
        drawLine(
            color = Color(0xFF8D5B4C),
            start = Offset(w * 0.50f, h * 0.84f),
            end = Offset(w * 0.50f, h * 0.94f),
            strokeWidth = w * 0.04f,
            cap = StrokeCap.Round
        )
    }
}

// 7. Bunga Lavender Lembut
@Composable
fun LavenderSprigIllustration(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val flowerColor = Color(0xFFB5A6C9)
        val stemColor = Color(0xFF6B8E7B)

        // Tangkai hijau
        drawLine(stemColor, Offset(w * 0.50f, h * 0.90f), Offset(w * 0.50f, h * 0.20f), strokeWidth = w * 0.04f, cap = StrokeCap.Round)

        // Kuncup-kuncup ungu lavender bersusun
        val heights = listOf(0.20f, 0.28f, 0.36f, 0.44f, 0.52f, 0.60f)
        heights.forEachIndexed { idx, yRel ->
            val y = h * yRel
            val sz = w * (0.16f - idx * 0.012f)
            drawOval(flowerColor, topLeft = Offset(w * 0.50f - sz * 1.1f, y), size = Size(sz, sz * 0.7f))
            drawOval(flowerColor, topLeft = Offset(w * 0.50f + sz * 0.1f, y), size = Size(sz, sz * 0.7f))
            drawOval(flowerColor.copy(alpha = 0.9f), topLeft = Offset(w * 0.50f - sz * 0.5f, y - sz * 0.2f), size = Size(sz, sz * 0.6f))
        }
    }
}

// 8. Perangko Pos Antik (Postage Stamp)
@Composable
fun PostageStampIllustration(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val stampBg = Color(0xFFFBF8F2)
        val frameColor = if (color == Color.Unspecified) Color(0xFFC59B76) else color

        // Badan perangko
        drawRoundRect(
            color = stampBg,
            topLeft = Offset(w * 0.12f, h * 0.12f),
            size = Size(w * 0.76f, h * 0.76f),
            cornerRadius = CornerRadius(w * 0.02f, h * 0.02f)
        )
        // Garis batas pos dalam
        drawRect(
            color = frameColor.copy(alpha = 0.7f),
            topLeft = Offset(w * 0.20f, h * 0.20f),
            size = Size(w * 0.60f, h * 0.60f),
            style = Stroke(width = w * 0.03f)
        )

        // Lukisan mini dalam perangko (matahari & bukit)
        drawCircle(Color(0xFFE87524), radius = w * 0.08f, center = Offset(w * 0.50f, h * 0.42f))
        val hill = Path().apply {
            moveTo(w * 0.22f, h * 0.78f)
            quadraticTo(w * 0.50f, h * 0.54f, w * 0.78f, h * 0.78f)
            close()
        }
        drawPath(hill, color = Color(0xFF6B8E7B))
    }
}

// 9. Tiket Vintage Berlubang (Ticket Stub)
@Composable
fun TicketStubIllustration(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val ticketColor = Color(0xFFF2EAD9)

        drawRoundRect(
            color = ticketColor,
            topLeft = Offset(w * 0.10f, h * 0.24f),
            size = Size(w * 0.80f, h * 0.52f),
            cornerRadius = CornerRadius(w * 0.04f, h * 0.04f)
        )
        // Garis sobekan putus-putus
        drawLine(
            color = Color(0xFF9E8F7A),
            start = Offset(w * 0.66f, h * 0.26f),
            end = Offset(w * 0.66f, h * 0.74f),
            strokeWidth = 2f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
        )
        // Bintang stempel tiket
        drawCircle(Color(0xFF8D5B4C), radius = w * 0.06f, center = Offset(w * 0.38f, h * 0.50f))
    }
}

// 10. Hantu Ramah (Ghost)
@Composable
fun CuteGhostIllustration(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        val ghostPath = Path().apply {
            moveTo(w * 0.24f, h * 0.48f)
            cubicTo(w * 0.24f, h * 0.16f, w * 0.76f, h * 0.16f, w * 0.76f, h * 0.48f)
            cubicTo(w * 0.78f, h * 0.70f, w * 0.82f, h * 0.86f, w * 0.74f, h * 0.88f)
            cubicTo(w * 0.66f, h * 0.80f, w * 0.60f, h * 0.90f, w * 0.50f, h * 0.85f)
            cubicTo(w * 0.40f, h * 0.90f, w * 0.34f, h * 0.80f, w * 0.26f, h * 0.88f)
            cubicTo(w * 0.18f, h * 0.86f, w * 0.22f, h * 0.70f, w * 0.24f, h * 0.48f)
            close()
        }
        drawPath(ghostPath, color = Color(0xFFFBF8F5))
        drawPath(ghostPath, color = color.copy(alpha = 0.6f), style = Stroke(width = w * 0.04f))

        drawCircle(color = Color(0xFF2B2825), radius = w * 0.045f, center = Offset(w * 0.40f, h * 0.44f))
        drawCircle(color = Color(0xFF2B2825), radius = w * 0.045f, center = Offset(w * 0.60f, h * 0.44f))
        drawCircle(color = Color.White, radius = w * 0.015f, center = Offset(w * 0.39f, h * 0.43f))
        drawCircle(color = Color.White, radius = w * 0.015f, center = Offset(w * 0.59f, h * 0.43f))

        drawCircle(color = Color(0xFFFFB7B2).copy(alpha = 0.75f), radius = w * 0.055f, center = Offset(w * 0.32f, h * 0.52f))
        drawCircle(color = Color(0xFFFFB7B2).copy(alpha = 0.75f), radius = w * 0.055f, center = Offset(w * 0.68f, h * 0.52f))
    }
}

// 11. Labu Ukir Halloween
@Composable
fun CarvedPumpkinIllustration(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val pumpkinColor = Color(0xFFE87524)
        val stemColor = Color(0xFF5A7D52)

        val stemPath = Path().apply {
            moveTo(w * 0.48f, h * 0.32f)
            cubicTo(w * 0.46f, h * 0.16f, w * 0.58f, h * 0.14f, w * 0.62f, h * 0.12f)
            lineTo(w * 0.56f, h * 0.28f)
            close()
        }
        drawPath(stemPath, color = stemColor)

        drawOval(pumpkinColor.copy(alpha = 0.9f), topLeft = Offset(w * 0.14f, h * 0.30f), size = Size(w * 0.34f, h * 0.56f))
        drawOval(pumpkinColor.copy(alpha = 0.9f), topLeft = Offset(w * 0.52f, h * 0.30f), size = Size(w * 0.34f, h * 0.56f))
        drawOval(pumpkinColor, topLeft = Offset(w * 0.28f, h * 0.28f), size = Size(w * 0.44f, h * 0.60f))

        val eyeLeft = Path().apply {
            moveTo(w * 0.36f, h * 0.50f)
            lineTo(w * 0.44f, h * 0.50f)
            lineTo(w * 0.40f, h * 0.42f)
            close()
        }
        val eyeRight = Path().apply {
            moveTo(w * 0.56f, h * 0.50f)
            lineTo(w * 0.64f, h * 0.50f)
            lineTo(w * 0.60f, h * 0.42f)
            close()
        }
        val mouth = Path().apply {
            moveTo(w * 0.38f, h * 0.64f)
            quadraticTo(w * 0.50f, h * 0.74f, w * 0.62f, h * 0.64f)
            lineTo(w * 0.58f, h * 0.68f)
            lineTo(w * 0.54f, h * 0.64f)
            lineTo(w * 0.48f, h * 0.68f)
            lineTo(w * 0.44f, h * 0.64f)
            close()
        }

        val glowYellow = Color(0xFFFFDE59)
        drawPath(eyeLeft, color = glowYellow)
        drawPath(eyeRight, color = glowYellow)
        drawPath(mouth, color = glowYellow)
    }
}

// 12. Daun Maple
@Composable
fun MapleLeafIllustration(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val leafColor = if (color == Color.Unspecified) Color(0xFFC35A38) else color

        val maplePath = Path().apply {
            moveTo(w * 0.50f, h * 0.12f)
            lineTo(w * 0.56f, h * 0.26f)
            lineTo(w * 0.68f, h * 0.22f)
            lineTo(w * 0.62f, h * 0.36f)
            lineTo(w * 0.80f, h * 0.42f)
            lineTo(w * 0.68f, h * 0.54f)
            lineTo(w * 0.72f, h * 0.66f)
            lineTo(w * 0.56f, h * 0.64f)
            lineTo(w * 0.52f, h * 0.84f)
            lineTo(w * 0.48f, h * 0.84f)
            lineTo(w * 0.44f, h * 0.64f)
            lineTo(w * 0.28f, h * 0.66f)
            lineTo(w * 0.32f, h * 0.54f)
            lineTo(w * 0.20f, h * 0.42f)
            lineTo(w * 0.38f, h * 0.36f)
            lineTo(w * 0.32f, h * 0.22f)
            lineTo(w * 0.44f, h * 0.26f)
            close()
        }
        drawPath(maplePath, color = leafColor)

        drawLine(
            color = Color.White.copy(alpha = 0.5f),
            start = Offset(w * 0.50f, h * 0.82f),
            end = Offset(w * 0.50f, h * 0.24f),
            strokeWidth = w * 0.03f,
            cap = StrokeCap.Round
        )
    }
}

// 13. Keping Salju Kristal
@Composable
fun SnowflakeIllustration(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f
        val flakeColor = if (color == Color.Unspecified) Color(0xFF8ECAE6) else color

        for (i in 0 until 6) {
            val angle = (i * 60f) * (Math.PI / 180f).toFloat()
            val len = w * 0.38f
            val endX = cx + cos(angle) * len
            val endY = cy + sin(angle) * len

            drawLine(
                color = flakeColor,
                start = Offset(cx, cy),
                end = Offset(endX, endY),
                strokeWidth = w * 0.05f,
                cap = StrokeCap.Round
            )

            val branchLen = len * 0.4f
            val midX = cx + cos(angle) * (len * 0.65f)
            val midY = cy + sin(angle) * (len * 0.65f)

            val vAngle1 = angle + 0.65f
            val vAngle2 = angle - 0.65f

            drawLine(color = flakeColor, start = Offset(midX, midY), end = Offset(midX + cos(vAngle1) * branchLen, midY + sin(vAngle1) * branchLen), strokeWidth = w * 0.035f, cap = StrokeCap.Round)
            drawLine(color = flakeColor, start = Offset(midX, midY), end = Offset(midX + cos(vAngle2) * branchLen, midY + sin(vAngle2) * branchLen), strokeWidth = w * 0.035f, cap = StrokeCap.Round)
        }

        drawCircle(color = flakeColor, radius = w * 0.08f, center = Offset(cx, cy), style = Stroke(width = w * 0.035f))
    }
}

// 14. Boneka Salju
@Composable
fun SnowmanIllustration(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        drawCircle(Color(0xFFF4F8FA), radius = w * 0.28f, center = Offset(w * 0.50f, h * 0.68f))
        drawCircle(Color(0xFFF8FCFE), radius = w * 0.20f, center = Offset(w * 0.50f, h * 0.36f))

        val scarfColor = if (color == Color.Unspecified) Color(0xFFBC4749) else color
        drawRoundRect(
            color = scarfColor,
            topLeft = Offset(w * 0.32f, h * 0.44f),
            size = Size(w * 0.36f, h * 0.10f),
            cornerRadius = CornerRadius(w * 0.04f, h * 0.04f)
        )
        drawCircle(Color(0xFF2C2825), radius = w * 0.03f, center = Offset(w * 0.44f, h * 0.32f))
        drawCircle(Color(0xFF2C2825), radius = w * 0.03f, center = Offset(w * 0.56f, h * 0.32f))

        val carrot = Path().apply {
            moveTo(w * 0.50f, h * 0.37f)
            lineTo(w * 0.66f, h * 0.39f)
            lineTo(w * 0.50f, h * 0.41f)
            close()
        }
        drawPath(carrot, color = Color(0xFFF77F00))
    }
}

// 15. Ranting Botanical Eucalyptus
@Composable
fun BotanicalSprigIllustration(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val leafColor = if (color == Color.Unspecified) Color(0xFF6B8E7B) else color

        val stem = Path().apply {
            moveTo(w * 0.25f, h * 0.88f)
            cubicTo(w * 0.35f, h * 0.60f, w * 0.50f, h * 0.35f, w * 0.75f, h * 0.12f)
        }
        drawPath(stem, color = Color(0xFF5A4D41), style = Stroke(width = w * 0.04f, cap = StrokeCap.Round))

        val leaves = listOf(
            Offset(w * 0.34f, h * 0.70f),
            Offset(w * 0.40f, h * 0.58f),
            Offset(w * 0.48f, h * 0.45f),
            Offset(w * 0.56f, h * 0.33f),
            Offset(w * 0.65f, h * 0.22f),
            Offset(w * 0.74f, h * 0.14f)
        )

        leaves.forEach { center ->
            drawOval(
                color = leafColor,
                topLeft = Offset(center.x - w * 0.14f, center.y - h * 0.08f),
                size = Size(w * 0.28f, h * 0.16f)
            )
        }
    }
}

// 16. Bunga Daisy / Aster
@Composable
fun DaisyFlowerIllustration(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f
        val petalColor = Color(0xFFFFFDF9)

        for (i in 0 until 8) {
            val angle = (i * 45f) * (Math.PI / 180f).toFloat()
            val px = cx + cos(angle) * (w * 0.22f)
            val py = cy + sin(angle) * (h * 0.22f)

            drawOval(
                color = petalColor,
                topLeft = Offset(px - w * 0.11f, py - h * 0.11f),
                size = Size(w * 0.22f, h * 0.22f)
            )
            drawOval(
                color = Color(0xFFE5DDD0),
                topLeft = Offset(px - w * 0.11f, py - h * 0.11f),
                size = Size(w * 0.22f, h * 0.22f),
                style = Stroke(width = 1.5f)
            )
        }

        drawCircle(Color(0xFFE9B949), radius = w * 0.15f, center = Offset(cx, cy))
    }
}

// 17. Segel Lilin Antik (Wax Seal)
@Composable
fun WaxSealIllustration(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val sealColor = if (color == Color.Unspecified) Color(0xFFA23E3E) else color

        val sealPath = Path().apply {
            moveTo(w * 0.20f, h * 0.35f)
            cubicTo(w * 0.12f, h * 0.60f, w * 0.30f, h * 0.88f, w * 0.50f, h * 0.88f)
            cubicTo(w * 0.70f, h * 0.90f, w * 0.88f, h * 0.72f, w * 0.84f, h * 0.48f)
            cubicTo(w * 0.86f, h * 0.24f, w * 0.68f, h * 0.12f, w * 0.48f, h * 0.14f)
            cubicTo(w * 0.30f, h * 0.12f, w * 0.18f, h * 0.24f, w * 0.20f, h * 0.35f)
            close()
        }
        drawPath(sealPath, color = sealColor)

        drawCircle(
            color = Color.White.copy(alpha = 0.3f),
            radius = w * 0.24f,
            center = Offset(w * 0.50f, h * 0.50f),
            style = Stroke(width = w * 0.04f)
        )

        val heart = Path().apply {
            moveTo(w * 0.50f, h * 0.58f)
            cubicTo(w * 0.38f, h * 0.46f, w * 0.38f, h * 0.38f, w * 0.44f, h * 0.38f)
            cubicTo(w * 0.48f, h * 0.38f, w * 0.50f, h * 0.42f, w * 0.50f, h * 0.44f)
            cubicTo(w * 0.50f, h * 0.42f, w * 0.52f, h * 0.38f, w * 0.56f, h * 0.38f)
            cubicTo(w * 0.62f, h * 0.38f, w * 0.62f, h * 0.46f, w * 0.50f, h * 0.58f)
            close()
        }
        drawPath(heart, color = Color.White.copy(alpha = 0.55f))
    }
}

// 18. Foto Polaroid Mini
@Composable
fun PolaroidCardIllustration(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        drawRoundRect(
            color = Color(0xFFFDFCFA),
            topLeft = Offset(w * 0.14f, h * 0.16f),
            size = Size(w * 0.72f, h * 0.74f),
            cornerRadius = CornerRadius(w * 0.03f, h * 0.03f)
        )
        drawRect(
            color = Color(0xFFE2C4A6),
            topLeft = Offset(w * 0.22f, h * 0.22f),
            size = Size(w * 0.56f, h * 0.44f)
        )
        drawCircle(Color(0xFFE87524), radius = w * 0.10f, center = Offset(w * 0.50f, h * 0.38f))

        val mountain = Path().apply {
            moveTo(w * 0.22f, h * 0.66f)
            lineTo(w * 0.40f, h * 0.46f)
            lineTo(w * 0.52f, h * 0.56f)
            lineTo(w * 0.66f, h * 0.42f)
            lineTo(w * 0.78f, h * 0.66f)
            close()
        }
        drawPath(mountain, color = Color(0xFF5C4033))

        drawRoundRect(
            color = (if (color == Color.Unspecified) Color(0xFFA2D2FF) else color).copy(alpha = 0.8f),
            topLeft = Offset(w * 0.36f, h * 0.10f),
            size = Size(w * 0.28f, h * 0.09f),
            cornerRadius = CornerRadius(w * 0.02f, h * 0.02f)
        )
    }
}

// 19. Pita Simpul (Ribbon Bow)
@Composable
fun RibbonBowIllustration(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val bowColor = if (color == Color.Unspecified) Color(0xFFFFB7B2) else color

        val leftLoop = Path().apply {
            moveTo(w * 0.50f, h * 0.48f)
            cubicTo(w * 0.24f, h * 0.25f, w * 0.12f, h * 0.45f, w * 0.26f, h * 0.62f)
            close()
        }
        val rightLoop = Path().apply {
            moveTo(w * 0.50f, h * 0.48f)
            cubicTo(w * 0.76f, h * 0.25f, w * 0.88f, h * 0.45f, w * 0.74f, h * 0.62f)
            close()
        }
        drawPath(leftLoop, color = bowColor)
        drawPath(rightLoop, color = bowColor)
        drawCircle(color = bowColor, radius = w * 0.08f, center = Offset(w * 0.50f, h * 0.48f))
    }
}

// 20. Awan Katun
@Composable
fun FluffyCloudIllustration(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val cloudColor = Color(0xFFF2F7FA)

        drawCircle(cloudColor, radius = w * 0.18f, center = Offset(w * 0.34f, h * 0.50f))
        drawCircle(cloudColor, radius = w * 0.24f, center = Offset(w * 0.52f, h * 0.42f))
        drawCircle(cloudColor, radius = w * 0.18f, center = Offset(w * 0.70f, h * 0.50f))
        drawRoundRect(
            color = cloudColor,
            topLeft = Offset(w * 0.26f, h * 0.48f),
            size = Size(w * 0.52f, h * 0.16f),
            cornerRadius = CornerRadius(w * 0.08f, h * 0.08f)
        )
        drawCircle(Color(0xFFFFD166), radius = w * 0.04f, center = Offset(w * 0.42f, h * 0.74f))
    }
}

// 21. Kaset Pita
@Composable
fun CassetteTapeIllustration(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val tapeColor = if (color == Color.Unspecified) Color(0xFF8D5B4C) else color

        drawRoundRect(
            color = tapeColor,
            topLeft = Offset(w * 0.12f, h * 0.24f),
            size = Size(w * 0.76f, h * 0.52f),
            cornerRadius = CornerRadius(w * 0.05f, h * 0.05f)
        )
        drawRoundRect(
            color = Color(0xFFF9F6F0),
            topLeft = Offset(w * 0.18f, h * 0.32f),
            size = Size(w * 0.64f, h * 0.36f),
            cornerRadius = CornerRadius(w * 0.03f, h * 0.03f)
        )
        drawCircle(Color(0xFF332D28), radius = w * 0.08f, center = Offset(w * 0.36f, h * 0.50f))
        drawCircle(Color(0xFF332D28), radius = w * 0.08f, center = Offset(w * 0.64f, h * 0.50f))
    }
}

// 22. Klip Kertas
@Composable
fun PaperclipIllustration(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val clipColor = if (color == Color.Unspecified) Color(0xFF9E978E) else color

        val clipPath = Path().apply {
            moveTo(w * 0.40f, h * 0.76f)
            lineTo(w * 0.40f, h * 0.24f)
            cubicTo(w * 0.40f, h * 0.12f, w * 0.65f, h * 0.12f, w * 0.65f, h * 0.24f)
            lineTo(w * 0.65f, h * 0.82f)
            cubicTo(w * 0.65f, h * 0.94f, w * 0.28f, h * 0.94f, w * 0.28f, h * 0.82f)
            lineTo(w * 0.28f, h * 0.36f)
            cubicTo(w * 0.28f, h * 0.26f, w * 0.52f, h * 0.26f, w * 0.52f, h * 0.36f)
            lineTo(w * 0.52f, h * 0.68f)
        }
        drawPath(clipPath, color = clipColor, style = Stroke(width = w * 0.06f, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
}

// 23. Stroberi
@Composable
fun StrawberryIllustration(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val berryColor = if (color == Color.Unspecified) Color(0xFFD64045) else color

        val berry = Path().apply {
            moveTo(w * 0.50f, h * 0.86f)
            cubicTo(w * 0.24f, h * 0.64f, w * 0.18f, h * 0.38f, w * 0.36f, h * 0.32f)
            cubicTo(w * 0.46f, h * 0.32f, w * 0.50f, h * 0.38f, w * 0.50f, h * 0.38f)
            cubicTo(w * 0.50f, h * 0.38f, w * 0.54f, h * 0.32f, w * 0.64f, h * 0.32f)
            cubicTo(w * 0.82f, h * 0.38f, w * 0.76f, h * 0.64f, w * 0.50f, h * 0.86f)
            close()
        }
        drawPath(berry, color = berryColor)
        drawOval(Color(0xFF588157), topLeft = Offset(w * 0.38f, h * 0.22f), size = Size(w * 0.24f, h * 0.12f))
    }
}

// 24. Kupu-kupu
@Composable
fun ButterflyIllustration(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val wingColor = if (color == Color.Unspecified) Color(0xFFD0C3F0) else color

        drawOval(wingColor, topLeft = Offset(w * 0.14f, h * 0.22f), size = Size(w * 0.34f, h * 0.34f))
        drawOval(wingColor.copy(alpha = 0.85f), topLeft = Offset(w * 0.22f, h * 0.50f), size = Size(w * 0.26f, h * 0.28f))
        drawOval(wingColor, topLeft = Offset(w * 0.52f, h * 0.22f), size = Size(w * 0.34f, h * 0.34f))
        drawOval(wingColor.copy(alpha = 0.85f), topLeft = Offset(w * 0.52f, h * 0.50f), size = Size(w * 0.26f, h * 0.28f))

        drawCircle(Color.White.copy(alpha = 0.7f), radius = w * 0.04f, center = Offset(w * 0.30f, h * 0.36f))
        drawCircle(Color.White.copy(alpha = 0.7f), radius = w * 0.04f, center = Offset(w * 0.70f, h * 0.36f))
    }
}

// 25. Acorn
@Composable
fun AcornIllustration(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        drawArc(
            color = Color(0xFF5C4033),
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = true,
            topLeft = Offset(w * 0.24f, h * 0.28f),
            size = Size(w * 0.52f, h * 0.30f)
        )
        drawLine(
            color = Color(0xFF5C4033),
            start = Offset(w * 0.50f, h * 0.28f),
            end = Offset(w * 0.50f, h * 0.16f),
            strokeWidth = w * 0.05f,
            cap = StrokeCap.Round
        )

        val nut = Path().apply {
            moveTo(w * 0.26f, h * 0.42f)
            lineTo(w * 0.74f, h * 0.42f)
            cubicTo(w * 0.74f, h * 0.72f, w * 0.58f, h * 0.86f, w * 0.50f, h * 0.86f)
            cubicTo(w * 0.42f, h * 0.86f, w * 0.26f, h * 0.72f, w * 0.26f, h * 0.42f)
            close()
        }
        drawPath(nut, color = Color(0xFFC59B76))
    }
}

// 26. Kucing Hitam
@Composable
fun BlackCatIllustration(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val catColor = Color(0xFF262327)

        drawCircle(catColor, radius = w * 0.22f, center = Offset(w * 0.50f, h * 0.44f))

        val leftEar = Path().apply {
            moveTo(w * 0.32f, h * 0.36f)
            lineTo(w * 0.30f, h * 0.16f)
            lineTo(w * 0.44f, h * 0.26f)
            close()
        }
        val rightEar = Path().apply {
            moveTo(w * 0.68f, h * 0.36f)
            lineTo(w * 0.70f, h * 0.16f)
            lineTo(w * 0.56f, h * 0.26f)
            close()
        }
        drawPath(leftEar, color = catColor)
        drawPath(rightEar, color = catColor)

        drawOval(catColor, topLeft = Offset(w * 0.32f, h * 0.50f), size = Size(w * 0.36f, h * 0.38f))
        drawCircle(Color(0xFF52B788), radius = w * 0.035f, center = Offset(w * 0.42f, h * 0.42f))
        drawCircle(Color(0xFF52B788), radius = w * 0.035f, center = Offset(w * 0.58f, h * 0.42f))
    }
}

// 27. Petir Neon
@Composable
fun NeonBoltIllustration(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val neon = if (color == Color.Unspecified) Color(0xFF00F0FF) else color

        val bolt = Path().apply {
            moveTo(w * 0.54f, h * 0.12f)
            lineTo(w * 0.26f, h * 0.50f)
            lineTo(w * 0.48f, h * 0.50f)
            lineTo(w * 0.42f, h * 0.88f)
            lineTo(w * 0.76f, h * 0.44f)
            lineTo(w * 0.54f, h * 0.44f)
            close()
        }
        drawPath(bolt, color = neon.copy(alpha = 0.4f), style = Stroke(width = w * 0.12f, join = StrokeJoin.Round))
        drawPath(bolt, color = neon)
        drawPath(bolt, color = Color.White.copy(alpha = 0.7f), style = Stroke(width = w * 0.03f))
    }
}

// 28. Bintang Kilau
@Composable
fun SparkleClusterIllustration(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val starColor = if (color == Color.Unspecified) Color(0xFFDFB27A) else color

        fun drawStar(cx: Float, cy: Float, r: Float) {
            val star = Path().apply {
                moveTo(cx, cy - r)
                cubicTo(cx, cy, cx, cy, cx + r, cy)
                cubicTo(cx, cy, cx, cy, cx, cy + r)
                cubicTo(cx, cy, cx, cy, cx - r, cy)
                cubicTo(cx, cy, cx, cy, cx, cy - r)
                close()
            }
            drawPath(star, color = starColor)
        }

        drawStar(w * 0.52f, h * 0.48f, w * 0.32f)
        drawStar(w * 0.24f, h * 0.30f, w * 0.14f)
        drawStar(w * 0.76f, h * 0.70f, w * 0.16f)
    }
}

@Composable
fun FallbackIllustration(key: String, color: Color) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(CircleShape)
            .background(color.copy(alpha = 0.2f))
            .border(1.5.dp, color, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = key.take(2).uppercase(),
            fontFamily = QuicksandFontFamily,
            fontWeight = FontWeight.Bold,
            color = color,
            fontSize = 16.sp
        )
    }
}
