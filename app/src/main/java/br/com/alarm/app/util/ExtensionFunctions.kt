package br.com.alarm.app.util

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.os.Handler
import android.os.Looper
import br.com.alarm.app.domain.models.alarm.Day

fun executeDelayed(delay: Long = 200L, runnable: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed({
        runnable.invoke()
    }, delay)
}

fun List<String>.removeBrackets() = this.toString().trim().replace("[", "").replace("]", "")

fun List<Day>.getResumeWeekDays() = this.filter { it.isEnable }.map { it.dayName.substring(0, 3) }

fun Uri?.getRingToneTitle(context: Context): String {
    val ringtone = RingtoneManager.getRingtone(context, this)
    return ringtone.getTitle(context)
}