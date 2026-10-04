package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.JournalEntity
import com.example.model.JournalTheme
import com.example.model.PaletteType
import com.example.model.PaperStyle
import com.example.ui.DarkThemeConfig
import com.example.ui.JournalViewModel
import com.example.ui.components.ThemeBadge
import com.example.ui.components.ThemeVectorIcon
import com.example.ui.theme.PlayfairFontFamily
import com.example.ui.theme.QuicksandFontFamily
import com.example.util.SoundManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Palet Coklat Muda & Soft Blue Khas OurJour
val CoklatMudaWarm = Color(0xFFC59B76)
val CoklatMudaKaramel = Color(0xFFD8B48F)
val CoklatMudaAlmond = Color(0xFFE8D5C4)
val CoklatMudaKrim = Color(0xFFFAF3EC)
val SoftBlueAccent = Color(0xFFA2D2FF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalListScreen(
    viewModel: JournalViewModel,
    modifier: Modifier = Modifier
) {
    val journals by viewModel.allJournals.collectAsState()
    val darkConfig by viewModel.darkThemeConfig.collectAsState()
    val isSoundMuted by viewModel.isSoundMuted.collectAsState()

    var selectedThemeFilter by remember { mutableStateOf<String?>("ALL") }
    var showCreateDialog by remember { mutableStateOf(false) }
    var journalToDelete by remember { mutableStateOf<JournalEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(CoklatMudaWarm.copy(alpha = 0.2f))
                                .border(1.dp, CoklatMudaWarm.copy(alpha = 0.4f), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.MenuBook,
                                contentDescription = null,
                                tint = CoklatMudaWarm,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "OurJour",
                                fontFamily = PlayfairFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "Koleksi buku notebook dekorasi & kolaborasi",
                                fontFamily = QuicksandFontFamily,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    // Dark mode quick switch
                    IconButton(
                        onClick = {
                            val nextConfig = when (darkConfig) {
                                DarkThemeConfig.SYSTEM -> DarkThemeConfig.DARK
                                DarkThemeConfig.DARK -> DarkThemeConfig.LIGHT
                                DarkThemeConfig.LIGHT -> DarkThemeConfig.SYSTEM
                            }
                            viewModel.setDarkThemeConfig(nextConfig)
                        }
                    ) {
                        Icon(
                            imageVector = when (darkConfig) {
                                DarkThemeConfig.DARK -> Icons.Default.DarkMode
                                DarkThemeConfig.LIGHT -> Icons.Default.LightMode
                                DarkThemeConfig.SYSTEM -> Icons.Outlined.BrightnessAuto
                            },
                            contentDescription = "Ganti Tema Tampilan"
                        )
                    }

                    // Sound switch
                    IconButton(onClick = { viewModel.toggleSoundMute() }) {
                        Icon(
                            if (isSoundMuted) Icons.Outlined.VolumeOff else Icons.Outlined.VolumeUp,
                            contentDescription = if (isSoundMuted) "Suara Senyap" else "Suara Aktif"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    showCreateDialog = true
                    SoundManager.playClick()
                },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = {
                    Text(
                        "Buka Notebook Baru",
                        fontFamily = QuicksandFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                },
                containerColor = CoklatMudaWarm,
                contentColor = Color.White,
                modifier = Modifier.testTag("btn_create_journal")
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Hero Banner Bernuansa Coklat Muda Hangat & Soft Blue
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, CoklatMudaKaramel.copy(alpha = 0.4f)),
                shadowElevation = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    CoklatMudaAlmond.copy(alpha = 0.45f),
                                    CoklatMudaKrim.copy(alpha = 0.65f),
                                    SoftBlueAccent.copy(alpha = 0.20f)
                                )
                            )
                        )
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Rak Buku Notebook Kamu",
                                    fontFamily = PlayfairFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "Sentuh salah satu buku untuk mulai mendekorasi lembaran jurnalmu",
                                    fontFamily = QuicksandFontFamily,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = CoklatMudaWarm.copy(alpha = 0.18f),
                                border = BorderStroke(0.8.dp, CoklatMudaWarm.copy(alpha = 0.4f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Outlined.Book,
                                        contentDescription = null,
                                        tint = CoklatMudaWarm,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        text = "${journals.size} Notebook Tersimpan",
                                        fontFamily = QuicksandFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = CoklatMudaWarm
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = SoftBlueAccent.copy(alpha = 0.25f),
                                border = BorderStroke(0.8.dp, SoftBlueAccent.copy(alpha = 0.5f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Outlined.Palette,
                                        contentDescription = null,
                                        tint = Color(0xFF3A86C8),
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        text = "Coklat Muda & Biru Lembut",
                                        fontFamily = QuicksandFontFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 11.sp,
                                        color = Color(0xFF28659E)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Theme Category Filter Row (Dengan Ikon Vektor Bersih tanpa Emoji)
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    val isAll = selectedThemeFilter == "ALL"
                    FilterChip(
                        selected = isAll,
                        onClick = {
                            selectedThemeFilter = "ALL"
                            SoundManager.playClick()
                        },
                        border = BorderStroke(1.dp, if (isAll) CoklatMudaWarm else CoklatMudaKaramel.copy(alpha = 0.4f)),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CoklatMudaWarm.copy(alpha = 0.22f),
                            selectedLabelColor = MaterialTheme.colorScheme.onSurface
                        ),
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Outlined.AutoAwesome,
                                    contentDescription = null,
                                    modifier = Modifier.size(13.dp),
                                    tint = if (isAll) CoklatMudaWarm else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Semua Notebook", fontFamily = QuicksandFontFamily, fontSize = 12.sp)
                            }
                        }
                    )
                }

                items(JournalTheme.entries) { theme ->
                    val isSelected = selectedThemeFilter == theme.id
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedThemeFilter = theme.id
                            SoundManager.playClick()
                        },
                        border = BorderStroke(1.dp, if (isSelected) theme.accentColor else CoklatMudaKaramel.copy(alpha = 0.4f)),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = theme.accentColor.copy(alpha = 0.20f),
                            selectedLabelColor = MaterialTheme.colorScheme.onSurface
                        ),
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                ThemeVectorIcon(
                                    theme = theme,
                                    modifier = Modifier.size(13.dp),
                                    tint = if (isSelected) theme.accentColor else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    theme.title.split(" ").first(),
                                    fontFamily = QuicksandFontFamily,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    )
                }
            }

            val filteredList = remember(journals, selectedThemeFilter) {
                if (selectedThemeFilter == "ALL") journals
                else journals.filter { it.theme == selectedThemeFilter }
            }

            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(CoklatMudaAlmond.copy(alpha = 0.5f))
                                .border(1.dp, CoklatMudaWarm.copy(alpha = 0.4f), RoundedCornerShape(18.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.MenuBook,
                                contentDescription = null,
                                tint = CoklatMudaWarm,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Belum ada buku notebook di kategori ini",
                            fontFamily = PlayfairFontFamily,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Buka notebook pertamamu dan hias lembarannya dengan stiker estetik",
                            fontFamily = QuicksandFontFamily,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.animateContentSize(
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium)
                    )
                ) {
                    items(filteredList, key = { it.id }) { journal ->
                        JournalNotebookCard(
                            journal = journal,
                            onOpen = { viewModel.openJournal(journal.id) },
                            onDelete = { journalToDelete = journal }
                        )
                    }
                }
            }
        }
    }

    // Dialog Pembuatan Jurnal Baru (Buku Notebook)
    if (showCreateDialog) {
        CreateJournalDialog(
            onDismiss = { showCreateDialog = false },
            onCreate = { title, theme, paper, palette ->
                viewModel.createJournal(title, theme, paper, palette)
                showCreateDialog = false
            }
        )
    }

    // Dialog Konfirmasi Hapus
    journalToDelete?.let { target ->
        AlertDialog(
            onDismissRequest = { journalToDelete = null },
            title = {
                Text(
                    text = "Hapus Buku Notebook?",
                    fontFamily = PlayfairFontFamily,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Buku notebook '${target.title}' beserta seluruh stiker lembaran dan riwayat kolaborasi akan dihapus.",
                    fontFamily = QuicksandFontFamily
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteJournal(target)
                        journalToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                TextButton(onClick = { journalToDelete = null }) {
                    Text("Batal")
                }
            }
        )
    }
}

