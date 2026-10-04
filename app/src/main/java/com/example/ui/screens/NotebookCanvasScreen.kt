package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.JournalElementEntity
import com.example.data.JournalEntity
import com.example.model.*
import com.example.ui.JournalViewModel
import com.example.ui.components.*
import com.example.ui.theme.PlayfairFontFamily
import com.example.ui.theme.QuicksandFontFamily
import com.example.util.SoundManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotebookCanvasScreen(
    viewModel: JournalViewModel,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier
) {
    val activeJournal by viewModel.activeJournal.collectAsState()
    val elements by viewModel.activeElements.collectAsState()
    val collaborators by viewModel.activeCollaborators.collectAsState()
    val activities by viewModel.activeActivities.collectAsState()
    val selectedElementId by viewModel.selectedElementId.collectAsState()
    val isSimulatedViewer by viewModel.simulatedViewerMode.collectAsState()
    val isSoundMuted by viewModel.isSoundMuted.collectAsState()
    val floatingReactions by viewModel.floatingReactions.collectAsState()

    var showDecorationSheet by remember { mutableStateOf(false) }
    var showCollaborationSheet by remember { mutableStateOf(false) }
    var showSpotifySheet by remember { mutableStateOf(false) }
    var activeSpotifyPlaylist by remember { mutableStateOf(com.example.util.SpotifyManager.JOURNAL_PLAYLISTS.first()) }
    var isCleanPreviewMode by remember { mutableStateOf(false) }
    var textEditTarget by remember { mutableStateOf<JournalElementEntity?>(null) }
    var editTextValue by remember { mutableStateOf("") }

    BackHandler {
        if (showDecorationSheet || showCollaborationSheet || showSpotifySheet) {
            showDecorationSheet = false
            showCollaborationSheet = false
            showSpotifySheet = false
        } else if (selectedElementId != null) {
            viewModel.selectElement(null)
        } else {
            viewModel.backToJournalList()
        }
    }

    val currentJournal = activeJournal ?: return

    val currentTheme = remember(currentJournal.theme) { JournalTheme.fromId(currentJournal.theme) }
    val currentPaper = remember(currentJournal.paperStyle) { PaperStyle.fromId(currentJournal.paperStyle) }
    val currentPalette = remember(currentJournal.colorPalette) { PaletteType.fromId(currentJournal.colorPalette) }

    val isReadOnly = isSimulatedViewer || currentJournal.currentUserRole == "VIEWER"
    val selectedElement = elements.find { it.id == selectedElementId }

    Scaffold(
        topBar = {
            if (!isCleanPreviewMode) {
                TopAppBar(
                    title = {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                ThemeVectorIcon(
                                    theme = currentTheme,
                                    modifier = Modifier.size(18.dp),
                                    tint = currentTheme.accentColor
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = currentJournal.title,
                                    fontFamily = PlayfairFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Text(
                                text = "${currentPaper.title} • ${currentPalette.title}",
                                fontFamily = QuicksandFontFamily,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { viewModel.backToJournalList() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali ke Daftar")
                        }
                    },
                    actions = {
                        // Tombol Kolaborasi dengan Avatar Teman
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                            modifier = Modifier
                                .clickable {
                                    showCollaborationSheet = true
                                    SoundManager.playClick()
                                }
                                .padding(end = 6.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Mini avatar circles of friends
                                Row(horizontalArrangement = Arrangement.spacedBy((-6).dp)) {
                                    collaborators.take(2).forEach { collab ->
                                        Box(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .clip(CircleShape)
                                                .background(Color(collab.avatarColorHex))
                                                .border(1.dp, MaterialTheme.colorScheme.surface, CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = collab.name.take(1).uppercase(),
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Kolaborasi",
                                    fontFamily = QuicksandFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        // Tombol Spotify Musik
                        IconButton(onClick = {
                            showSpotifySheet = true
                            SoundManager.playClick()
                        }) {
                            Icon(
                                Icons.Default.MusicNote,
                                contentDescription = "Musik Menjurnal Spotify",
                                tint = Color(0xFF1DB954)
                            )
                        }

                        // Toggle Sound
                        IconButton(onClick = { viewModel.toggleSoundMute() }) {
                            Icon(
                                if (isSoundMuted) Icons.Outlined.VolumeOff else Icons.Outlined.VolumeUp,
                                contentDescription = if (isSoundMuted) "Suara Senyap" else "Suara Aktif"
                            )
                        }

                        // Toggle Clean Preview (hide toolbars)
                        IconButton(onClick = {
                            isCleanPreviewMode = !isCleanPreviewMode
                            SoundManager.playClick()
                        }) {
                            Icon(
                                Icons.Outlined.Fullscreen,
                                contentDescription = "Mode Pratinjau Bersih"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        },
        floatingActionButton = {
            if (!isReadOnly && !isCleanPreviewMode && selectedElement == null) {
                ExtendedFloatingActionButton(
                    onClick = {
                        showDecorationSheet = true
                        SoundManager.playClick()
                    },
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = {
                        Text(
                            "Hias Lembaran",
                            fontFamily = QuicksandFontFamily,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.testTag("btn_decorate_fab")
                )
            }
        }
    ) { paddingValues ->
        BoxWithConstraints(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val density = LocalDensity.current
            val canvasWidthPx = with(density) { maxWidth.toPx() }
            val canvasHeightPx = with(density) { maxHeight.toPx() }

            // Kanvas lembaran notebook
            NotebookPaperCanvas(
                paperStyle = currentPaper,
                isDark = isDarkTheme,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        // Klik area kosong untuk membatalkan seleksi
                        viewModel.selectElement(null)
                    }
            ) {
                // Render semua elemen yang ditempel
                elements.forEach { elem ->
                    JournalCanvasItem(
                        element = elem,
                        isSelected = elem.id == selectedElementId,
                        isReadOnly = isReadOnly,
                        canvasWidthPx = canvasWidthPx,
                        canvasHeightPx = canvasHeightPx,
                        onSelect = { viewModel.selectElement(elem.id) },
                        onDrag = { newX, newY -> viewModel.updateElementPosition(elem.id, newX, newY) }
                    )
                }

                // Floating Reactions (Hati / Bintang melayang)
                FloatingReactionsOverlay(reactions = floatingReactions)

                // Indikator mode "Lihat Saja" jika sedang aktif
                if (isReadOnly) {
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                        shadowElevation = 6.dp,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Visibility,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Mode Tamu: Lihat Saja (Elemen Terkunci)",
                                fontFamily = QuicksandFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // Bar Reaksi Kolaborasi Estetik di bagian bawah (Vektor Stamped Badge)
                    Surface(
                        shape = RoundedCornerShape(28.dp),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f),
                        shadowElevation = 8.dp,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 24.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Reaksi:",
                                fontFamily = QuicksandFontFamily,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            REACTION_KEYS.forEach { (key, _) ->
                                StampedReactionBadge(
                                    reactionKey = key,
                                    size = 34.dp,
                                    modifier = Modifier.clickable { viewModel.sendReaction(key) }
                                )
                            }
                        }
                    }
                }

                // Floating Spotify Mini Pill (buka playlist lo-fi santai saat menjurnal)
                if (!isCleanPreviewMode && selectedElement == null && !isReadOnly) {
                    SpotifyCanvasPill(
                        currentPlaylist = activeSpotifyPlaylist,
                        onClick = {
                            showSpotifySheet = true
                            SoundManager.playClick()
                        },
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 16.dp, bottom = 24.dp)
                    )
                }

                // Tombol Keluar dari Clean Preview Mode jika aktif
                if (isCleanPreviewMode) {
                    IconButton(
                        onClick = { isCleanPreviewMode = false },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f), CircleShape)
                    ) {
                        Icon(Icons.Default.FullscreenExit, contentDescription = "Tutup Pratinjau")
                    }
                }

                // Bottom Transformation Bar saat stiker/elemen dipilih
                if (!isReadOnly && !isCleanPreviewMode && selectedElement != null) {
                    SelectedElementControlBar(
                        element = selectedElement,
                        currentPalette = currentPalette,
                        onScaleChange = { viewModel.updateElementScale(selectedElement.id, it) },
                        onRotationChange = { viewModel.updateElementRotation(selectedElement.id, it) },
                        onColorChange = { viewModel.updateElementColor(selectedElement.id, it) },
                        onFontChange = { viewModel.updateElementFont(selectedElement.id, it) },
                        onEditText = {
                            textEditTarget = selectedElement
                            editTextValue = selectedElement.content
                        },
                        onBringForward = { viewModel.bringElementForward(selectedElement.id) },
                        onDuplicate = { viewModel.duplicateElement(selectedElement.id) },
                        onDelete = { viewModel.deleteElement(selectedElement.id) },
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }
        }
    }

    // Modal Bottom Sheet Dekorasi
    if (showDecorationSheet) {
        DecorationBottomSheet(
            currentTheme = currentTheme,
            currentPalette = currentPalette,
            currentPaper = currentPaper,
            onDismiss = { showDecorationSheet = false },
            onAddSticker = { emoji, name, color -> viewModel.addSticker(emoji, name, color) },
            onAddWashiTape = { preset -> viewModel.addWashiTape(preset) },
            onAddStickyNote = { text, color -> viewModel.addStickyNote(text, color) },
            onAddText = { text, font, size, color -> viewModel.addTextElement(text, font, size, color) },
            onAddStamp = { title, subtitle, color -> viewModel.addStamp(title, subtitle, color) },
            onSelectPaper = { viewModel.updateJournalPaperStyle(it) },
            onSelectPalette = { viewModel.updateJournalPalette(it) },
            onSelectTheme = { viewModel.updateJournalTheme(it) }
        )
    }

    // Modal Bottom Sheet Kolaborasi
    if (showCollaborationSheet) {
        CollaborationSheet(
            journal = currentJournal,
            collaborators = collaborators,
            activities = activities,
            isViewerMode = isSimulatedViewer,
            onDismiss = { showCollaborationSheet = false },
            onInvite = { name, email, perm -> viewModel.inviteCollaborator(name, email, perm) },
            onUpdatePermission = { collabId, newPerm -> viewModel.updateCollaboratorPermission(collabId, newPerm) },
            onRemoveCollaborator = { viewModel.removeCollaborator(it) },
            onToggleViewerMode = { viewModel.toggleSimulatedViewerMode() }
        )
    }

    // Modal Bottom Sheet Spotify
    if (showSpotifySheet) {
        SpotifyMusicSheet(
            selectedPlaylist = activeSpotifyPlaylist,
            onSelectPlaylist = { activeSpotifyPlaylist = it },
            onDismiss = { showSpotifySheet = false }
        )
    }

    // Dialog Edit Teks Cepat
    textEditTarget?.let { target ->
        AlertDialog(
            onDismissRequest = { textEditTarget = null },
            title = {
                Text(
                    text = "Ubah Teks",
                    fontFamily = PlayfairFontFamily,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                OutlinedTextField(
                    value = editTextValue,
                    onValueChange = { editTextValue = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateElementText(target.id, editTextValue)
                        textEditTarget = null
                    }
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(onClick = { textEditTarget = null }) {
                    Text("Batal")
                }
            }
        )
    }
}
