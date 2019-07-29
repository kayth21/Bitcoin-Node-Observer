package com.ceaver.bno.nodes.detail

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
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