package br.com.alarm.app.domain.database

import android.net.Uri
import androidx.room.TypeConverter
import br.com.alarm.app.domain.models.alarm.WeekDays
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


object Converters {

    @TypeConverter
    fun fromWeekDays(weekDays: WeekDays?): String? {
        weekDays?.let {
            val type = object : TypeToken<WeekDays>() {}.type
            return Gson().toJson(weekDays, type)
        }

        return null
    }

    @TypeConverter
    fun toWeekDays(weekDaysString: String?): WeekDays? {
        weekDaysString?.let {
            val type = object : TypeToken<WeekDays>() {}.type
            return Gson().fromJson(weekDaysString, type)
        }

        return null
    }

    @TypeConverter
    fun fromUri(uri: Uri?): String? = uri?.toString()

    @TypeConverter
    fun toUri(uriString: String?): Uri? = uriString?.let { Uri.parse(it) }
}