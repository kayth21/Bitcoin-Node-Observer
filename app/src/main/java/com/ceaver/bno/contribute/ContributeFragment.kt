package com.ceaver.bno.contribute


import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ContributeFragment : DialogFragment() {

    companion object {
        const val CONTRIBUTE_FRAGMENT_TAG = "com.ceaver.bno.contribute.ContributeFragment.Tag"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(com.ceaver.bno.R.layout.contribute_fragment, container, false)
    }
}
