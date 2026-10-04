package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.JournalElementEntity
import com.example.model.PaperStyle
import com.example.ui.theme.*
import com.example.util.SoundManager
import kotlin.math.roundToInt

fun getFontFamilyByName(name: String): FontFamily {
    return when (name.uppercase()) {
        "SACRAMENTO" -> SacramentoFontFamily
        "CAVEAT" -> CaveatFontFamily
        "INDIE_FLOWER" -> IndieFlowerFontFamily
        "PLAYFAIR" -> PlayfairFontFamily
        "QUICKSAND" -> QuicksandFontFamily
        "MONO" -> FontFamily.Monospace
        else -> FontFamily.Default
    }
}

@Composable
fun NotebookPaperCanvas(
    paperStyle: PaperStyle,
    isDark: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val deskBg = if (isDark) Color(0xFF141318) else Color(0xFFEDE4D8)
    val coverColor = if (isDark) Color(0xFF261D18) else Color(0xFFB58863) // Coklat muda / leather
    val pageBgColor = if (isDark) paperStyle.darkPaperBg else paperStyle.lightPaperBg
    val patternColor = if (isDark) paperStyle.darkPatternColor else paperStyle.lightPatternColor
    val ribbonColor = if (isDark) Color(0xFF5E3023) else Color(0xFF8ECAE6) // Soft blue satin ribbon

    // Meja/Alas luar tempat notebook diletakkan
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(deskBg)
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        // Lapisan Sampul Notebook Hardcover (Cover buku terlihat di sekeliling kertas)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shadow(elevation = 10.dp, shape = RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(coverColor)
                .padding(start = 18.dp, top = 10.dp, end = 12.dp, bottom = 12.dp)
        ) {
            // Efek tumpukan kertas notebook (stacked page depth di tepi kanan & bawah)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .shadow(elevation = 3.dp, shape = RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp, topEnd = 12.dp, bottomEnd = 12.dp))
                    .clip(RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp, topEnd = 12.dp, bottomEnd = 12.dp))
                    .background(pageBgColor)
            ) {
                // Pola lembaran buku (Dotted, Grid, Lines, Kraft)
                if (paperStyle != PaperStyle.BLANK) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val canvasWidth = size.width
                        val canvasHeight = size.height

                        when (paperStyle) {
                            PaperStyle.DOTS -> {
                                val spacing = 28.dp.toPx()
                                var y = spacing + 40.dp.toPx()
                                while (y < canvasHeight) {
                                    var x = spacing + 24.dp.toPx()
                                    while (x < canvasWidth - 16.dp.toPx()) {
                                        drawCircle(
                                            color = patternColor,
                                            radius = 1.6f,
                                            center = Offset(x, y)
                                        )
                                        x += spacing
                                    }
                                    y += spacing
                                }
                            }
                            PaperStyle.GRID -> {
                                val spacing = 26.dp.toPx()
                                var x = 32.dp.toPx()
                                while (x <= canvasWidth - 12.dp.toPx()) {
                                    drawLine(
                                        color = patternColor,
                                        start = Offset(x, 40.dp.toPx()),
                                        end = Offset(x, canvasHeight - 12.dp.toPx()),
                                        strokeWidth = 1f
                                    )
                                    x += spacing
                                }
                                var y = 40.dp.toPx()
                                while (y <= canvasHeight - 12.dp.toPx()) {
                                    drawLine(
                                        color = patternColor,
                                        start = Offset(32.dp.toPx(), y),
                                        end = Offset(canvasWidth - 12.dp.toPx(), y),
                                        strokeWidth = 1f
                                    )
                                    y += spacing
                                }
                            }
                            PaperStyle.LINES -> {
                                val spacing = 32.dp.toPx()
                                var y = 56.dp.toPx()
                                while (y <= canvasHeight - 16.dp.toPx()) {
                                    drawLine(
                                        color = patternColor,
                                        start = Offset(32.dp.toPx(), y),
                                        end = Offset(canvasWidth - 20.dp.toPx(), y),
                                        strokeWidth = 1.2f
                                    )
                                    y += spacing
                                }
                            }
                            PaperStyle.KRAFT -> {
                                val spacing = 38.dp.toPx()
                                var y = 56.dp.toPx()
                                while (y <= canvasHeight - 16.dp.toPx()) {
                                    drawLine(
                                        color = patternColor.copy(alpha = 0.45f),
                                        start = Offset(32.dp.toPx(), y),
                                        end = Offset(canvasWidth - 20.dp.toPx(), y),
                                        strokeWidth = 0.8f,
                                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 20f), 0f)
                                    )
                                    y += spacing
                                }
                            }
                            PaperStyle.DARK -> {
                                val spacing = 32.dp.toPx()
                                var y = spacing + 40.dp.toPx()
                                while (y < canvasHeight - 16.dp.toPx()) {
                                    var x = spacing + 24.dp.toPx()
                                    while (x < canvasWidth - 16.dp.toPx()) {
                                        drawCircle(
                                            color = patternColor,
                                            radius = 1.3f,
                                            center = Offset(x, y)
                                        )
                                        x += spacing
                                    }
                                    y += spacing
                                }
                            }
                            else -> {}
                        }

                        // Garis tepi margin buku jurnal merah/rose halus di kiri (notebook margin guide)
                        val marginX = 26.dp.toPx()
                        drawLine(
                            color = if (isDark) Color(0x33E56B6F) else Color(0x44D97724),
                            start = Offset(marginX, 36.dp.toPx()),
                            end = Offset(marginX, canvasHeight - 16.dp.toPx()),
                            strokeWidth = 1.2f
                        )
                    }
                }

                // Header Cetakan Halaman Notebook Asli (Header jurnal fisik)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp, end = 24.dp, top = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "OURJOUR NOTEBOOK",
                        fontFamily = QuicksandFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp,
                        letterSpacing = 2.sp,
                        color = if (isDark) Color(0xFF6B6676) else Color(0xFF9E8B77)
                    )
                    Text(
                        text = "TANGGAL: ________________",
                        fontFamily = QuicksandFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 9.sp,
                        letterSpacing = 1.sp,
                        color = if (isDark) Color(0xFF6B6676) else Color(0xFF9E8B77)
                    )
                }

                // Konten stiker / dekorasi di atas kertas notebook
                content()
            }
        }

        // Pita Pembatas Buku Satin (Notebook Ribbon Bookmark) menjuntai di atas
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 36.dp)
                .width(18.dp)
                .height(34.dp)
                .shadow(elevation = 3.dp, shape = RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp))
                .clip(RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp))
                .background(ribbonColor)
        )

        // Spiral Ring Binder di sisi kiri (Penjilid buku notebook spiral otentik)
        Canvas(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .width(28.dp)
                .fillMaxHeight()
                .padding(vertical = 24.dp)
        ) {
            val ringCount = 14
            val ringHeight = size.height / ringCount
            val ringColor = if (isDark) Color(0xFF857E78) else Color(0xFF7A6B5D) // Perunggu / metalik
            val ringHighlight = if (isDark) Color(0xFFB0AAA4) else Color(0xFFE2DCD5)

            for (i in 0 until ringCount) {
                val cy = ringHeight * i + ringHeight / 2

                // Lubang kertas di sisi jilid
                drawCircle(
                    color = Color.Black.copy(alpha = 0.35f),
                    radius = 3.5.dp.toPx(),
                    center = Offset(16.dp.toPx(), cy)
                )

                // Cincin spiral melengkung
                val ringPath = Path().apply {
                    moveTo(2.dp.toPx(), cy - 4.dp.toPx())
                    cubicTo(
                        10.dp.toPx(), cy - 7.dp.toPx(),
                        22.dp.toPx(), cy - 5.dp.toPx(),
                        20.dp.toPx(), cy + 3.dp.toPx()
                    )
                }
                drawPath(ringPath, color = ringColor, style = Stroke(width = 2.4.dp.toPx(), cap = StrokeCap.Round))
                drawPath(ringPath, color = ringHighlight, style = Stroke(width = 0.8.dp.toPx(), cap = StrokeCap.Round))
            }
        }
    }
}

