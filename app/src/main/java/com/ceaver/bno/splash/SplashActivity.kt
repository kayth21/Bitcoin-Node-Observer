package com.ceaver.bno.splash

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.ceaver.bno.MainActivity
import com.ceaver.bno.R
import com.ceaver.bno.WorkerEvents
import com.ceaver.bno.Workers
import com.ceaver.bno.network.Network
import com.ceaver.bno.system.SystemEvents
import com.ceaver.bno.system.SystemRepository
import com.ceaver.bno.threading.BackgroundThreadExecutor
import kotlinx.android.synthetic.main.splash_activity.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        publishView()

        if (SystemRepository.isInitialized()) {
            BackgroundThreadExecutor.execute {
                Thread.sleep(1000);
                startActivity(Intent(this, MainActivity::class.java))
            }
        } else {
            bindActions()
            loadSnapshotData()
        }
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    private fun publishView() {
        setContentView(R.layout.splash_activity)
    }

    private fun bindActions() {
        splashActivityRetryButton.setOnClickListener {
            splashActivityRetryButton.visibility = View.INVISIBLE; loadSnapshotData()
        }
    }

    private fun loadSnapshotData() {
        if (Network.isConnected()) {
            splashActivityActionLabel.text = getString(R.string.loadingData)
            Workers.run()
        } else {
            splashActivityActionLabel.text = getString(R.string.noInternetConnection)
            splashActivityRetryButton.visibility = View.VISIBLE
        }
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: SystemEvents.Initialized) {
        Thread.sleep(1000);
        startActivity(Intent(this, MainActivity::class.java))
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: WorkerEvents.SnapshotWorkerEnd) {
        if (event.error != null) {
            splashActivityActionLabel.text = getString(R.string.loadingData)
            Workers.run()
        }
    }
}