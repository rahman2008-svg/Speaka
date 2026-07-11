package com.example.data

import kotlinx.coroutines.flow.Flow

class SpeakRepository(private val speakDao: SpeakDao) {
    val allItems: Flow<List<SpeakItem>> = speakDao.getAllItems()
    val favoriteItems: Flow<List<SpeakItem>> = speakDao.getFavoriteItems()

    suspend fun insertItem(item: SpeakItem) {
        speakDao.insertItem(item)
    }

    suspend fun deleteItem(item: SpeakItem) {
        speakDao.deleteItem(item)
    }

    suspend fun deleteItemById(id: Int) {
        speakDao.deleteItemById(id)
    }

    suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean) {
        speakDao.updateFavoriteStatus(id, isFavorite)
    }

    suspend fun clearHistoryOnly() {
        speakDao.clearHistoryOnly()
    }

    suspend fun clearAll() {
        speakDao.clearAll()
    }
}
