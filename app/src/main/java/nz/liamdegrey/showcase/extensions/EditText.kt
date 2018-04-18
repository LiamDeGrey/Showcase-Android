package nz.liamdegrey.showcase.extensions

import android.text.Editable
import android.widget.EditText
import nz.liamdegrey.showcase.ui.shared.common.helpers.BasicTextWatcher

fun EditText.onTextChanged(block: (String) -> Unit) {
    addTextChangedListener(object : BasicTextWatcher() {
        override fun afterTextChanged(s: Editable) {
            super.afterTextChanged(s)

            block(s.toString())
        }
    })
}