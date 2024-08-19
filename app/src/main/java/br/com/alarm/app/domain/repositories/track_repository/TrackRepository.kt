package br.com.alarm.app.domain.repositories.track_repository

import br.com.alarm.app.domain.service.firebase.FirebaseServiceContract
import com.google.firebase.crashlytics.CustomKeysAndValues
import javax.inject.Inject

class TrackRepository @Inject constructor(private val firebase: FirebaseServiceContract) :
    TrackRepositoryContract {

    override suspend fun setScreenTrack(screenName: String) {
        firebase.screenEvent(screenName)
    }

    override suspend fun emitCustomEvent(customKeys: CustomKeysAndValues) {
        firebase.emitCustomEvent(customKeys)
    }

    override suspend fun emitThrow(throwable: Throwable) {
        firebase.emitThrow(throwable)
    }
}