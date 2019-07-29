package com.ceaver.bno.nodes.input


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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
        val ip = nodeInputFragmentHostField.text.toString()
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
            nodeInputFragmentHostField.setText(it!!.host)
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
        val ipV4Regex = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\$"
        val ipV6Regex = "^(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))\$"
        val dynDnsUrlRegex = "^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)+([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\\-]*[A-Za-z0-9])\$"
        nodeInputFragmentHostField.registerInputValidator({ it.matches("$ipV4Regex|$ipV6Regex|$dynDnsUrlRegex".toRegex()) }, getString(R.string.invalidHost))
        nodeInputFragmentHostField.afterTextChanged { nodeInputFragmentSaveButton.isEnabled = checkSaveButton() }
        nodeInputFragmentPortField.registerInputValidator({ it.isNotEmpty() && it.toInt() in 0..65535 }, getString(R.string.invalidPortNumber))
        nodeInputFragmentPortField.afterTextChanged { nodeInputFragmentSaveButton.isEnabled = checkSaveButton() }
    }

    private fun enableInput(enable: Boolean) {
        nodeInputFragmentSaveButton.isEnabled = enable && checkSaveButton()
        nodeInputFragmentHostField.isEnabled = enable
        nodeInputFragmentPortField.isEnabled = enable
    }

    private fun checkSaveButton(): Boolean {
        return nodeInputFragmentHostField.error == null && nodeInputFragmentPortField.error == null
    }
}
