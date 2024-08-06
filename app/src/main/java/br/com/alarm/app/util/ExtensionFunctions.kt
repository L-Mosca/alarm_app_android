package br.com.alarm.app.util

import android.os.Handler
import android.os.Looper
import br.com.alarm.app.domain.alarm.Day
import java.util.Calendar

fun executeDelayed(delay: Long = 200L, runnable: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed({
        runnable.invoke()
    }, delay)
}

fun List<String>.removeBrackets() = this.toString().trim().replace("[", "").replace("]", "")

fun List<Day>.getResumeWeekDays() = this.filter { it.isEnable }.map { it.dayName.substring(0, 3) }


/**
 * Calculate difference between actual time (hour and minute) and time picker time (hour and minute)
 *
 * @return Pair(Hour, Minute) -> Difference between actual time and time picker time
 */
fun Pair<Int, Int>.getDifferenceTime(): Pair<Int, Int> {
    val calendar = Calendar.getInstance()
    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
    val currentMinute = calendar.get(Calendar.MINUTE)

    val tpHour = this.first
    val tpMinute = this.second

    val currentTimeInMinutes = currentHour * 60 + currentMinute
    var tpTimeInMinutes = tpHour * 60 + tpMinute

    if (tpTimeInMinutes < currentTimeInMinutes) {
        tpTimeInMinutes += 24 * 60
    }

    val timeDifferenceInMinutes = tpTimeInMinutes - currentTimeInMinutes

    val differenceHours = timeDifferenceInMinutes / 60
    val differenceMinutes = timeDifferenceInMinutes % 60

    return Pair(differenceHours, differenceMinutes)

}