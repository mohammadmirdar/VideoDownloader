package com.mirdar.videodownloader.data.download.cache

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mirdar.videodownloader.data.download.cache.model.CacheDownload
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadDao {

    @Query("SELECT * FROM ${CacheDownload.TABLE_NAME} ORDER BY ${CacheDownload.COLUMN_DOWNLOAD_ID}")
    fun readAllDownloads(): Flow<List<CacheDownload>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cacheDownload: CacheDownload)

    @Delete
    suspend fun delete(cacheDownload: CacheDownload)

    @Query("DELETE FROM ${CacheDownload.TABLE_NAME}")
    suspend fun clearAll()

    @Query(
        "UPDATE ${CacheDownload.TABLE_NAME}" +
                " SET ${CacheDownload.COLUMN_DOWNLOAD_COMPLETED} = :isCompleted " +
                "WHERE ${CacheDownload.COLUMN_DOWNLOAD_ID} = :id"
    )
    suspend fun setCompleted(isCompleted: Boolean = true, id: String)
}