package com.ceaver.bno.snapshots

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import com.ceaver.bno.common.SingleLiveEvent

class SnapshotViewModel : ViewModel() {

    private val snapshot = SingleLiveEvent<Snapshot>()

    fun init(snapshotFragment: SnapshotFragment, snapshotObserver: Observer<Snapshot>): SnapshotViewModel {
        snapshot.observe(snapshotFragment, snapshotObserver)
        return this
    }

    fun loadSnapshot() {
        snapshot.postValue(SnapshotRepository.load())
    }
}