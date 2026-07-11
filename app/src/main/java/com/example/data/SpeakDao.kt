package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SpeakDao {
    @Query("SELECT * FROM speak_items ORDER BY timestamp DESC")
    fun getAllItems(): Flow<List<SpeakItem>>

    @Query("SELECT * FROM speak_items WHERE isFavorite = 1 ORDER BY timestamp DESC")
    fun getFavoriteItems(): Flow<List<SpeakItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: SpeakItem)

    @Delete
    suspend fun deleteItem(item: SpeakItem)

    @Query("DELETE FROM speak_items WHERE id = :id")
    suspend fun deleteItemById(id: Int)

    @Query("UPDATE speak_items SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean)

    @Query("DELETE FROM speak_items WHERE isFavorite = 0")
    suspend fun clearHistoryOnly()

    @Query("DELETE FROM speak_items")
    suspend fun clearAll()
}