@Composable
fun JournalCanvasItem(
    element: JournalElementEntity,
    isSelected: Boolean,
    isReadOnly: Boolean,
    canvasWidthPx: Float,
    canvasHeightPx: Float,
    onSelect: () -> Unit,
    onDrag: (Float, Float) -> Unit
) {
    var itemOffsetX by remember(element.id, element.posX) { mutableFloatStateOf(element.posX) }
    var itemOffsetY by remember(element.id, element.posY) { mutableFloatStateOf(element.posY) }
    var isDragging by remember { mutableStateOf(false) }

    LaunchedEffect(element.posX, element.posY) {
        if (!isDragging) {
            itemOffsetX = element.posX
            itemOffsetY = element.posY
        }
    }

    // Animasi pergerakan halus (Smooth spring lift saat diangkat & digeser)
    val animatedLiftScale by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (isDragging) 1.07f else if (isSelected) 1.02f else 1.0f,
        animationSpec = androidx.compose.animation.core.spring(
            dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy,
            stiffness = androidx.compose.animation.core.Spring.StiffnessLow
        ),
        label = "dragLift"
    )

    val animatedElevation by androidx.compose.animation.core.animateDpAsState(
        targetValue = if (isDragging) 12.dp else if (isSelected) 4.dp else 0.dp,
        animationSpec = androidx.compose.animation.core.spring(
            dampingRatio = androidx.compose.animation.core.Spring.DampingRatioNoBouncy,
            stiffness = androidx.compose.animation.core.Spring.StiffnessMedium
        ),
        label = "dragElevation"
    )

    val posX = (itemOffsetX * canvasWidthPx).coerceAtLeast(0f)
    val posY = (itemOffsetY * canvasHeightPx).coerceAtLeast(0f)

    Box(
        modifier = Modifier
            .offset { IntOffset(posX.roundToInt(), posY.roundToInt()) }
            .rotate(element.rotation)
            .shadow(elevation = animatedElevation, shape = RoundedCornerShape(8.dp))
            .then(
                if (!isReadOnly) {
                    Modifier.pointerInput(element.id) {
                        detectDragGestures(
                            onDragStart = {
                                isDragging = true
                                onSelect()
                                SoundManager.playClick()
                            },
                            onDragEnd = {
                                isDragging = false
                                onDrag(itemOffsetX, itemOffsetY)
                            },
                            onDragCancel = {
                                isDragging = false
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                if (canvasWidthPx > 0 && canvasHeightPx > 0) {
                                    itemOffsetX = (itemOffsetX + dragAmount.x / canvasWidthPx).coerceIn(0.01f, 0.90f)
                                    itemOffsetY = (itemOffsetY + dragAmount.y / canvasHeightPx).coerceIn(0.01f, 0.90f)
                                    onDrag(itemOffsetX, itemOffsetY)
                                }
                            }
                        )
                    }
                } else Modifier
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                if (!isReadOnly) {
                    onSelect()
                }
            }
            .then(
                if (isSelected && !isReadOnly) {
                    Modifier.border(
                        width = 1.5.dp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(8.dp)
                    )
                } else Modifier
            )
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier.then(
                if (animatedLiftScale != 1.0f) {
                    Modifier.graphicsLayer {
                        scaleX = animatedLiftScale
                        scaleY = animatedLiftScale
                    }
                } else Modifier
            )
        ) {
            when (element.type) {
                "STICKER" -> {
                    // Ilustrasi Original (BUKAN keyboard emoji)
                    OriginalStickerView(
                        stickerKey = element.content,
                        primaryColor = Color(element.colorHex),
                        scale = element.scale,
                        modifier = Modifier.testTag("sticker_${element.id}")
                    )
                }
                "WASHI_TAPE" -> {
                    // Pita washi murni motif estetik TANPA tulisan nama/warna
                    WashiTapeItem(element = element)
                }
                "STICKY_NOTE" -> {
                    StickyNoteItem(element = element)
                }
                "TEXT" -> {
                    Text(
                        text = element.content,
                        fontSize = (element.fontSize * element.scale).sp,
                        fontFamily = getFontFamilyByName(element.fontFamilyName),
                        color = Color(element.colorHex),
                        textAlign = TextAlign.Start,
                        lineHeight = ((element.fontSize * element.scale) * 1.35f).sp,
                        modifier = Modifier
                            .widthIn(max = 280.dp)
                            .testTag("text_${element.id}")
                    )
                }
                "STAMP" -> {
                    StampItem(element = element)
                }
                else -> {
                    Text(element.content)
                }
            }
        }
    }
}

