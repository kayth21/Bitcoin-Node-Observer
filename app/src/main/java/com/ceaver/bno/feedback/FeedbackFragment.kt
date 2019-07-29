package com.ceaver.bno.feedback


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment


class FeedbackFragment : DialogFragment() {

    companion object {
        const val FEEDBACK_FRAGMENT_TAG = "com.ceaver.bno.feedback.FeedbackFragment.Tag"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(com.ceaver.bno.R.layout.feedback_fragment, container, false)
    }
}
