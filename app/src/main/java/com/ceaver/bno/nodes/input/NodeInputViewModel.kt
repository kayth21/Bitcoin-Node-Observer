package com.ceaver.bno.nodes.input

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.ceaver.bno.common.SingleLiveEvent
import com.ceaver.bno.nodes.Node
import com.ceaver.bno.nodes.NodeRepository
import com.ceaver.bno.threading.BackgroundThreadExecutor

class NodeInputViewModel : ViewModel() {

    val node = MutableLiveData<Node>()
    val status = SingleLiveEvent<NodeInputStatus>()

    fun init(nodeId: Long?): NodeInputViewModel {
        if (nodeId == null)
            BackgroundThreadExecutor.execute { node.postValue(Node(0, "", 8333)) }
        else
            NodeRepository.loadNodeAsync(nodeId, false) { node.postValue(it) }
        return this
    }

    fun onSaveClick(ip: String, port: Int) {
        status.postValue(NodeInputStatus.START_SAVE)
        NodeRepository.saveNodeAsync(node.value!!.copy(ip = ip, port = port, nodeStatus = null), true) {
            status.postValue(NodeInputStatus.END_SAVE)
        }
    }

    enum class NodeInputStatus {
        START_SAVE,
        END_SAVE
    }
}