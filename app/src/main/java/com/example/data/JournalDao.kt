package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {
    // Journal queries
    @Query("SELECT * FROM journals ORDER BY updatedAt DESC")
    fun getAllJournals(): Flow<List<JournalEntity>>

    @Query("SELECT * FROM journals WHERE id = :id LIMIT 1")
    fun getJournalById(id: Long): Flow<JournalEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJournal(journal: JournalEntity): Long

    @Update
    suspend fun updateJournal(journal: JournalEntity)

    @Delete
    suspend fun deleteJournal(journal: JournalEntity)

    // Elements queries
    @Query("SELECT * FROM journal_elements WHERE journalId = :journalId ORDER BY zIndex ASC")
    fun getElementsForJournal(journalId: Long): Flow<List<JournalElementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElement(element: JournalElementEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElements(elements: List<JournalElementEntity>)

    @Update
    suspend fun updateElement(element: JournalElementEntity)

    @Delete
    suspend fun deleteElement(element: JournalElementEntity)

    @Query("DELETE FROM journal_elements WHERE journalId = :journalId")
    suspend fun deleteAllElementsForJournal(journalId: Long)

    // Collaborator queries
    @Query("SELECT * FROM collaborators WHERE journalId = :journalId ORDER BY joinedAt ASC")
    fun getCollaboratorsForJournal(journalId: Long): Flow<List<CollaboratorEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollaborator(collaborator: CollaboratorEntity): Long

    @Update
    suspend fun updateCollaborator(collaborator: CollaboratorEntity)

    @Delete
    suspend fun deleteCollaborator(collaborator: CollaboratorEntity)

    // Activity queries
    @Query("SELECT * FROM activities WHERE journalId = :journalId ORDER BY timestamp DESC LIMIT 30")
    fun getActivitiesForJournal(journalId: Long): Flow<List<ActivityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: ActivityEntity)

    // Custom stickers queries
    @Query("SELECT * FROM custom_stickers ORDER BY createdAt DESC")
    fun getAllCustomStickers(): Flow<List<CustomStickerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomSticker(sticker: CustomStickerEntity): Long

    @Delete
    suspend fun deleteCustomSticker(sticker: CustomStickerEntity)
}
