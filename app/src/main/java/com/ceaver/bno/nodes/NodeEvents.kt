package com.ceaver.bno.nodes

class NodeEvents {
    data class Update(val nodeId: Long? = null, val suppressReload: Boolean = false)
    data class Insert(val nodeId: Long? = null, val suppressReload: Boolean = false)
    class Delete()
}