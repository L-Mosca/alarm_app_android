package br.com.alarm.app.domain.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.alarm.app.domain.models.alarm.AlarmItem

@Dao
interface AlarmDatabaseDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alarmItem: AlarmItem): Long?

    @Query("SELECT * FROM AlarmItem")
    suspend fun getAlarmList(): List<AlarmItem>

    @Query("SELECT * FROM AlarmItem WHERE id = :id")
    suspend fun getAlarmDetail(id: Long): AlarmItem

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAlarm(alarmItem: AlarmItem)

    @Query("DELETE FROM AlarmItem WHERE id = :alarmId")
    suspend fun deleteAlarm(alarmId: Long)
}