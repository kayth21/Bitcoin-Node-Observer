package com.ceaver.bno.splash

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.ceaver.bno.MainActivity
import com.ceaver.bno.R
import com.ceaver.bno.WorkerEvents
import com.ceaver.bno.Workers
import com.ceaver.bno.network.isConnected
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
        bindActions()
        loadSnapshotData()
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
        if (isConnected()) {
            splashActivityActionLabel.text = getString(R.string.loadingData)
            Workers.run()
        } else {
            splashActivityActionLabel.text = getString(R.string.noInternetConnection)
            splashActivityRetryButton.visibility = View.VISIBLE
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: WorkerEvents.End) {
        if (event.error == null) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            splashActivityActionLabel.text = event.error
            splashActivityRetryButton.visibility = View.VISIBLE
        }
    }
}