/**
 * Pita Washi Tape Estetik Murni.
 * TANPA teks nama warna pada permukaannya (persis seperti washi tape jurnal sungguhan),
 * dengan tekstur semi-transparan dan ujung bergerigi/robekan kertas realistis.
 */
@Composable
fun WashiTapeItem(element: JournalElementEntity) {
    val primaryColor = Color(element.colorHex)
    val secondaryColor = Color(element.secondaryColorHex)
    val lengthDp = if (element.fontSize in 50..380) element.fontSize else 140
    val tapeWidth = (lengthDp * element.scale).dp
    val tapeHeight = (28 * element.scale).dp

    Box(
        modifier = Modifier
            .width(tapeWidth)
            .height(tapeHeight)
            .shadow(elevation = 1.5.dp, shape = RoundedCornerShape(2.dp))
            .clip(RoundedCornerShape(2.dp))
            .background(primaryColor.copy(alpha = 0.82f))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Render pola washi tape estetik sesuai nama/tipe
            val isGingham = element.content.contains("Gingham", ignoreCase = true)
            val isDots = element.content.contains("Polkadot", ignoreCase = true)
            val isGrid = element.content.contains("Grid", ignoreCase = true)

            if (isDots) {
                val dotRadius = 2.2f * element.scale
                val stepX = 14.dp.toPx()
                val stepY = 10.dp.toPx()
                var y = stepY / 2
                while (y < h) {
                    var x = stepX / 2
                    while (x < w) {
                        drawCircle(secondaryColor.copy(alpha = 0.75f), radius = dotRadius, center = Offset(x, y))
                        x += stepX
                    }
                    y += stepY
                }
            } else if (isGrid) {
                val step = 10.dp.toPx()
                var x = 0f
                while (x < w) {
                    drawLine(secondaryColor.copy(alpha = 0.4f), start = Offset(x, 0f), end = Offset(x, h), strokeWidth = 1f)
                    x += step
                }
                var y = 0f
                while (y < h) {
                    drawLine(secondaryColor.copy(alpha = 0.4f), start = Offset(0f, y), end = Offset(w, y), strokeWidth = 1f)
                    y += step
                }
            } else if (isGingham) {
                val band = 12.dp.toPx()
                var x = 0f
                while (x < w) {
                    drawRect(secondaryColor.copy(alpha = 0.35f), topLeft = Offset(x, 0f), size = Size(band / 2, h))
                    x += band
                }
                var y = 0f
                while (y < h) {
                    drawRect(secondaryColor.copy(alpha = 0.35f), topLeft = Offset(0f, y), size = Size(w, band / 2))
                    y += band
                }
            } else {
                // Diagonal stripes (garis diagonal lembut)
                val step = 14.dp.toPx()
                var x = -h
                while (x < w + h) {
                    drawLine(
                        color = secondaryColor.copy(alpha = 0.55f),
                        start = Offset(x, 0f),
                        end = Offset(x + h, h),
                        strokeWidth = 3f * element.scale
                    )
                    x += step
                }
            }

            // Ujung sobekan washi bergerigi di kiri dan kanan (efek pita sobek tangan)
            val tearWidth = 4.dp.toPx()
            for (i in 0 until 5) {
                val y1 = (h / 5) * i
                val y2 = (h / 5) * (i + 1)
                drawLine(Color.White.copy(alpha = 0.5f), Offset(0f, y1), Offset(tearWidth, (y1 + y2) / 2), strokeWidth = 1.2f)
                drawLine(Color.White.copy(alpha = 0.5f), Offset(w, y1), Offset(w - tearWidth, (y1 + y2) / 2), strokeWidth = 1.2f)
            }
        }
    }
}

