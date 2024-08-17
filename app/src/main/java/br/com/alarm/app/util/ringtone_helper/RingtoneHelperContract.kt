package br.com.alarm.app.util.ringtone_helper

import android.content.Intent

interface RingtoneHelperContract {

    fun buildRingtoneIntent(currentRingtone: String?): Intent

    fun getRingToneTitle(uri: String): String

    fun getDefaultRingtone(): String
}