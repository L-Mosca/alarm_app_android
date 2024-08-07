package br.com.alarm.app.di

import br.com.alarm.app.domain.repositories.AlarmRepository
import br.com.alarm.app.domain.repositories.AlarmRepositoryContract
import br.com.alarm.app.util.ringtone_helper.RingtoneHelper
import br.com.alarm.app.util.ringtone_helper.RingtoneHelperContract
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.intellij.lang.annotations.PrintFormat
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Modules {

    @Provides
    @Singleton
    fun bindRingtoneHelper(ringtoneHelper: RingtoneHelper): RingtoneHelperContract = ringtoneHelper

    @Provides
    @Singleton
    fun bindAlarmRepository(alarmRepository: AlarmRepository): AlarmRepositoryContract =
        alarmRepository
}