package com.ceaver.bno.manual


import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ceaver.bno.R

class ManualFragment : DialogFragment() {

    companion object {
        const val MANUAL_FRAGMENT_TAG = "com.ceaver.bno.manual.ManualFragment.Tag"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.manual_fragment, container, false)
    }


}
