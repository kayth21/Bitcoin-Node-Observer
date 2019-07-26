package com.ceaver.bno.nodes.detail

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import com.ceaver.bno.nodes.Node
import com.ceaver.bno.nodes.NodeRepository

class NodeDetailViewModel : ViewModel() {

    val node = MutableLiveData<Node>()

    fun init(owner: LifecycleOwner, nodeObserver: Observer<Node>): NodeDetailViewModel {
        node.observe(owner, nodeObserver)
        return this
    }

    fun loadNode(nodeId: Long) {
        NodeRepository.loadNodeAsync(nodeId, false) { node.postValue(it) }
    }
}