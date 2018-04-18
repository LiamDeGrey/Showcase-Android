package nz.liamdegrey.showcase.ui.shared.splash.spans

import android.text.TextPaint
import android.text.style.CharacterStyle
import android.util.Property

class TranslationYSpan(initialTranslationY: Float) : CharacterStyle() {
    private var translationY = initialTranslationY


    override fun updateDrawState(paint: TextPaint) {
        paint.baselineShift = translationY.toInt()
    }

    companion object {
        val TRANSLATION_Y = object : Property<TranslationYSpan, Float>(Float::class.java, "TRANSLATION_Y") {
            override fun get(span: TranslationYSpan): Float = span.translationY

            override fun set(span: TranslationYSpan, value: Float) {
                span.translationY = value
            }
        }
    }
}