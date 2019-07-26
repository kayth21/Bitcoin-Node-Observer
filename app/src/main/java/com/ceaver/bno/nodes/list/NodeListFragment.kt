package com.ceaver.bno.nodes.list


import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.ceaver.bno.R
import com.ceaver.bno.nodes.Node
import com.ceaver.bno.nodes.NodeEvents
import com.ceaver.bno.nodes.NodeRepository
import com.ceaver.bno.nodes.detail.NodeDetailFragment
import com.ceaver.bno.nodes.input.NodeInputFragment
import kotlinx.android.synthetic.main.node_list_fragment.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class NodeListFragment : Fragment() {

    private val nodeListAdapter = NodeListAdapter(OnListItemClickListener())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.node_list_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this);
        nodeListFragmentNodeList.adapter = nodeListAdapter
        nodeListFragmentNodeList.addItemDecoration(DividerItemDecoration(activity!!.application, LinearLayoutManager.VERTICAL)) // TODO seriously?
        nodeListFragmentCreateNodeButton.setOnClickListener {
            val nodeInputFragment = NodeInputFragment()
            nodeInputFragment.arguments = Bundle()
            nodeInputFragment.show(fragmentManager, NodeInputFragment.NODE_INPUT_FRAGMENT_TAG)
        }
        loadAllNodes()
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
        nodeListFragmentNodeList.adapter = null
        nodeListFragmentNodeList.removeItemDecorationAt(0) // TODO seriously?
        nodeListFragmentCreateNodeButton.setOnClickListener(null)
    }


    @Suppress("UNUSED_PARAMETER")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: NodeEvents.Delete) {
        loadAllNodes()
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: NodeEvents.Insert) {
        loadAllNodes()
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: NodeEvents.Update) {
        loadAllNodes()
    }

    private fun loadAllNodes() {
        NodeRepository.loadNodesAsync(true) { onAllNodesLoaded(it) }
    }

    private fun onAllNodesLoaded(nodes: List<Node>) {
        nodeListAdapter.nodeList = nodes
        nodeListAdapter.notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(item: Node)
    }

    private inner class OnListItemClickListener : OnItemClickListener {
        override fun onItemClick(item: Node) {
            showDialogFragment(NodeDetailFragment(), NodeDetailFragment.NODE_DETAIL_FRAGMENT_TAG, NodeDetailFragment.NODE_ID, item.id)
        }
    }

    override fun onContextItemSelected(menuItem: MenuItem?): Boolean {
        if (menuItem!!.groupId == NodeListAdapter.CONTEXT_MENU_GROUP_ID) {
            val selectedNode = nodeListAdapter.currentLongClickNode!!
            when (menuItem.itemId) {
                NodeListAdapter.CONTEXT_MENU_EDIT_ITEM_ID -> showDialogFragment(NodeInputFragment(), NodeInputFragment.NODE_INPUT_FRAGMENT_TAG, NodeInputFragment.NODE_ID, selectedNode.id)
                NodeListAdapter.CONTEXT_MENU_DELETE_ITEM_ID -> NodeRepository.deleteNodeAsync(selectedNode)
                else -> throw IllegalStateException()
            }
        }
        return super.onContextItemSelected(menuItem)
    }

    private fun showDialogFragment(dialogFragment: DialogFragment, fragmentTag: String, nodeIdKey: String, nodeIdValue: Long) {
        val arguments = Bundle();
        arguments.putLong(nodeIdKey, nodeIdValue)
        dialogFragment.arguments = arguments
        dialogFragment.show(fragmentManager, fragmentTag)
    }
}
