package com.example.data

import kotlinx.coroutines.flow.Flow

class JournalRepository(private val dao: JournalDao) {
    fun getAllJournals(): Flow<List<JournalEntity>> = dao.getAllJournals()

    fun getJournalById(id: Long): Flow<JournalEntity?> = dao.getJournalById(id)

    suspend fun insertJournal(journal: JournalEntity): Long = dao.insertJournal(journal)

    suspend fun updateJournal(journal: JournalEntity) = dao.updateJournal(journal)

    suspend fun deleteJournal(journal: JournalEntity) {
        dao.deleteAllElementsForJournal(journal.id)
        dao.deleteJournal(journal)
    }

    fun getElementsForJournal(journalId: Long): Flow<List<JournalElementEntity>> =
        dao.getElementsForJournal(journalId)

    suspend fun insertElement(element: JournalElementEntity) = dao.insertElement(element)

    suspend fun insertElements(elements: List<JournalElementEntity>) = dao.insertElements(elements)

    suspend fun updateElement(element: JournalElementEntity) = dao.updateElement(element)

    suspend fun deleteElement(element: JournalElementEntity) = dao.deleteElement(element)

    fun getCollaboratorsForJournal(journalId: Long): Flow<List<CollaboratorEntity>> =
        dao.getCollaboratorsForJournal(journalId)

    suspend fun addCollaborator(collaborator: CollaboratorEntity): Long =
        dao.insertCollaborator(collaborator)

    suspend fun updateCollaborator(collaborator: CollaboratorEntity) =
        dao.updateCollaborator(collaborator)

    suspend fun deleteCollaborator(collaborator: CollaboratorEntity) =
        dao.deleteCollaborator(collaborator)

    fun getActivitiesForJournal(journalId: Long): Flow<List<ActivityEntity>> =
        dao.getActivitiesForJournal(journalId)

    suspend fun logActivity(activity: ActivityEntity) = dao.insertActivity(activity)

    // Custom stickers
    fun getAllCustomStickers(): Flow<List<CustomStickerEntity>> = dao.getAllCustomStickers()

    suspend fun insertCustomSticker(sticker: CustomStickerEntity): Long =
        dao.insertCustomSticker(sticker)

    suspend fun deleteCustomSticker(sticker: CustomStickerEntity) =
        dao.deleteCustomSticker(sticker)
}
