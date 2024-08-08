package br.com.alarm.app.util.ringtone_helper

import android.content.Intent
import android.net.Uri
import br.com.alarm.app.domain.models.alarm.AlarmItem

interface RingtoneHelperContract {

    fun buildRingtoneIntent(currentRingtone: Uri?): Intent

    fun getRingToneTitle(uri: Uri): String

    fun getDefaultRingtone(): Uri
}