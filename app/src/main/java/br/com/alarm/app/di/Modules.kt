package br.com.alarm.app.di

import android.content.Context
import br.com.alarm.app.domain.database.AlarmDatabase
import br.com.alarm.app.domain.database.AlarmDatabaseDAO
import br.com.alarm.app.domain.repositories.AlarmRepository
import br.com.alarm.app.domain.repositories.AlarmRepositoryContract
import br.com.alarm.app.domain.service.media_player.MediaPlayerContract
import br.com.alarm.app.domain.service.media_player.MediaPlayerService
import br.com.alarm.app.util.ringtone_helper.RingtoneHelper
import br.com.alarm.app.util.ringtone_helper.RingtoneHelperContract
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.intellij.lang.annotations.PrintFormat
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Modules {

    @Provides
    @Singleton
    fun provideMediaPlayerService(): MediaPlayerContract {
        return MediaPlayerService()
    }

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AlarmDatabase {
        return AlarmDatabase.getDatabase(context)
    }

    @Provides
    fun provideDatabaseDao(database: AlarmDatabase): AlarmDatabaseDAO {
        return database.alarmDao()
    }

    @Provides
    @Singleton
    fun bindRingtoneHelper(ringtoneHelper: RingtoneHelper): RingtoneHelperContract = ringtoneHelper

    @Provides
    @Singleton
    fun bindAlarmRepository(alarmRepository: AlarmRepository): AlarmRepositoryContract =
        alarmRepository
}