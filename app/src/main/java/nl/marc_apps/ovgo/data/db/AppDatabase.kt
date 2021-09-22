package nl.marc_apps.ovgo.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [TrainStationEntity::class], version = AppDatabase.VERSION)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trainStationDao(): TrainStationDao

    companion object {
        const val VERSION = 1
    }
}
