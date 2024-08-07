package br.com.alarm.app.util.ringtone_helper

import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import br.com.alarm.app.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class RingtoneHelper @Inject constructor(@ApplicationContext private val context: Context) :
    RingtoneHelperContract {

    override fun buildRingtoneIntent(currentRingtone: Uri?): Intent {
        val ringtone = currentRingtone ?: RingtoneManager.getActualDefaultRingtoneUri(
            context,
            RingtoneManager.TYPE_ALARM
        )

        val screenTitle = context.getString(R.string.alarm_sound)

        val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, screenTitle)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, ringtone)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
        return intent
    }

    override fun getRingToneTitle(uri: Uri): String {
        val ringtone = RingtoneManager.getRingtone(context, uri)
        val title = ringtone.getTitle(context)
        return title
    }

    override fun getDefaultRingtone(): Uri =
        RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM)

}