package br.com.alarm.app.domain.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.alarm.app.domain.models.alarm.AlarmItem

@Database(entities = [AlarmItem::class], version = 2)
@TypeConverters(Converters::class)
abstract class AlarmDatabase : RoomDatabase() {

    abstract fun alarmDao(): AlarmDatabaseDAO

    companion object {
        @Volatile
        private var INSTANCE: AlarmDatabase? = null

        fun getDatabase(context: Context): AlarmDatabase {
            return if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AlarmDatabase::class.java,
                        "AlarmApp.Database"
                    ).fallbackToDestructiveMigration().build()
                }
                INSTANCE as AlarmDatabase
            } else {
                INSTANCE as AlarmDatabase
            }
        }
    }
}