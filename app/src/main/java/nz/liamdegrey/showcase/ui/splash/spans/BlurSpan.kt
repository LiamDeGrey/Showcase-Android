package nz.liamdegrey.showcase.ui.splash.spans

import android.graphics.BlurMaskFilter
import android.text.TextPaint
import android.text.style.CharacterStyle
import android.util.Property


class BlurSpan(initialRadius: Float) : CharacterStyle() {
    private var radius = initialRadius

    override fun updateDrawState(paint: TextPaint) {
        paint.maskFilter = BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL)
    }

    companion object {
        var BLUR_RADIUS = object : Property<BlurSpan, Float>(Float::class.java, "SPAN_BLUR_RADIUS") {
            override fun get(span: BlurSpan): Float = span.radius

            override fun set(span: BlurSpan, radius: Float) {
                span.radius = radius
            }
        }
    }
}