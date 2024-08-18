package br.com.alarm.app.domain.service.media_player

import android.content.Context
import android.net.Uri

interface MediaPlayerContract {

    fun init(context: Context, uri: Uri)

    fun stop()
}