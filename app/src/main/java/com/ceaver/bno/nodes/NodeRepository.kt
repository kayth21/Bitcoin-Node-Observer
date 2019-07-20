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

    fun saveNode(node: Node): Long {
        return if (node.id > 0)
            updateNode(node)
        else
            insertNode(node)
    }

    fun saveNodeAsync(node: Node, callbackInMainThread: Boolean, callback: (Long) -> Unit) {
        if (node.id > 0)
            updateNodeAsync(node, callbackInMainThread, callback)
        else
            insertNodeAsync(node, callbackInMainThread, callback)
    }

    fun insertNode(node: Node, suppressEvents: Boolean = false): Long {
        val nodeId = getNodeDao().insertNode(node)
        getEventbus().post(NodeEvents.Insert(nodeId))
        return nodeId
    }

    fun insertNodeAsync(node: Node, callbackInMainThread: Boolean, callback: (Long) -> Unit) {
        BackgroundThreadExecutor.execute {
            val nodeId = insertNode(node)
            handleCallback(callbackInMainThread, callback, nodeId)
        }
    }

    fun updateNode(node: Node, suppressReload: Boolean = false): Long {
        getNodeDao().updateNode(node)
        getEventbus().post(NodeEvents.Update(node.id, suppressReload))
        return node.id
    }

    fun updateNodeAsync(node: Node, callbackInMainThread: Boolean, callback: (Long) -> Unit) {
        BackgroundThreadExecutor.execute {
            updateNode(node)
            handleCallback(callbackInMainThread, callback, node.id)
        }
    }

    fun updateNodes(nodes: List<Node>, suppressReload: Boolean = false): List<Long> {
        getNodeDao().updateNodes(nodes)
        getEventbus().post(NodeEvents.Update(null, suppressReload))
        return nodes.map { it.id }
    }

    fun updateNodesAsync(nodes: List<Node>, callbackInMainThread: Boolean, callback: (List<Long>) -> Unit) {
        BackgroundThreadExecutor.execute {
            val nodeIds = updateNodes(nodes)
            handleCallback(callbackInMainThread, callback, nodeIds)
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

    private fun <T> handleCallback(callbackInMainThread: Boolean, callback: (T) -> Unit, nodeId: T) {
        if (callbackInMainThread)
            Handler(Looper.getMainLooper()).post { callback.invoke(nodeId) }
        else
            callback.invoke(nodeId)
    }

    private fun getNodeDao(): NodeDao = getDatabase().nodeDao()

    private fun getDatabase(): Database = Database.getInstance()

    private fun getEventbus() = EventBus.getDefault()

}