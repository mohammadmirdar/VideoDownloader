package com.mirdar.videodownloader.data.download.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mirdar.videodownloader.data.download.cache.model.CacheDownload.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
class CacheDownload(

    @ColumnInfo(name = COLUMN_DOWNLOAD_ID)
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = COLUMN_DOWNLOAD_THUMBNAIL)
    val thumbnail: String,

    @ColumnInfo(name = COLUMN_DOWNLOAD_TITLE)
    val title: String,

    @ColumnInfo(name = COLUMN_DOWNLOAD_DESCRIPTION)
    val description: String
) {

    companion object {
        const val COLUMN_DOWNLOAD_ID = "id"
        const val COLUMN_DOWNLOAD_THUMBNAIL = "thumbnail"
        const val COLUMN_DOWNLOAD_TITLE = "title"
        const val COLUMN_DOWNLOAD_DESCRIPTION = "description"
        const val TABLE_NAME = "cache_download"
    }
}
