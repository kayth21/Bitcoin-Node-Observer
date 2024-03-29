package com.ceaver.bno

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.ceaver.bno.contribute.ContributeFragment
import com.ceaver.bno.credits.CreditsFragment
import com.ceaver.bno.feedback.FeedbackFragment
import com.ceaver.bno.logging.LogListActivity
import com.ceaver.bno.manual.ManualFragment
import com.ceaver.bno.nodes.NodeEvents
import com.ceaver.bno.preferences.PreferencesActivity
import kotlinx.android.synthetic.main.main_activity.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(findViewById(R.id.mainActivityToolbar))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.mainmenuReloadAction -> {
                Workers.run()
                true
            }
            R.id.mainmenuSettingsAction -> {
                startActivity(Intent(this, PreferencesActivity::class.java))
                true
            }
            R.id.mainmenuFeedbackAction -> {
                FeedbackFragment().show(supportFragmentManager, FeedbackFragment.FEEDBACK_FRAGMENT_TAG)
                true
            }
            R.id.mainmenuContributeAction -> {
                ContributeFragment().show(supportFragmentManager, ContributeFragment.CONTRIBUTE_FRAGMENT_TAG)
                true
            }
            R.id.mainmenuManualAction -> {
                ManualFragment().show(supportFragmentManager, ManualFragment.MANUAL_FRAGMENT_TAG)
                true
            }
            R.id.mainmenuCreditsAction -> {
                CreditsFragment().show(supportFragmentManager, CreditsFragment.CREDITS_FRAGMENT_TAG)
                true
            }
            R.id.mainmenuLoggingAction -> {
                startActivity(Intent(this, LogListActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: WorkerEvents.Start) {
        enableReloadAction(false)
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: WorkerEvents.End) {
        enableReloadAction(true)
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: NodeEvents.Insert) {
        if (!event.suppressReload)
            Workers.run(event.nodeId)
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: NodeEvents.Update) {
        if (!event.suppressReload)
            Workers.run(event.nodeId)
    }

    private fun enableReloadAction(enable: Boolean) {
        val reloadAction = mainActivityToolbar.menu.findItem(R.id.mainmenuReloadAction)
        reloadAction.icon.mutate().alpha = if (enable) 255 else 130
        reloadAction.isEnabled = enable
    }
}
