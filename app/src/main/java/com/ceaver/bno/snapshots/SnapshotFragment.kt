package com.ceaver.bno.snapshots


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ceaver.bno.extensions.asFormattedDateTime
import com.ceaver.bno.extensions.asFormattedNumber
import com.ceaver.bno.extensions.setLocked
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
        return inflater.inflate(com.ceaver.bno.R.layout.snapshot_fragment, container, false)
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
        snapshotFragmentBitnodesImage.setLocked(!snapshot.isNetworkStatusNormal())
        snapshotFragmentBlockHeightValue.text = snapshot.blockHeight!!.asFormattedNumber()
        snapshotFragmentTotalNodesValue.text = snapshot.totalNodes!!.asFormattedNumber()
        snapshotFragmentSnapshotDateValue.text = snapshot.snapshotDate!!.asFormattedDateTime()
        snapshotFragmentExceptionLabel.text = snapshot.errorMessage ?: ""
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: SnapshotEvents.Update) {
        loadSnapshot()
    }
}
