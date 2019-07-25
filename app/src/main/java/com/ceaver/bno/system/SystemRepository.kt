package com.ceaver.bno.system

import android.content.Context
import com.ceaver.bno.Application
import com.ceaver.bno.snapshots.SnapshotEvents
import com.ceaver.bno.snapshots.SnapshotRepository
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

private const val SHARED_PREFERENCES_KEY = "com.ceaver.bno.system.SystemRepository.SharedPreferences"
private const val IS_INITIALIZED = "com.ceaver.bno.system.SystemRepository.SharedPreferences.isInitialized"

object SystemRepository {

    init {
        EventBus.getDefault().register(this)
    }

    fun isInitialized(): Boolean {
        return Application.appContext!!.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE).getBoolean(IS_INITIALIZED, false)
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: SnapshotEvents.Update) {
        if (!isInitialized() && SnapshotRepository.load().isNetworkStatusNormal()) {
            Application.appContext!!.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE).edit().putBoolean(IS_INITIALIZED, true).apply()
            EventBus.getDefault().unregister(this)
        }
    }
}