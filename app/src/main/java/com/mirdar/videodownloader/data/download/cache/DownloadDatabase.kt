package com.mirdar.videodownloader.data.download.cache

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [

    ],
    version = 1
)
abstract class DownloadDatabase: RoomDatabase() {

    abstract fun downloadDao(): DownloadDao

    companion object {
        const val DATABASE_NAME = "download.db"
    }
}