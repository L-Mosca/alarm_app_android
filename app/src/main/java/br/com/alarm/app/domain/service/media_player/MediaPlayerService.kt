package br.com.alarm.app.domain.service.media_player

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import javax.inject.Inject

class MediaPlayerService @Inject constructor() : MediaPlayerContract {

    private var mediaPlayer: MediaPlayer? = null

    override fun init(context: Context, uri: Uri) {
        mediaPlayer?.release()

        if (mediaPlayer == null) mediaPlayer = MediaPlayer()

        mediaPlayer?.setDataSource(context, uri)
        mediaPlayer?.prepare()
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
    }

    override fun stop () {
        mediaPlayer?.let {
            if (it.isPlaying) it.stop()
            it.release()
            mediaPlayer = null
        }
    }

}