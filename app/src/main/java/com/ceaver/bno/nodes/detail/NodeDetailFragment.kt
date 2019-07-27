package com.ceaver.bno.nodes.detail


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.ceaver.bno.R
import com.ceaver.bno.bitcoin.BitcoinService
import com.ceaver.bno.extensions.asFormattedDateTime
import com.ceaver.bno.nodes.Node
import com.ceaver.bno.nodes.NodeEvents
import kotlinx.android.synthetic.main.node_detail_fragment.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class NodeDetailFragment : DialogFragment() {

    companion object {
        const val NODE_DETAIL_FRAGMENT_TAG = "com.ceaver.bno.nodes.detail.NodeDetailFragment.Tag"
        const val NODE_ID = "com.ceaver.bno.nodes.detail.NodeDetailFragment.nodeId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lookupViewModel().init(this, Observer { onNodeLoaded(it!!) })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.node_detail_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this);
        loadNode()
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    private fun lookupNodeId(): Long = arguments!!.getLong(NODE_ID)
    private fun lookupViewModel(): NodeDetailViewModel = ViewModelProviders.of(this).get(NodeDetailViewModel::class.java)

    private fun onNodeLoaded(node: Node) {
        nodeDetailFragmentSocketAddressValue.text = "${node.host}:${node.port}"
        // sync data
        nodeDetailFragmentLastSyncStatusValue.text = node.lastSyncStatus?.name
        nodeDetailFragmentLastSyncDateValue.text = node.lastSyncDate?.asFormattedDateTime()
        nodeDetailFragmentErrorTextValue.text = node.errorMessage
        if (node.errorMessage == null) {
            nodeDetailFragmentErrorTextValue.visibility = GONE
            nodeDetailFragmentErrorTextLabel.visibility = GONE
        } else {
            nodeDetailFragmentErrorTextValue.visibility = VISIBLE
            nodeDetailFragmentErrorTextLabel.visibility = VISIBLE
        }
        // node details
        nodeDetailFragmentNodeStatusValue.text = node.nodeStatus?.name
        nodeDetailFragmentBlockHeightValue.text = node.height?.toString()
        nodeDetailFragmentProtocolVersionValue.text = node.protocolVersion
        nodeDetailFragmentUserAgentValue.text = node.userAgent
        nodeDetailFragmentConnectedSinceValue.text = node.connectedSince?.asFormattedDateTime()
        nodeDetailFragmentServicesValue.text = if (node.services == null) "" else BitcoinService.getServicesAsString(node.services)
        nodeDetailFragmentAsnValue.text = node.asn
        nodeDetailFragmentCityValue.text = node.city
        nodeDetailFragmentCountryCodeValue.text = node.countryCode
        nodeDetailFragmentTimezoneValue.text = node.timezone
        nodeDetailFragmentLatLongValue.text = if (node.latitude != null && node.longitude != null) "${node.latitude} / ${node.longitude}" else ""
        nodeDetailFragmentOrganisationNameValue.text = node.organizationName
        nodeDetailFragmentHostnameValue.text = node.hostname
        // leaderboard
        nodeDetailFragmentPeerIndexValue.text = node.peerIndex
        nodeDetailFragmentRankValue.text = node.rank?.toString()
        nodeDetailFragmentProtocolVersionIndexValue.text = node.protocolVersionIndex
        nodeDetailFragmentServicesIndexValue.text = node.servicesIndex
        nodeDetailFragmentHeightIndexValue.text = node.heightIndex
        nodeDetailFragmentAsnIndexValue.text = node.asnIndex
        nodeDetailFragmentPortIndexValue.text = node.portIndex
        nodeDetailFragmentNetworkSpeedIndexValue.text = node.networkSpeedIndex
        nodeDetailFragmentAverageDailyLatencyIndexValue.text = node.averageDailyLatencyIndex
        nodeDetailFragmentDailyUptimeIndexValue.text = node.dailyUptimeIndex
        nodeDetailFragmentAverageWeeklyLatencyIndexValue.text = node.averageWeeklyLatencyIndex
        nodeDetailFragmentWeeklyUptimeIndexValue.text = node.weeklyUptimeIndex
        nodeDetailFragmentAverageMonthlyLatencyIndexValue.text = node.averageMonthlyLatencyIndex
        nodeDetailFragmentMonthlyUptimeIndexValue.text = node.monthlyUptimeIndex
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: NodeEvents.Update) {
        if (event.nodeId == lookupNodeId())
            loadNode()
    }

    private fun loadNode() {
        lookupViewModel().loadNode(lookupNodeId())
    }
}