/**
 * Kartu Representasi Buku Notebook Jurnal Fisik (Bukan sekadar lembar catatan lepas)
 * Dilengkapi spine jilid coklat muda berjahit, preview sampul, dan tag warna lembut.
 */
@Composable
fun JournalNotebookCard(
    journal: JournalEntity,
    onOpen: () -> Unit,
    onDelete: () -> Unit
) {
    val theme = remember(journal.theme) { JournalTheme.fromId(journal.theme) }
    val paper = remember(journal.paperStyle) { PaperStyle.fromId(journal.paperStyle) }
    val palette = remember(journal.colorPalette) { PaletteType.fromId(journal.colorPalette) }

    val formattedDate = remember(journal.updatedAt) {
        val sdf = SimpleDateFormat("d MMMM yyyy", Locale("id", "ID"))
        sdf.format(Date(journal.updatedAt))
    }

    Card(
        onClick = onOpen,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, CoklatMudaKaramel.copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("journal_card_${journal.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Mini Buku Notebook dengan Spine Jilid Berjahit Coklat Muda
            Box(
                modifier = Modifier
                    .width(58.dp)
                    .height(68.dp)
                    .shadow(elevation = 3.dp, shape = RoundedCornerShape(topStart = 3.dp, bottomStart = 3.dp, topEnd = 8.dp, bottomEnd = 8.dp))
                    .clip(RoundedCornerShape(topStart = 3.dp, bottomStart = 3.dp, topEnd = 8.dp, bottomEnd = 8.dp))
                    .background(Color(journal.coverColorHex))
            ) {
                // Halaman buku terlihat di sisi kanan
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(6.dp)
                        .align(Alignment.CenterEnd)
                        .background(paper.lightPaperBg)
                )

                // Spine/Jilid notebook di sisi kiri bergaris jahitan perunggu
                Canvas(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(14.dp)
                        .align(Alignment.CenterStart)
                        .background(CoklatMudaWarm.copy(alpha = 0.45f))
                ) {
                    val stitchStep = 8.dp.toPx()
                    var y = 6.dp.toPx()
                    while (y < size.height - 6.dp.toPx()) {
                        drawLine(
                            color = Color.White.copy(alpha = 0.7f),
                            start = Offset(7.dp.toPx(), y),
                            end = Offset(7.dp.toPx(), y + 4.dp.toPx()),
                            strokeWidth = 1.2f
                        )
                        y += stitchStep
                    }
                }

                // Ikon Tema Vektor di tengah sampul notebook
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 14.dp, end = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = com.example.ui.components.getThemeVectorIcon(theme),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Info Buku Notebook
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = journal.title,
                    fontFamily = PlayfairFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Chip Tema Coklat Muda / Aksen
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = CoklatMudaWarm.copy(alpha = 0.16f),
                        border = BorderStroke(0.6.dp, CoklatMudaWarm.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = theme.title.split(" ").first(),
                            fontFamily = QuicksandFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = CoklatMudaWarm,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    // Chip Kertas Notebook
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                    ) {
                        Text(
                            text = paper.title,
                            fontFamily = QuicksandFontFamily,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Badge Kolaborasi Soft Blue
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = SoftBlueAccent.copy(alpha = 0.22f),
                        border = BorderStroke(0.6.dp, SoftBlueAccent.copy(alpha = 0.45f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Outlined.Group, contentDescription = null, tint = Color(0xFF28659E), modifier = Modifier.size(11.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = journal.collaborationCode,
                                fontFamily = QuicksandFontFamily,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF28659E)
                            )
                        }
                    }

                    Text(
                        text = formattedDate,
                        fontFamily = QuicksandFontFamily,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }

            // Tombol Aksi Buka & Hapus
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDelete, modifier = Modifier.size(34.dp)) {
                    Icon(
                        Icons.Outlined.DeleteOutline,
                        contentDescription = "Hapus Buku",
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
                Icon(
                    Icons.AutoMirrored.Outlined.ArrowForward,
                    contentDescription = "Buka",
                    modifier = Modifier.size(18.dp),
                    tint = CoklatMudaWarm
                )
            }
        }
    }
}

@Composable
fun CreateJournalDialog(
    onDismiss: () -> Unit,
    onCreate: (title: String, theme: JournalTheme, paper: PaperStyle, palette: PaletteType) -> Unit
) {
    var titleInput by remember { mutableStateOf("") }
    var selectedTheme by remember { mutableStateOf(JournalTheme.CALM) }
    var selectedPaper by remember { mutableStateOf(JournalTheme.CALM.defaultPaper) }
    var selectedPalette by remember { mutableStateOf(JournalTheme.CALM.defaultPalette) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(CoklatMudaWarm.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.MenuBook,
                        contentDescription = null,
                        tint = CoklatMudaWarm,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Buka Notebook Baru",
                    fontFamily = PlayfairFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                OutlinedTextField(
                    value = titleInput,
                    onValueChange = { titleInput = it },
                    placeholder = { Text("Nama buku notebook (misal: Jurnal Musim Gugur)", fontSize = 12.sp, fontFamily = QuicksandFontFamily) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text("Pilih Tema Notebook:", fontFamily = QuicksandFontFamily, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 6.dp)
                ) {
                    items(JournalTheme.entries) { theme ->
                        val isSelected = selectedTheme == theme
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedTheme = theme
                                selectedPaper = theme.defaultPaper
                                selectedPalette = theme.defaultPalette
                                SoundManager.playClick()
                            },
                            border = BorderStroke(1.dp, if (isSelected) theme.accentColor else CoklatMudaKaramel.copy(alpha = 0.4f)),
                            label = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    ThemeVectorIcon(
                                        theme = theme,
                                        modifier = Modifier.size(13.dp),
                                        tint = if (isSelected) theme.accentColor else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(theme.title.split(" ").first(), fontFamily = QuicksandFontFamily, fontSize = 11.sp)
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text("Pilihan Pola Halaman Buku:", fontFamily = QuicksandFontFamily, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 6.dp)
                ) {
                    items(PaperStyle.entries) { paper ->
                        val isSelected = selectedPaper == paper
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedPaper = paper
                                SoundManager.playClick()
                            },
                            border = BorderStroke(1.dp, if (isSelected) CoklatMudaWarm else CoklatMudaKaramel.copy(alpha = 0.4f)),
                            label = { Text(paper.title, fontFamily = QuicksandFontFamily, fontSize = 11.sp) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text("Pilihan Palet Warna:", fontFamily = QuicksandFontFamily, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 6.dp)
                ) {
                    items(PaletteType.entries) { palette ->
                        val isSelected = selectedPalette == palette
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedPalette = palette
                                SoundManager.playClick()
                            },
                            border = BorderStroke(1.dp, if (isSelected) CoklatMudaWarm else CoklatMudaKaramel.copy(alpha = 0.4f)),
                            label = { Text(palette.title, fontFamily = QuicksandFontFamily, fontSize = 11.sp) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val finalTitle = titleInput.ifBlank { "Notebook ${selectedTheme.title.split(" ").first()}" }
                    onCreate(finalTitle, selectedTheme, selectedPaper, selectedPalette)
                },
                colors = ButtonDefaults.buttonColors(containerColor = CoklatMudaWarm),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Mulai Menghias", fontFamily = QuicksandFontFamily, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal", fontFamily = QuicksandFontFamily)
            }
        }
    )
}
