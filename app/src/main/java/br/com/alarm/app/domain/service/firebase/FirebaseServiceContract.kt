package br.com.alarm.app.domain.service.firebase

import com.google.firebase.crashlytics.CustomKeysAndValues

interface FirebaseServiceContract {

    suspend fun screenEvent(screenName: String)

    suspend fun emitCustomEvent(customKeys: CustomKeysAndValues)

    suspend fun emitThrow(throwable: Throwable)
}