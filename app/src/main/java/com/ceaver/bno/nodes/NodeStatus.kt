package com.ceaver.bno.nodes

import com.ceaver.bno.R

enum class NodeStatus(val image: Int) {
    UNKNOWN(R.drawable.node_status_unknown),
    UP(R.drawable.node_status_up),
    DOWN(R.drawable.node_status_down),
    EXCEPTION(R.drawable.node_status_exception),
    PENDING(R.drawable.node_status_pending)
}