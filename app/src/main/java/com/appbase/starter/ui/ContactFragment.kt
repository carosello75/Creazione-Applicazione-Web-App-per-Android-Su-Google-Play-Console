package com.appbase.starter.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.appbase.starter.R

class ContactFragment : Fragment(R.layout.fragment_placeholder) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.text_title).text = getString(R.string.nav_contact)
        view.findViewById<TextView>(R.id.text_hint).text = getString(R.string.hint_edit_contact)
    }
}
