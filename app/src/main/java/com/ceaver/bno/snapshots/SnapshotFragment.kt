package com.ceaver.bno.snapshots


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.ceaver.bno.R
import com.ceaver.bno.WorkerEvents
import com.ceaver.bno.extensions.asFormattedDateTime
import kotlinx.android.synthetic.main.snapshot_fragment.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class SnapshotFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createViewModel(Observer { onSnapshotLoaded(it!!) })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.snapshot_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        loadSnapshot()
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    private fun createViewModel(snapshotObserver: Observer<Snapshot>): SnapshotViewModel {
        return ViewModelProviders.of(this).get(SnapshotViewModel::class.java).init(this, snapshotObserver)
    }

    private fun loadSnapshot() {
        ViewModelProviders.of(this).get(SnapshotViewModel::class.java).loadSnapshot()
    }

    private fun onSnapshotLoaded(snapshot: Snapshot) {
        snapshotFragmentBlockHeightLabel.text = "Block height: ${snapshot.blockHeight}"
        snapshotFragmentTotalNodesLabel.text = "Total nodes: ${snapshot.totalNodes}"
        snapshotFragmentSnapshotDateLabel.text = "Snapshot date: ${snapshot.snapshotDate.asFormattedDateTime()}"
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: WorkerEvents.End) {
        loadSnapshot()
    }

}
