package com.ceaver.bno.nodes.list

import android.support.v7.widget.RecyclerView
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ceaver.bno.extensions.setLocked
import com.ceaver.bno.network.NetworkStatus
import com.ceaver.bno.nodes.Node
import kotlin.random.Random

internal class NodeListAdapter(private val onClickListener: NodeListFragment.OnItemClickListener) : RecyclerView.Adapter<NodeListAdapter.NodeViewHolder>() {

    companion object {
        val CONTEXT_MENU_GROUP_ID = Random.nextInt()
        val CONTEXT_MENU_DELETE_ITEM_ID = Random.nextInt()
    }

    var nodeList: List<Node> = ArrayList()
    var currentLongClickNode: Node? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NodeViewHolder {
        return NodeViewHolder(LayoutInflater.from(parent.context).inflate(com.ceaver.bno.R.layout.node_list_row, parent, false))
    }

    override fun getItemCount(): Int = nodeList.size

    override fun onBindViewHolder(holder: NodeViewHolder, position: Int) {
        holder.bindItem(nodeList[position], onClickListener)
        holder.itemView.setOnLongClickListener { currentLongClickNode = nodeList[position]; false }
    }

    inner class NodeViewHolder(val view: View) : RecyclerView.ViewHolder(view), View.OnCreateContextMenuListener {
        override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            menu!!.add(CONTEXT_MENU_GROUP_ID, CONTEXT_MENU_DELETE_ITEM_ID, 0, "Delete")
        }

        fun bindItem(node: Node, onClickListener: NodeListFragment.OnItemClickListener) {
            (view.findViewById(com.ceaver.bno.R.id.nodeListRowStatusImage) as ImageView).setImageResource(node.nodeStatus.image)
            (view.findViewById(com.ceaver.bno.R.id.nodeListRowStatusImage) as ImageView).setLocked(node.networkStatus == NetworkStatus.LOADING || node.networkStatus == NetworkStatus.ERROR)
            (view.findViewById(com.ceaver.bno.R.id.nodeListRowSocketAddressField) as TextView).text = "${node.ip}:${node.port}"
            (view.findViewById(com.ceaver.bno.R.id.nodeListRowBlockHeightField) as TextView).text = if (node.isLoading()) "" else if (node.errorMessage == null) "Height: ${node.height ?: "unknown"}" else ""
            (view.findViewById(com.ceaver.bno.R.id.nodeListRowLocationField) as TextView).text = if (node.isLoading()) "" else node.errorMessage ?: "${node.city ?: "unknown city"} (${node.countryCode ?: "unknown country"})"
            (view.findViewById(com.ceaver.bno.R.id.nodeListRowRankingField) as TextView).text = if (node.isLoading()) "" else if (node.errorMessage == null) "Ranking: ${node.rank ?: "unknown"}" else ""

            view.setOnCreateContextMenuListener(this)
            itemView.setOnClickListener { onClickListener.onItemClick(node) }
        }
    }
}