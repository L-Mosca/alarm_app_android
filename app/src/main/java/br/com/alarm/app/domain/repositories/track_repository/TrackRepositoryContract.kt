package br.com.alarm.app.domain.repositories.track_repository

import com.google.firebase.crashlytics.CustomKeysAndValues

interface TrackRepositoryContract {

    suspend fun setScreenTrack(screenName: String)

    suspend fun emitCustomEvent(customKeys: CustomKeysAndValues)

    suspend fun emitThrow(throwable: Throwable)
}