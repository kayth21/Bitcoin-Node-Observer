package com.ceaver.bno.contribute


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

class ContributeFragment : DialogFragment() {

    companion object {
        const val CONTRIBUTE_FRAGMENT_TAG = "com.ceaver.bno.contribute.ContributeFragment.Tag"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(com.ceaver.bno.R.layout.contribute_fragment, container, false)
    }
}
