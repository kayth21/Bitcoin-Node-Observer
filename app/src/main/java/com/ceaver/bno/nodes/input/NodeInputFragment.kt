package com.ceaver.bno.nodes.input


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ceaver.bno.R
import com.ceaver.bno.extensions.afterTextChanged
import com.ceaver.bno.extensions.registerInputValidator
import kotlinx.android.synthetic.main.node_input_fragment.*

class NodeInputFragment : DialogFragment() {

    companion object {
        const val NODE_INPUT_FRAGMENT_TAG = "com.ceaver.bno.nodes.input.NodeInputFragment.Tag"
        const val NODE_ID = "com.ceaver.bno.nodes.input.NodeInputFragment.nodeId"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.node_input_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()

        val nodeId = lookupNodeId()
        val viewModel = lookupViewModel(nodeId)

        bindActions(viewModel)
        observeStatus(viewModel)
        observeDataReady(viewModel)
    }

    private fun lookupNodeId(): Long? = arguments!!.getLong(NODE_ID).takeUnless { it == 0L }
    private fun lookupViewModel(nodeId: Long?): NodeInputViewModel = ViewModelProviders.of(this).get(NodeInputViewModel::class.java).init(nodeId)

    private fun bindActions(viewModel: NodeInputViewModel) {
        nodeInputFragmentSaveButton.setOnClickListener { onSaveClick(viewModel) }
    }

    private fun onSaveClick(viewModel: NodeInputViewModel) {
        val ip = nodeInputFragmentIpField.text.toString()
        val port = nodeInputFragmentPortField.text.toString().toInt()
        viewModel.onSaveClick(ip, port)
    }

    private fun observeStatus(viewModel: NodeInputViewModel) {
        viewModel.status.observe(this, Observer {
            when (it) {
                NodeInputViewModel.NodeInputStatus.START_SAVE -> onStartSave()
                NodeInputViewModel.NodeInputStatus.END_SAVE -> onEndSave()
            }
        })
    }

    private fun observeDataReady(viewModel: NodeInputViewModel) {
        viewModel.node.observe(this, Observer {
            nodeInputFragmentIpField.setText(it!!.ip)
            nodeInputFragmentPortField.setText(it.port.toString())

            registerInputValidation()
            enableInput(true)
            viewModel.node.removeObservers(this)
        })
    }


    private fun onStartSave() {
        enableInput(false)
    }

    private fun onEndSave() {
        dismiss()
    }

    private fun registerInputValidation() {
        nodeInputFragmentIpField.registerInputValidator({ it.matches("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\$".toRegex()) }, getString(R.string.invalidIpAddress))
        nodeInputFragmentIpField.afterTextChanged { nodeInputFragmentSaveButton.isEnabled = checkSaveButton() }
        nodeInputFragmentPortField.registerInputValidator({ it.isNotEmpty() && it.toInt() in 0..65535 }, getString(R.string.invalidPortNumber))
        nodeInputFragmentPortField.afterTextChanged { nodeInputFragmentSaveButton.isEnabled = checkSaveButton() }
    }

    private fun enableInput(enable: Boolean) {
        nodeInputFragmentSaveButton.isEnabled = enable && checkSaveButton()
        nodeInputFragmentIpField.isEnabled = enable
        nodeInputFragmentPortField.isEnabled = enable
    }

    private fun checkSaveButton(): Boolean {
        return nodeInputFragmentIpField.error == null && nodeInputFragmentPortField.error == null
    }
}
