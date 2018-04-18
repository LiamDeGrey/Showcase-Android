package nz.liamdegrey.showcase.extensions

import android.text.SpannableString
import android.text.Spanned
import android.text.style.CharacterStyle

fun CharSequence.styleStringPortion(style: CharacterStyle, portionText: CharSequence): CharSequence {
    val spannableString = SpannableString(this)
    val startIndexPortionText = this.toString().indexOf(portionText.toString())
    val endIndexPortionText = startIndexPortionText + portionText.length
    spannableString.setSpan(style, startIndexPortionText, endIndexPortionText, Spanned.SPAN_INCLUSIVE_INCLUSIVE)

    return spannableString
}