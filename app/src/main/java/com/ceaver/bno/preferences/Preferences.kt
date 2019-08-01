package com.ceaver.bno.preferences

import androidx.preference.PreferenceManager
import com.ceaver.bno.Application
import com.ceaver.bno.extensions.getBoolean

object Preferences {

    fun isPreferencesSyncOnStartup() = PreferenceManager.getDefaultSharedPreferences(Application.appContext).getBoolean("preferencesSyncOnStartup")!!
    fun isNotifyOnNodeStatusChange() = PreferenceManager.getDefaultSharedPreferences(Application.appContext).getBoolean("preferencesNotifyOnStatusChange")!!
}