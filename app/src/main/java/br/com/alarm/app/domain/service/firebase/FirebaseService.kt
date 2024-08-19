package br.com.alarm.app.domain.service.firebase

import android.content.Context
import br.com.alarm.app.BuildConfig
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.crashlytics.CustomKeysAndValues
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FirebaseService @Inject constructor(@ApplicationContext private val context: Context) :
    FirebaseServiceContract {

    private val analytics = FirebaseAnalytics.getInstance(context)
    private val crashlytics = FirebaseCrashlytics.getInstance()

    override suspend fun screenEvent(screenName: String) {
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, screenName)
        }
    }

    override suspend fun emitCustomEvent(customKeys: CustomKeysAndValues) {
        crashlytics.setCustomKeys(customKeys)
    }

    override suspend fun emitThrow(throwable: Throwable) {
        crashlytics.log("${BuildConfig.VERSION_NAME}.${BuildConfig.BUILD_TYPE} - Error: ${throwable.message}")
        crashlytics.recordException(throwable)
    }
}