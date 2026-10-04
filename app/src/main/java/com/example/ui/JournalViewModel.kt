package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import com.example.model.*
import com.example.util.SoundManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

sealed class AppScreen {
    object JournalList : AppScreen()
    data class NotebookCanvas(val journalId: Long) : AppScreen()
}

enum class DarkThemeConfig {
    SYSTEM, LIGHT, DARK
}

class JournalViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val repository = JournalRepository(database.journalDao())

    val allJournals: StateFlow<List<JournalEntity>> = repository.getAllJournals()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentScreen = MutableStateFlow<AppScreen>(AppScreen.JournalList)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _activeJournalId = MutableStateFlow<Long?>(null)
    val activeJournalId: StateFlow<Long?> = _activeJournalId.asStateFlow()

    val activeJournal: StateFlow<JournalEntity?> = _activeJournalId.flatMapLatest { id ->
        if (id == null) flowOf(null)
        else repository.getJournalById(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val activeElements: StateFlow<List<JournalElementEntity>> = _activeJournalId.flatMapLatest { id ->
        if (id == null) flowOf(emptyList())
        else repository.getElementsForJournal(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeCollaborators: StateFlow<List<CollaboratorEntity>> = _activeJournalId.flatMapLatest { id ->
        if (id == null) flowOf(emptyList())
        else repository.getCollaboratorsForJournal(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeActivities: StateFlow<List<ActivityEntity>> = _activeJournalId.flatMapLatest { id ->
        if (id == null) flowOf(emptyList())
        else repository.getActivitiesForJournal(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val customStickers: StateFlow<List<CustomStickerEntity>> = repository.getAllCustomStickers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedElementId = MutableStateFlow<String?>(null)
    val selectedElementId: StateFlow<String?> = _selectedElementId.asStateFlow()

    // Mode Simulasi Kolaborasi: apakah user sedang beraksi sebagai "Bisa Edit" atau "Lihat Saja"
    private val _simulatedViewerMode = MutableStateFlow(false)
    val simulatedViewerMode: StateFlow<Boolean> = _simulatedViewerMode.asStateFlow()

    // Pengaturan Tema Gelap
    private val _darkThemeConfig = MutableStateFlow(DarkThemeConfig.SYSTEM)
    val darkThemeConfig: StateFlow<DarkThemeConfig> = _darkThemeConfig.asStateFlow()

    // Pengaturan Suara
    private val _isSoundMuted = MutableStateFlow(SoundManager.isMuted())
    val isSoundMuted: StateFlow<Boolean> = _isSoundMuted.asStateFlow()

    // Floating reaction trigger (emoji string + random offset)
    private val _floatingReactions = MutableStateFlow<List<Pair<String, Long>>>(emptyList())
    val floatingReactions: StateFlow<List<Pair<String, Long>>> = _floatingReactions.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllJournals().first().let { list ->
                if (list.isEmpty()) {
                    seedInitialData()
                }
            }
        }
    }

    private suspend fun seedInitialData() {
        val initialJournal = JournalEntity(
            title = "Jurnal Tenang Hari Ini",
            theme = JournalTheme.CALM.id,
            paperStyle = PaperStyle.DOTS.id,
            colorPalette = PaletteType.CALM_LATTE.id,
            coverColorHex = 0xFFC59B76,
            collaborationCode = "OJ-2026-CALM",
            isShared = true,
            currentUserRole = "OWNER"
        )
        val journalId = repository.insertJournal(initialJournal)

        // Stiker pembuka bernuansa kalem (coklat susu, soft blue, cream)
        val elements = listOf(
            JournalElementEntity(
                id = UUID.randomUUID().toString(),
                journalId = journalId,
                type = "WASHI_TAPE",
                content = "Soft Blue & Susu",
                colorHex = 0xFFA2D2FF,
                secondaryColorHex = 0xFFC59B76,
                posX = 0.22f,
                posY = 0.08f,
                scale = 1.0f,
                rotation = -3f,
                zIndex = 1,
                addedByName = "Ayu"
            ),
            JournalElementEntity(
                id = UUID.randomUUID().toString(),
                journalId = journalId,
                type = "STICKER",
                content = "coffee_cup",
                colorHex = 0xFFC59B76,
                posX = 0.14f,
                posY = 0.16f,
                scale = 1.35f,
                rotation = -8f,
                zIndex = 2,
                addedByName = "Ayu"
            ),
            JournalElementEntity(
                id = UUID.randomUUID().toString(),
                journalId = journalId,
                type = "TEXT",
                content = "Selamat datang di OurJour\nRuang tenang mendekorasi notebook & kolaborasi.",
                colorHex = 0xFF332D28,
                posX = 0.48f,
                posY = 0.25f,
                scale = 1.05f,
                rotation = 1f,
                fontFamilyName = "SACRAMENTO",
                fontSize = 26,
                zIndex = 3,
                addedByName = "Kamu"
            ),
            JournalElementEntity(
                id = UUID.randomUUID().toString(),
                journalId = journalId,
                type = "STICKY_NOTE",
                content = "Buku Notebook Jurnal:\n• Halaman bersampul coklat susu & soft blue\n• Hias dengan stiker original & pita washi\n• Ajak teman kolaborasi di buku ini!",
                colorHex = 0xFFFAF4EB,
                secondaryColorHex = 0xFF5C4033,
                posX = 0.32f,
                posY = 0.52f,
                scale = 1.0f,
                rotation = -2f,
                fontFamilyName = "QUICKSAND",
                fontSize = 15,
                zIndex = 4,
                addedByName = "Budi"
            ),
            JournalElementEntity(
                id = UUID.randomUUID().toString(),
                journalId = journalId,
                type = "STICKER",
                content = "fluffy_cloud",
                colorHex = 0xFFA2D2FF,
                posX = 0.78f,
                posY = 0.46f,
                scale = 1.3f,
                rotation = 6f,
                zIndex = 5,
                addedByName = "Citra"
            ),
            JournalElementEntity(
                id = UUID.randomUUID().toString(),
                journalId = journalId,
                type = "STICKER",
                content = "botanical_sprig",
                colorHex = 0xFF8EAF9D,
                posX = 0.76f,
                posY = 0.74f,
                scale = 1.35f,
                rotation = -5f,
                zIndex = 6,
                addedByName = "Ayu"
            )
        )
        repository.insertElements(elements)

        // Tambah kolaborator contoh
        repository.addCollaborator(
            CollaboratorEntity(
                journalId = journalId,
                name = "Ayu Lestari",
                email = "ayu@estetik.id",
                avatarColorHex = 0xFFFFCAD4,
                permission = "CAN_EDIT",
                isOnline = true
            )
        )
        repository.addCollaborator(
            CollaboratorEntity(
                journalId = journalId,
                name = "Budi Prakoso",
                email = "budi@jurnal.me",
                avatarColorHex = 0xFFB5EAD7,
                permission = "CAN_EDIT",
                isOnline = false
            )
        )
        repository.addCollaborator(
            CollaboratorEntity(
                journalId = journalId,
                name = "Citra Dewi",
                email = "citra@seni.co",
                avatarColorHex = 0xFFD0C3F0,
                permission = "CAN_VIEW",
                isOnline = true
            )
        )

        // Log riwayat
        repository.logActivity(
            ActivityEntity(
                journalId = journalId,
                authorName = "Ayu",
                actionText = "menempelkan Stiker Daun Maple & Washi Tartan"
            )
        )
        repository.logActivity(
            ActivityEntity(
                journalId = journalId,
                authorName = "Budi",
                actionText = "menambahkan Memo Ide Dekor"
            )
        )
    }

    fun openJournal(journalId: Long) {
        _activeJournalId.value = journalId
        _selectedElementId.value = null
        _simulatedViewerMode.value = false
        _currentScreen.value = AppScreen.NotebookCanvas(journalId)
        SoundManager.playPageFlip()
    }

    fun backToJournalList() {
        _selectedElementId.value = null
        _currentScreen.value = AppScreen.JournalList
        SoundManager.playClick()
    }

    fun createJournal(
        title: String,
        theme: JournalTheme,
        paperStyle: PaperStyle,
        palette: PaletteType
    ) {
        viewModelScope.launch {
            val newJournal = JournalEntity(
                title = title.ifBlank { "Lembaran Baru" },
                theme = theme.id,
                paperStyle = paperStyle.id,
                colorPalette = palette.id,
                coverColorHex = theme.accentColor.value.toLong(),
                collaborationCode = "RJ-${(1000..9999).random()}-${theme.id.take(4)}"
            )
            val id = repository.insertJournal(newJournal)
            openJournal(id)
        }
    }

    fun deleteJournal(journal: JournalEntity) {
        viewModelScope.launch {
            repository.deleteJournal(journal)
            SoundManager.playDelete()
            if (_activeJournalId.value == journal.id) {
                backToJournalList()
            }
        }
    }

    fun updateJournalPaperStyle(paperStyle: PaperStyle) {
        val current = activeJournal.value ?: return
        viewModelScope.launch {
            repository.updateJournal(current.copy(paperStyle = paperStyle.id, updatedAt = System.currentTimeMillis()))
            SoundManager.playPageFlip()
        }
    }

    fun updateJournalPalette(palette: PaletteType) {
        val current = activeJournal.value ?: return
        viewModelScope.launch {
            repository.updateJournal(current.copy(colorPalette = palette.id, updatedAt = System.currentTimeMillis()))
            SoundManager.playClick()
        }
    }

    fun updateJournalTheme(theme: JournalTheme) {
        val current = activeJournal.value ?: return
        viewModelScope.launch {
            repository.updateJournal(
                current.copy(
                    theme = theme.id,
                    paperStyle = theme.defaultPaper.id,
                    colorPalette = theme.defaultPalette.id,
                    updatedAt = System.currentTimeMillis()
                )
            )
            SoundManager.playPageFlip()
        }
    }

    fun selectElement(elementId: String?) {
        _selectedElementId.value = elementId
        if (elementId != null) {
            SoundManager.playClick()
        }
    }

    fun addSticker(stickerKey: String, name: String, colorHex: Long? = null) {
        val journalId = _activeJournalId.value ?: return
        val currentElements = activeElements.value
        val nextZ = (currentElements.maxOfOrNull { it.zIndex } ?: 0) + 1
        val chosenColor = colorHex ?: 0xFFC86D51

        viewModelScope.launch {
            val newElement = JournalElementEntity(
                journalId = journalId,
                type = "STICKER",
                content = stickerKey,
                colorHex = chosenColor,
                posX = (0.25f + Math.random().toFloat() * 0.45f).coerceIn(0.15f, 0.75f),
                posY = (0.25f + Math.random().toFloat() * 0.45f).coerceIn(0.15f, 0.75f),
                scale = 1.25f,
                rotation = (-15..15).random().toFloat(),
                zIndex = nextZ,
                addedByName = "Kamu"
            )
            repository.insertElement(newElement)
            _selectedElementId.value = newElement.id
            SoundManager.playStick()
            repository.logActivity(
                ActivityEntity(
                    journalId = journalId,
                    authorName = "Kamu",
                    actionText = "menempelkan Stiker $name"
                )
            )
        }
    }

    fun addWashiTape(preset: WashiTapePreset) {
        val journalId = _activeJournalId.value ?: return
        val currentElements = activeElements.value
        val nextZ = (currentElements.maxOfOrNull { it.zIndex } ?: 0) + 1

        viewModelScope.launch {
            val newElement = JournalElementEntity(
                journalId = journalId,
                type = "WASHI_TAPE",
                content = preset.name,
                colorHex = preset.primaryColorHex,
                secondaryColorHex = preset.secondaryColorHex,
                posX = (0.20f + Math.random().toFloat() * 0.40f).coerceIn(0.10f, 0.70f),
                posY = (0.15f + Math.random().toFloat() * 0.60f).coerceIn(0.10f, 0.80f),
                scale = 1.0f,
                rotation = (-6..6).random().toFloat(),
                zIndex = nextZ,
                addedByName = "Kamu"
            )
            repository.insertElement(newElement)
            _selectedElementId.value = newElement.id
            SoundManager.playStick()
            repository.logActivity(
                ActivityEntity(
                    journalId = journalId,
                    authorName = "Kamu",
                    actionText = "menempelkan Pita Washi ${preset.name}"
                )
            )
        }
    }

    fun addStickyNote(text: String, colorHex: Long) {
        val journalId = _activeJournalId.value ?: return
        val currentElements = activeElements.value
        val nextZ = (currentElements.maxOfOrNull { it.zIndex } ?: 0) + 1

        viewModelScope.launch {
            val newElement = JournalElementEntity(
                journalId = journalId,
                type = "STICKY_NOTE",
                content = text.ifBlank { "Catatan baruku..." },
                colorHex = colorHex,
                secondaryColorHex = 0xFF5C4033,
                posX = 0.35f,
                posY = 0.40f,
                scale = 1.0f,
                rotation = (-4..4).random().toFloat(),
                fontFamilyName = "CAVEAT",
                fontSize = 18,
                zIndex = nextZ,
                addedByName = "Kamu"
            )
            repository.insertElement(newElement)
            _selectedElementId.value = newElement.id
            SoundManager.playStick()
            repository.logActivity(
                ActivityEntity(
                    journalId = journalId,
                    authorName = "Kamu",
                    actionText = "menempelkan Memo Tempel"
                )
            )
        }
    }

    fun addTextElement(text: String, fontFamily: String, fontSize: Int, colorHex: Long) {
        val journalId = _activeJournalId.value ?: return
        val currentElements = activeElements.value
        val nextZ = (currentElements.maxOfOrNull { it.zIndex } ?: 0) + 1

        viewModelScope.launch {
            val newElement = JournalElementEntity(
                journalId = journalId,
                type = "TEXT",
                content = text.ifBlank { "Tulis cerita di sini..." },
                colorHex = colorHex,
                posX = 0.30f,
                posY = 0.35f,
                scale = 1.0f,
                rotation = 0f,
                fontFamilyName = fontFamily,
                fontSize = fontSize,
                zIndex = nextZ,
                addedByName = "Kamu"
            )
            repository.insertElement(newElement)
            _selectedElementId.value = newElement.id
            SoundManager.playStick()
            repository.logActivity(
                ActivityEntity(
                    journalId = journalId,
                    authorName = "Kamu",
                    actionText = "menambahkan Tulisan estetik"
                )
            )
        }
    }

    fun addStamp(label: String, subtitle: String = "", colorHex: Long) {
        val journalId = _activeJournalId.value ?: return
        val currentElements = activeElements.value
        val nextZ = (currentElements.maxOfOrNull { it.zIndex } ?: 0) + 1

        val stampContent = if (subtitle.isNotBlank()) "$label • $subtitle" else label

        viewModelScope.launch {
            val newElement = JournalElementEntity(
                journalId = journalId,
                type = "STAMP",
                content = stampContent,
                colorHex = colorHex,
                posX = 0.40f,
                posY = 0.40f,
                scale = 1.1f,
                rotation = (-8..8).random().toFloat(),
                zIndex = nextZ,
                addedByName = "Kamu"
            )
            repository.insertElement(newElement)
            _selectedElementId.value = newElement.id
            SoundManager.playStick()
            repository.logActivity(
                ActivityEntity(
                    journalId = journalId,
                    authorName = "Kamu",
                    actionText = "membubuhkan Cap $label"
                )
            )
        }
    }

    fun updateElementPosition(elementId: String, newX: Float, newY: Float) {
        val element = activeElements.value.find { it.id == elementId } ?: return
        val clampedX = newX.coerceIn(0.02f, 0.90f)
        val clampedY = newY.coerceIn(0.02f, 0.90f)
        viewModelScope.launch {
            repository.updateElement(element.copy(posX = clampedX, posY = clampedY))
        }
        SoundManager.playDragTick()
    }

    fun updateElementScale(elementId: String, newScale: Float) {
        val element = activeElements.value.find { it.id == elementId } ?: return
        val clamped = newScale.coerceIn(0.6f, 2.5f)
        viewModelScope.launch {
            repository.updateElement(element.copy(scale = clamped))
        }
        SoundManager.playTransformTick()
    }

    fun updateElementRotation(elementId: String, newRotation: Float) {
        val element = activeElements.value.find { it.id == elementId } ?: return
        val clamped = newRotation.coerceIn(-180f, 180f)
        viewModelScope.launch {
            repository.updateElement(element.copy(rotation = clamped))
        }
        SoundManager.playTransformTick()
    }

    fun updateElementColor(elementId: String, colorHex: Long) {
        val element = activeElements.value.find { it.id == elementId } ?: return
        viewModelScope.launch {
            repository.updateElement(element.copy(colorHex = colorHex))
            SoundManager.playClick()
        }
    }

    fun updateElementFont(elementId: String, fontName: String) {
        val element = activeElements.value.find { it.id == elementId } ?: return
        viewModelScope.launch {
            repository.updateElement(element.copy(fontFamilyName = fontName))
            SoundManager.playClick()
        }
    }

    fun updateElementText(elementId: String, newText: String) {
        val element = activeElements.value.find { it.id == elementId } ?: return
        viewModelScope.launch {
            repository.updateElement(element.copy(content = newText))
        }
    }

    fun updateElementLength(elementId: String, newLength: Int) {
        val element = activeElements.value.find { it.id == elementId } ?: return
        val clamped = newLength.coerceIn(60, 360)
        viewModelScope.launch {
            repository.updateElement(element.copy(fontSize = clamped))
        }
        SoundManager.playTransformTick()
    }

    fun updateElementTransform(
        elementId: String,
        newScale: Float,
        newRotation: Float,
        newX: Float? = null,
        newY: Float? = null
    ) {
        val element = activeElements.value.find { it.id == elementId } ?: return
        val clampedScale = newScale.coerceIn(0.5f, 3.0f)
        val clampedRotation = ((newRotation + 180f) % 360f + 360f) % 360f - 180f
        val clampedX = newX?.coerceIn(0.02f, 0.90f) ?: element.posX
        val clampedY = newY?.coerceIn(0.02f, 0.90f) ?: element.posY
        viewModelScope.launch {
            repository.updateElement(
                element.copy(
                    scale = clampedScale,
                    rotation = clampedRotation,
                    posX = clampedX,
                    posY = clampedY
                )
            )
        }
    }

    fun createCustomSticker(
        name: String,
        shapeType: String,
        symbolType: String,
        monogramText: String,
        bgColorHex: Long,
        accentColorHex: Long
    ) {
        viewModelScope.launch {
            val sticker = CustomStickerEntity(
                name = name.ifBlank { "Lencana Kustom" },
                shapeType = shapeType,
                symbolType = symbolType,
                monogramText = monogramText,
                bgColorHex = bgColorHex,
                accentColorHex = accentColorHex
            )
            repository.insertCustomSticker(sticker)
            SoundManager.playReaction()
            addCustomSticker(sticker)
        }
    }

    fun addCustomSticker(sticker: CustomStickerEntity) {
        val key = "custom:${sticker.shapeType}:${sticker.symbolType}:${sticker.monogramText}:${sticker.bgColorHex}:${sticker.accentColorHex}"
        addSticker(key, sticker.name, sticker.bgColorHex)
    }

    fun deleteCustomSticker(sticker: CustomStickerEntity) {
        viewModelScope.launch {
            repository.deleteCustomSticker(sticker)
            SoundManager.playDelete()
        }
    }

    fun bringElementForward(elementId: String) {
        val element = activeElements.value.find { it.id == elementId } ?: return
        val maxZ = (activeElements.value.maxOfOrNull { it.zIndex } ?: 0) + 1
        viewModelScope.launch {
            repository.updateElement(element.copy(zIndex = maxZ))
            SoundManager.playClick()
        }
    }

    fun deleteElement(elementId: String) {
        val element = activeElements.value.find { it.id == elementId } ?: return
        viewModelScope.launch {
            repository.deleteElement(element)
            _selectedElementId.value = null
            SoundManager.playDelete()
        }
    }

    fun duplicateElement(elementId: String) {
        val element = activeElements.value.find { it.id == elementId } ?: return
        val maxZ = (activeElements.value.maxOfOrNull { it.zIndex } ?: 0) + 1
        viewModelScope.launch {
            val duplicate = element.copy(
                id = UUID.randomUUID().toString(),
                posX = (element.posX + 0.05f).coerceIn(0.05f, 0.85f),
                posY = (element.posY + 0.05f).coerceIn(0.05f, 0.85f),
                rotation = element.rotation + 5f,
                zIndex = maxZ,
                addedByName = "Kamu"
            )
            repository.insertElement(duplicate)
            _selectedElementId.value = duplicate.id
            SoundManager.playStick()
        }
    }

    // --- Kolaborasi: Pengelolaan Akses & Undangan Teman ---

    fun inviteCollaborator(name: String, email: String, permission: String) {
        val journalId = _activeJournalId.value ?: return
        val pastelAvatars = listOf(
            0xFFFFCAD4, 0xFFD0C3F0, 0xFFB5EAD7, 0xFFFFE699, 0xFFA0C4FF, 0xFFFFDAC1, 0xFFE2D4C9
        )
        viewModelScope.launch {
            val newCollab = CollaboratorEntity(
                journalId = journalId,
                name = name.ifBlank { "Teman Baru" },
                email = email.ifBlank { "teman@jurnal.me" },
                avatarColorHex = pastelAvatars.random(),
                permission = permission, // "CAN_EDIT" or "CAN_VIEW"
                isOnline = true
            )
            repository.addCollaborator(newCollab)
            SoundManager.playReaction()
            repository.logActivity(
                ActivityEntity(
                    journalId = journalId,
                    authorName = "Kamu",
                    actionText = "mengundang $name (${if (permission == "CAN_EDIT") "Bisa Edit" else "Lihat Saja"})"
                )
            )
        }
    }

    fun updateCollaboratorPermission(collaboratorId: Long, newPermission: String) {
        val collab = activeCollaborators.value.find { it.id == collaboratorId } ?: return
        viewModelScope.launch {
            repository.updateCollaborator(collab.copy(permission = newPermission))
            SoundManager.playClick()
            val journalId = _activeJournalId.value ?: return@launch
            repository.logActivity(
                ActivityEntity(
                    journalId = journalId,
                    authorName = "Kamu",
                    actionText = "mengubah izin ${collab.name} menjadi ${if (newPermission == "CAN_EDIT") "Bisa Edit" else "Lihat Saja"}"
                )
            )
        }
    }

    fun removeCollaborator(collaborator: CollaboratorEntity) {
        viewModelScope.launch {
            repository.deleteCollaborator(collaborator)
            SoundManager.playDelete()
        }
    }

    fun toggleSimulatedViewerMode() {
        _simulatedViewerMode.value = !_simulatedViewerMode.value
        SoundManager.playClick()
    }

    fun sendReaction(emoji: String) {
        SoundManager.playReaction()
        val now = System.currentTimeMillis()
        _floatingReactions.value = _floatingReactions.value + (emoji to now)
        // Auto-clean old reactions
        viewModelScope.launch {
            kotlinx.coroutines.delay(2500)
            _floatingReactions.value = _floatingReactions.value.filter { it.second != now }
        }
    }

    // --- Pengaturan Tema & Audio ---

    fun setDarkThemeConfig(config: DarkThemeConfig) {
        _darkThemeConfig.value = config
        SoundManager.playClick()
    }

    fun toggleSoundMute() {
        val muted = SoundManager.toggleMute()
        _isSoundMuted.value = muted
    }
}