@Composable
fun StickyNoteItem(element: JournalElementEntity) {
    val noteBg = Color(element.colorHex)
    val noteText = Color(element.secondaryColorHex)

    Box(
        modifier = Modifier
            .widthIn(min = (130 * element.scale).dp, max = (240 * element.scale).dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(4.dp))
            .background(noteBg, RoundedCornerShape(4.dp))
            .padding((12 * element.scale).dp)
    ) {
        Column {
            // Little tape pin on top
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .offset(y = (-6).dp)
                    .width(36.dp)
                    .height(10.dp)
                    .background(Color(0x44000000), RoundedCornerShape(2.dp))
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = element.content,
                fontSize = (element.fontSize * element.scale).sp,
                fontFamily = getFontFamilyByName(element.fontFamilyName),
                color = noteText,
                lineHeight = ((element.fontSize * element.scale) * 1.3f).sp
            )
        }
    }
}

@Composable
fun StampItem(element: JournalElementEntity) {
    val stampColor = Color(element.colorHex)
    val parts = element.content.split("•")
    val title = parts.getOrNull(0)?.trim() ?: element.content
    val subtitle = parts.getOrNull(1)?.trim() ?: ""

    VintageRubberStamp(
        title = title,
        subtitle = subtitle,
        color = stampColor,
        scale = element.scale,
        modifier = Modifier.testTag("stamp_${element.id}")
    )
}

@Composable
fun FloatingReactionsOverlay(
    reactions: List<Pair<String, Long>>,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        reactions.takeLast(6).forEachIndexed { index, pair ->
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut()
            ) {
                val randomX = (15 + (index * 25) % 70)
                val randomY = (60 - (index * 12) % 40)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(x = (randomX * 4).dp, y = (-randomY * 8).dp)
                ) {
                    StampedReactionBadge(
                        reactionKey = pair.first,
                        size = 44.dp
                    )
                }
            }
        }
    }
}
