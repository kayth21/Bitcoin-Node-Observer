package com.ceaver.bno.donate


import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ceaver.bno.R

class DonateFragment : DialogFragment() {

    companion object {
        const val DONATE_FRAGMENT_TAG = "com.ceaver.bno.donate.DonateFragment.Tag"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.donate_fragment, container, false)
    }


}
