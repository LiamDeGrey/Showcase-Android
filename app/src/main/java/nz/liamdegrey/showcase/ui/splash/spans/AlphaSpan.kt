package nz.liamdegrey.showcase.ui.splash.spans

import android.text.TextPaint
import android.text.style.CharacterStyle
import android.util.Property

class AlphaSpan(initialAlpha: Float) : CharacterStyle() {
    private var alpha = initialAlpha

    override fun updateDrawState(paint: TextPaint) {
        paint.alpha = (alpha * 255).toInt()
    }

    companion object {
        var ALPHA = object : Property<AlphaSpan, Float>(Float::class.java, "SPAN_ALPHA") {
            override fun get(span: AlphaSpan): Float = span.alpha

            override fun set(span: AlphaSpan, value: Float) {
                span.alpha = value
            }
        }
    }
}