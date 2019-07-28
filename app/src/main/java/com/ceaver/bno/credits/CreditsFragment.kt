package com.ceaver.bno.credits


import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ceaver.bno.R

class CreditsFragment : DialogFragment() {

    companion object {
        const val CREDITS_FRAGMENT_TAG = "com.ceaver.bno.credits.CreditsFragment.Tag"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.credits_fragment, container, false)
    }


}
