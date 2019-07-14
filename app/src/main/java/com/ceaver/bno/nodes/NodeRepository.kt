package com.ceaver.bno.nodes

import android.os.Handler
import android.os.Looper
import com.ceaver.bno.database.Database
import com.ceaver.bno.threading.BackgroundThreadExecutor
import org.greenrobot.eventbus.EventBus

object NodeRepository {

    fun loadNode(id: Long): Node {
        return getNodeDao().loadNode(id)
    }

    fun loadNodeAsync(id: Long, callbackInMainThread: Boolean, callback: (Node) -> Unit) {
        BackgroundThreadExecutor.execute {
            val node = loadNode(id)
            handleCallback(callbackInMainThread, callback, node)
        }
    }

    fun loadNodes(): List<Node> {
        return getNodeDao().loadNodes()
    }

    fun loadNodesAsync(callbackInMainThread: Boolean, callback: (List<Node>) -> Unit) {
        BackgroundThreadExecutor.execute {
            val nodes = loadNodes()
            handleCallback(callbackInMainThread, callback, nodes)
        }
    }

    fun saveNode(node: Node) {
        if (node.id > 0)
            updateNode(node)
        else
            insertNode(node)
    }

    fun saveNodeAsync(node: Node, callbackInMainThread: Boolean, callback: () -> Unit) {
        if (node.id > 0)
            updateNodeAsync(node, callbackInMainThread, callback)
        else
            insertNodeAsync(node, callbackInMainThread, callback)
    }

    fun insertNode(node: Node) {
        getNodeDao().insertNode(node)
        getEventbus().post(NodeEvents.Insert())
    }

    fun insertNodeAsync(node: Node, callbackInMainThread: Boolean, callback: () -> Unit) {
        BackgroundThreadExecutor.execute {
            insertNode(node)
            handleCallback(callbackInMainThread, callback)
        }
    }

    fun updateNode(node: Node) {
        getNodeDao().updateNode(node)
        getEventbus().post(NodeEvents.Update())
    }

    fun updateNodeAsync(node: Node, callbackInMainThread: Boolean, callback: () -> Unit) {
        BackgroundThreadExecutor.execute {
            updateNode(node)
            handleCallback(callbackInMainThread, callback)
        }
    }

    fun deleteNode(intention: Node) {
        getNodeDao().deleteNode(intention)
        getEventbus().post(NodeEvents.Delete())
    }

    fun deleteNodeAsync(intention: Node) {
        BackgroundThreadExecutor.execute {
            deleteNode(intention)
        }
    }

    private fun handleCallback(callbackInMainThread: Boolean, callback: (Node) -> Unit, node: Node) {
        if (callbackInMainThread)
            Handler(Looper.getMainLooper()).post { callback.invoke(node) }
        else
            callback.invoke(node)
    }

    private fun handleCallback(callbackInMainThread: Boolean, callback: () -> Unit) {
        if (callbackInMainThread)
            Handler(Looper.getMainLooper()).post(callback)
        else
            callback.invoke()
    }

    private fun handleCallback(callbackInMainThread: Boolean, callback: (List<Node>) -> Unit, nodes: List<Node>) {
        if (callbackInMainThread)
            Handler(Looper.getMainLooper()).post { callback.invoke(nodes) }
        else
            callback.invoke(nodes)
    }

    private fun getNodeDao(): NodeDao = getDatabase().nodeDao()

    private fun getDatabase(): Database = Database.getInstance()

    private fun getEventbus() = EventBus.getDefault()

}