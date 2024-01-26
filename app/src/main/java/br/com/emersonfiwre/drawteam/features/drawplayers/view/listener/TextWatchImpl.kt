package br.com.emersonfiwre.drawteam.features.drawplayers.view.listener

import android.text.Editable
import android.text.TextWatcher

class TextWatchImpl(private val listener: (s: CharSequence?) -> Unit): TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // Not yet implemented
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        listener(s)
    }

    override fun afterTextChanged(s: Editable?) {
        // Not yet implemented
    }
}