package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "journals")
data class JournalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val theme: String, // HALLOWEEN, FALL, SNOW, SIMPLE, EARTH, PASTEL, NEON, RANDOM
    val paperStyle: String, // DOTS, GRID, LINES, BLANK, KRAFT, DARK
    val colorPalette: String, // EARTH, PASTEL, NEON, AUTUMN, HALLOWEEN, SNOW, MINIMAL
    val coverColorHex: Long,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val collaborationCode: String = "OJ-" + (1000..9999).random() + "-NOTE",
    val isShared: Boolean = false,
    val currentUserRole: String = "OWNER" // OWNER, EDITOR, VIEWER
)

@Entity(tableName = "journal_elements")
data class JournalElementEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val journalId: Long,
    val type: String, // STICKER, WASHI_TAPE, STICKY_NOTE, TEXT, POLAROID, STAMP
    val content: String,
    val colorHex: Long,
    val secondaryColorHex: Long = 0xFFFFFFFF,
    val posX: Float, // relative 0.0f - 1.0f
    val posY: Float,
    val scale: Float = 1.0f,
    val rotation: Float = 0f,
    val fontFamilyName: String = "CAVEAT", // SACRAMENTO, CAVEAT, INDIE_FLOWER, PLAYFAIR, QUICKSAND, MONO
    val fontSize: Int = 18,
    val zIndex: Int = 0,
    val addedByName: String = "Kamu"
)

@Entity(tableName = "collaborators")
data class CollaboratorEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val journalId: Long,
    val name: String,
    val email: String,
    val avatarColorHex: Long,
    val permission: String, // CAN_EDIT, CAN_VIEW
    val isOnline: Boolean = true,
    val joinedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "activities")
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val journalId: Long,
    val authorName: String,
    val actionText: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "custom_stickers")
data class CustomStickerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val shapeType: String, // STAMP, CIRCLE, WAX_SEAL, HEXAGON, BADGE, ARCH
    val symbolType: String, // COFFEE, BOTANICAL, DAISY, HEART, STAR, MOON, GHOST, CAT, BUTTERFLY, BOOK
    val monogramText: String = "",
    val bgColorHex: Long,
    val accentColorHex: Long,
    val createdAt: Long = System.currentTimeMillis()
)
