package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CustomStickerEntity
import com.example.data.JournalElementEntity
import com.example.model.*
import com.example.ui.theme.*
import com.example.util.SoundManager

enum class DecorationTab(val title: String, val iconVector: androidx.compose.ui.graphics.vector.ImageVector) {
    STICKERS("Stiker", Icons.Outlined.AutoAwesome),
    WASHI("Pita Washi", Icons.Outlined.LinearScale),
    STICKY("Sisipan Kertas", Icons.Outlined.StickyNote2),
    TEXT("Tulisan", Icons.Outlined.Title),
    STAMP("Cap Jurnal", Icons.Outlined.Approval),
    PAPER("Pola Buku", Icons.Outlined.MenuBook),
    PALETTE("Palet Warna", Icons.Outlined.Palette)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DecorationBottomSheet(
    currentTheme: JournalTheme,
    currentPalette: PaletteType,
    currentPaper: PaperStyle,
    customStickers: List<CustomStickerEntity> = emptyList(),
    onDismiss: () -> Unit,
    onAddSticker: (String, String, Long) -> Unit,
    onAddCustomSticker: (CustomStickerEntity) -> Unit = {},
    onCreateCustomSticker: (String, String, String, String, Long, Long) -> Unit = { _, _, _, _, _, _ -> },
    onDeleteCustomSticker: (CustomStickerEntity) -> Unit = {},
    onAddWashiTape: (WashiTapePreset) -> Unit,
    onAddStickyNote: (String, Long) -> Unit,
    onAddText: (String, String, Int, Long) -> Unit,
    onAddStamp: (String, String, Long) -> Unit,
    onSelectPaper: (PaperStyle) -> Unit,
    onSelectPalette: (PaletteType) -> Unit,
    onSelectTheme: (JournalTheme) -> Unit
) {
    var selectedTab by remember { mutableStateOf(DecorationTab.STICKERS) }
    var selectedCategoryTheme by remember { mutableStateOf(currentTheme) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            // Tab Selector
            ScrollableTabRow(
                selectedTabIndex = selectedTab.ordinal,
                edgePadding = 16.dp,
                divider = {},
                containerColor = Color.Transparent
            ) {
                DecorationTab.entries.forEach { tab ->
                    Tab(
                        selected = selectedTab == tab,
                        onClick = {
                            selectedTab = tab
                            SoundManager.playClick()
                        },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = tab.iconVector,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = if (selectedTab == tab) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    tab.title,
                                    fontFamily = QuicksandFontFamily,
                                    fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            when (selectedTab) {
                DecorationTab.STICKERS -> {
                    StickerPickerContent(
                        selectedTheme = selectedCategoryTheme,
                        customStickers = customStickers,
                        currentPalette = currentPalette,
                        onThemeChange = {
                            selectedCategoryTheme = it
                            SoundManager.playClick()
                        },
                        onPick = { key, name ->
                            val color = currentPalette.colors.first().value.toLong()
                            onAddSticker(key, name, color)
                            onDismiss()
                        },
                        onPickCustom = { sticker ->
                            onAddCustomSticker(sticker)
                            onDismiss()
                        },
                        onCreateCustom = { name, shape, symbol, text, bg, accent ->
                            onCreateCustomSticker(name, shape, symbol, text, bg, accent)
                            onDismiss()
                        },
                        onDeleteCustom = { onDeleteCustomSticker(it) }
                    )
                }
                DecorationTab.WASHI -> {
                    WashiPickerContent(
                        onPick = { preset ->
                            onAddWashiTape(preset)
                            onDismiss()
                        }
                    )
                }
                DecorationTab.STICKY -> {
                    StickyNotePickerContent(
                        palette = currentPalette,
                        onAdd = { text, colorHex ->
                            onAddStickyNote(text, colorHex)
                            onDismiss()
                        }
                    )
                }
                DecorationTab.TEXT -> {
                    TextCreatorContent(
                        palette = currentPalette,
                        onAdd = { text, font, size, color ->
                            onAddText(text, font, size, color)
                            onDismiss()
                        }
                    )
                }
                DecorationTab.STAMP -> {
                    StampPickerContent(
                        palette = currentPalette,
                        onPick = { label, emoji, color ->
                            onAddStamp(label, emoji, color)
                            onDismiss()
                        }
                    )
                }
                DecorationTab.PAPER -> {
                    PaperPickerContent(
                        currentPaper = currentPaper,
                        onSelect = {
                            onSelectPaper(it)
                            onDismiss()
                        }
                    )
                }
                DecorationTab.PALETTE -> {
                    PaletteAndThemeContent(
                        currentTheme = currentTheme,
                        currentPalette = currentPalette,
                        onSelectPalette = {
                            onSelectPalette(it)
                        },
                        onSelectTheme = {
                            onSelectTheme(it)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun StickerPickerContent(
    selectedTheme: JournalTheme,
    customStickers: List<CustomStickerEntity>,
    currentPalette: PaletteType,
    onThemeChange: (JournalTheme) -> Unit,
    onPick: (String, String) -> Unit,
    onPickCustom: (CustomStickerEntity) -> Unit,
    onCreateCustom: (String, String, String, String, Long, Long) -> Unit,
    onDeleteCustom: (CustomStickerEntity) -> Unit
) {
    var stickerMode by remember { mutableStateOf("BUILTIN") } // "BUILTIN", "MY_CUSTOM", "STUDIO"

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        // Mode Selector: Stiker Asli, Stiker Buatan Saya, Studio Buat Stiker
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = stickerMode == "BUILTIN",
                onClick = {
                    stickerMode = "BUILTIN"
                    SoundManager.playClick()
                },
                label = { Text("Koleksi Asli", fontFamily = QuicksandFontFamily, fontSize = 12.sp) },
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                selected = stickerMode == "MY_CUSTOM",
                onClick = {
                    stickerMode = "MY_CUSTOM"
                    SoundManager.playClick()
                },
                label = { Text("Kustom Saya (${customStickers.size})", fontFamily = QuicksandFontFamily, fontSize = 12.sp) },
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                selected = stickerMode == "STUDIO",
                onClick = {
                    stickerMode = "STUDIO"
                    SoundManager.playClick()
                },
                label = { Text("+ Buat Stiker", fontFamily = QuicksandFontFamily, fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                modifier = Modifier.weight(1f)
            )
        }

        when (stickerMode) {
            "BUILTIN" -> {
                // Theme filters: Calm, Fall, Halloween, Snow, Simple, Earth, Pastel, Neon, Random
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    items(JournalTheme.entries) { theme ->
                        val isSelected = selectedTheme == theme
                        FilterChip(
                            selected = isSelected,
                            onClick = { onThemeChange(theme) },
                            label = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    ThemeVectorIcon(
                                        theme = theme,
                                        modifier = Modifier.size(13.dp),
                                        tint = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        theme.title.split(" ").first(),
                                        fontFamily = QuicksandFontFamily,
                                        fontSize = 12.sp
                                    )
                                }
                            },
                            modifier = Modifier.testTag("filter_theme_${theme.id}")
                        )
                    }
                }

                val filteredStickers = if (selectedTheme == JournalTheme.RANDOM) {
                    JournalPresets.STICKERS
                } else {
                    JournalPresets.STICKERS.filter { it.theme == selectedTheme }
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .heightIn(max = 280.dp)
                        .fillMaxWidth()
                ) {
                    items(filteredStickers) { sticker ->
                        Card(
                            onClick = { onPick(sticker.stickerKey, sticker.name) },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                OriginalStickerView(
                                    stickerKey = sticker.stickerKey,
                                    primaryColor = MaterialTheme.colorScheme.primary,
                                    scale = 0.9f,
                                    modifier = Modifier.size(50.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = sticker.name,
                                    fontFamily = QuicksandFontFamily,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }

            "MY_CUSTOM" -> {
                if (customStickers.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Outlined.AutoAwesome,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "Belum ada stiker buatan sendiri",
                                fontFamily = QuicksandFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            TextButton(onClick = { stickerMode = "STUDIO" }) {
                                Text("Buka Studio Buat Stiker Sekarang", fontFamily = QuicksandFontFamily, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .heightIn(max = 280.dp)
                            .fillMaxWidth()
                    ) {
                        items(customStickers) { sticker ->
                            Card(
                                onClick = { onPickCustom(sticker) },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    val key = "custom:${sticker.shapeType}:${sticker.symbolType}:${sticker.monogramText}:${sticker.bgColorHex}:${sticker.accentColorHex}"
                                    OriginalStickerView(
                                        stickerKey = key,
                                        primaryColor = Color(sticker.bgColorHex),
                                        scale = 0.9f,
                                        modifier = Modifier.size(50.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = sticker.name,
                                        fontFamily = QuicksandFontFamily,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            }

            "STUDIO" -> {
                // Studio Pembuat Stiker Mandiri
                CustomStickerStudio(
                    palette = currentPalette,
                    onSave = { name, shape, symbol, text, bg, accent ->
                        onCreateCustom(name, shape, symbol, text, bg, accent)
                    }
                )
            }
        }
    }
}

/**
 * Studio Stiker Mandiri: Komponen interaktif bagi pengguna untuk mendesain stiker sendiri
 */
@Composable
fun CustomStickerStudio(
    palette: PaletteType,
    onSave: (name: String, shape: String, symbol: String, text: String, bgHex: Long, accentHex: Long) -> Unit
) {
    var stickerName by remember { mutableStateOf("") }
    var selectedShape by remember { mutableStateOf("STAMP") }
    var selectedSymbol by remember { mutableStateOf("COFFEE") }
    var monogramInput by remember { mutableStateOf("JOUR") }
    var selectedBgColor by remember { mutableStateOf(palette.colors.first()) }
    var selectedAccentColor by remember { mutableStateOf(Color.White) }

    val shapes = listOf(
        "STAMP" to "Perangko",
        "CIRCLE" to "Lingkaran",
        "WAX_SEAL" to "Lilin",
        "HEXAGON" to "Segienam",
        "ARCH" to "Kubah"
    )

    val symbols = listOf(
        "COFFEE" to "Kopi",
        "BOTANICAL" to "Botani",
        "DAISY" to "Daisy",
        "HEART" to "Hati",
        "STAR" to "Bintang",
        "GHOST" to "Hantu",
        "CAT" to "Kucing"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 340.dp)
    ) {
        // Pratinjau Langsung Stiker Kustom
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val previewKey = "custom:$selectedShape:$selectedSymbol:$monogramInput:${selectedBgColor.value.toLong()}:${selectedAccentColor.value.toLong()}"
                OriginalStickerView(
                    stickerKey = previewKey,
                    primaryColor = selectedBgColor,
                    scale = 1.0f,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "Pratinjau Stiker Kustom",
                        fontFamily = QuicksandFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "Bentuk: $selectedShape • Teks: '$monogramInput'",
                        fontFamily = QuicksandFontFamily,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Pilihan Bentuk
        Text("Pilih Bentuk Lencana:", fontFamily = QuicksandFontFamily, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.padding(vertical = 4.dp)) {
            items(shapes) { (sKey, sLabel) ->
                FilterChip(
                    selected = selectedShape == sKey,
                    onClick = {
                        selectedShape = sKey
                        SoundManager.playClick()
                    },
                    label = { Text(sLabel, fontSize = 11.sp, fontFamily = QuicksandFontFamily) }
                )
            }
        }

        // Pilihan Teks Monogram
        OutlinedTextField(
            value = monogramInput,
            onValueChange = { monogramInput = it.take(8) },
            placeholder = { Text("Teks singkat (misal: Cozy / Ayu / 2026)", fontSize = 11.sp) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            shape = RoundedCornerShape(8.dp)
        )

        // Pilihan Warna Dasar Lencana
        Text("Warna Lencana:", fontFamily = QuicksandFontFamily, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            palette.colors.forEach { col ->
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(col)
                        .clickable {
                            selectedBgColor = col
                            SoundManager.playClick()
                        }
                        .then(
                            if (selectedBgColor == col) Modifier.border(2.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                            else Modifier
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val finalName = if (monogramInput.isNotBlank()) "Stiker $monogramInput" else "Lencana Kustom"
                onSave(
                    finalName,
                    selectedShape,
                    selectedSymbol,
                    monogramInput,
                    selectedBgColor.value.toLong(),
                    selectedAccentColor.value.toLong()
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Simpan & Tempelkan ke Lembaran", fontFamily = QuicksandFontFamily, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun WashiPickerContent(
    onPick: (WashiTapePreset) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Pita Perekat Washi Tape Asli (Tanpa Teks)",
            fontFamily = QuicksandFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.heightIn(max = 260.dp)
        ) {
            items(JournalPresets.WASHI_TAPES) { preset ->
                val primaryColor = Color(preset.primaryColorHex)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onPick(preset) }
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(34.dp)
                            .shadow(elevation = 2.dp, shape = RoundedCornerShape(3.dp))
                            .clip(RoundedCornerShape(3.dp))
                            .background(primaryColor)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = preset.name,
                        fontFamily = QuicksandFontFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun StickyNotePickerContent(
    palette: PaletteType,
    onAdd: (String, Long) -> Unit
) {
    var noteText by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(palette.colors.first()) }

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        OutlinedTextField(
            value = noteText,
            onValueChange = { noteText = it },
            placeholder = { Text("Tulis catatan atau pengingat...", fontFamily = QuicksandFontFamily) },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("Pilih Warna Memo:", fontFamily = QuicksandFontFamily, fontSize = 13.sp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            palette.colors.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(color)
                        .clickable {
                            selectedColor = color
                            SoundManager.playClick()
                        }
                        .then(
                            if (selectedColor == color) {
                                Modifier.border(2.5.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                            } else Modifier
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { onAdd(noteText, selectedColor.value.toLong()) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Tempelkan Memo", fontFamily = QuicksandFontFamily, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun TextCreatorContent(
    palette: PaletteType,
    onAdd: (String, String, Int, Long) -> Unit
) {
    var textInput by remember { mutableStateOf("") }
    var selectedFont by remember { mutableStateOf("SACRAMENTO") }
    var selectedSize by remember { mutableIntStateOf(24) }
    var selectedColor by remember { mutableStateOf(Color(0xFF332D28)) }

    val fontOptions = listOf(
        "SACRAMENTO" to "Kaligrafi Halus (Sacramento)",
        "CAVEAT" to "Tulisan Tangan (Caveat)",
        "INDIE_FLOWER" to "Doodle Lucu (Indie)",
        "PLAYFAIR" to "Klasik Anggun (Playfair)",
        "QUICKSAND" to "Lembut Minimal (Quicksand)",
        "MONO" to "Mesin Tik (Monospace)"
    )

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        OutlinedTextField(
            value = textInput,
            onValueChange = { textInput = it },
            placeholder = { Text("Ketik kata mutiara, refleksi, atau judul...", fontFamily = QuicksandFontFamily) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("Variasi Gaya Tipografi:", fontFamily = QuicksandFontFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 6.dp)
        ) {
            items(fontOptions) { (key, label) ->
                FilterChip(
                    selected = selectedFont == key,
                    onClick = {
                        selectedFont = key
                        SoundManager.playClick()
                    },
                    label = {
                        Text(
                            label,
                            fontFamily = getFontFamilyByName(key),
                            fontSize = 14.sp
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Pilihan Warna Teks:", fontFamily = QuicksandFontFamily, fontSize = 13.sp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            palette.colors.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(color)
                        .clickable {
                            selectedColor = color
                            SoundManager.playClick()
                        }
                        .then(
                            if (selectedColor == color) {
                                Modifier.border(2.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                            } else Modifier
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { onAdd(textInput, selectedFont, selectedSize, selectedColor.value.toLong()) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Tempelkan Tulisan", fontFamily = QuicksandFontFamily, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun StampPickerContent(
    palette: PaletteType,
    onPick: (String, String, Long) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Cap & Stempel Jurnal Otentik",
            fontFamily = QuicksandFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.heightIn(max = 250.dp)
        ) {
            items(JournalPresets.STAMPS) { stamp ->
                val color = palette.colors.first()
                Surface(
                    onClick = { onPick(stamp.title, stamp.subtitle, color.value.toLong()) },
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.padding(8.dp), contentAlignment = Alignment.Center) {
                        VintageRubberStamp(
                            title = stamp.title,
                            subtitle = stamp.subtitle,
                            color = color,
                            scale = 0.95f
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PaperPickerContent(
    currentPaper: PaperStyle,
    onSelect: (PaperStyle) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Pilih Pola Lembaran Notebook",
            fontFamily = QuicksandFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.heightIn(max = 260.dp)
        ) {
            items(PaperStyle.entries) { paper ->
                val isSelected = currentPaper == paper
                Card(
                    onClick = { onSelect(paper) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = paper.title,
                            fontFamily = QuicksandFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = paper.description,
                            fontFamily = QuicksandFontFamily,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PaletteAndThemeContent(
    currentTheme: JournalTheme,
    currentPalette: PaletteType,
    onSelectPalette: (PaletteType) -> Unit,
    onSelectTheme: (JournalTheme) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .heightIn(max = 340.dp)
    ) {
        Text(
            text = "Ganti Tema Jurnal:",
            fontFamily = QuicksandFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            items(JournalTheme.entries) { theme ->
                val isSelected = currentTheme == theme
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelectTheme(theme) },
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            ThemeVectorIcon(
                                theme = theme,
                                modifier = Modifier.size(15.dp),
                                tint = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(theme.title, fontFamily = QuicksandFontFamily)
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Pilih Palet Warna:",
            fontFamily = QuicksandFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            PaletteType.entries.forEach { palette ->
                val isSelected = currentPalette == palette
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                            else Color.Transparent
                        )
                        .clickable { onSelectPalette(palette) }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = palette.title,
                        fontFamily = QuicksandFontFamily,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.width(140.dp)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        palette.colors.forEach { color ->
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(color)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SelectedElementControlBar(
    element: JournalElementEntity,
    currentPalette: PaletteType,
    onScaleChange: (Float) -> Unit,
    onRotationChange: (Float) -> Unit,
    onLengthChange: (Int) -> Unit = {},
    onColorChange: (Long) -> Unit,
    onFontChange: (String) -> Unit,
    onEditText: () -> Unit,
    onBringForward: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Header info & Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = when (element.type) {
                            "STICKER" -> "Stiker Ilustrasi"
                            "WASHI_TAPE" -> "Pita Washi"
                            "STICKY_NOTE" -> "Memo Tempel"
                            "TEXT" -> "Tulisan"
                            "STAMP" -> "Cap Jurnal"
                            else -> "Elemen"
                        },
                        fontFamily = QuicksandFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    if (element.type == "TEXT" || element.type == "STICKY_NOTE") {
                        IconButton(onClick = onEditText, modifier = Modifier.size(36.dp)) {
                            Icon(Icons.Outlined.Edit, "Ubah Teks", modifier = Modifier.size(18.dp))
                        }
                    }
                    IconButton(onClick = onBringForward, modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Outlined.FlipToFront, "Majukan", modifier = Modifier.size(18.dp))
                    }
                    IconButton(onClick = onDuplicate, modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Outlined.ContentCopy, "Duplikat", modifier = Modifier.size(18.dp))
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                        Icon(
                            Icons.Outlined.Delete,
                            "Hapus",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Size & Rotation sliders
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Ukuran", fontSize = 11.sp, fontFamily = QuicksandFontFamily, modifier = Modifier.width(46.dp))
                Slider(
                    value = element.scale,
                    onValueChange = onScaleChange,
                    valueRange = 0.6f..2.5f,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Putar", fontSize = 11.sp, fontFamily = QuicksandFontFamily, modifier = Modifier.width(36.dp))
                Slider(
                    value = element.rotation,
                    onValueChange = onRotationChange,
                    valueRange = -90f..90f,
                    modifier = Modifier.weight(1f)
                )
            }

            // Slider Penyesuaian Panjang Khusus untuk Pita Washi Tape
            if (element.type == "WASHI_TAPE") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val lengthVal = if (element.fontSize in 50..380) element.fontSize else 140
                    Text("Panjang", fontSize = 11.sp, fontFamily = QuicksandFontFamily, modifier = Modifier.width(46.dp))
                    Slider(
                        value = lengthVal.toFloat(),
                        onValueChange = { onLengthChange(it.toInt()) },
                        valueRange = 60f..340f,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${lengthVal}dp",
                        fontSize = 11.sp,
                        fontFamily = QuicksandFontFamily,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.width(40.dp)
                    )
                }
            }

            // Color / Font options if applicable
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Color choices
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    currentPalette.colors.take(5).forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(color)
                                .clickable { onColorChange(color.value.toLong()) }
                        )
                    }
                }

                // Quick font changer for text/sticky
                if (element.type == "TEXT" || element.type == "STICKY_NOTE") {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.widthIn(max = 200.dp)
                    ) {
                        val availableFonts = listOf(
                            "SACRAMENTO" to "Kaligrafi",
                            "CAVEAT" to "Tangan",
                            "INDIE_FLOWER" to "Doodle",
                            "PLAYFAIR" to "Serif",
                            "QUICKSAND" to "Sans"
                        )
                        items(availableFonts) { (fKey, fLabel) ->
                            val isSelected = element.fontFamilyName.equals(fKey, ignoreCase = true)
                            AssistChip(
                                onClick = { onFontChange(fKey) },
                                label = { Text(fLabel, fontSize = 10.sp) },
                                colors = if (isSelected) AssistChipDefaults.assistChipColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                ) else AssistChipDefaults.assistChipColors()
                            )
                        }
                    }
                }
            }
        }
    }
}
