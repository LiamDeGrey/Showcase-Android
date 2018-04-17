package nz.liamdegrey.showcase.ui.splash.spans

import android.animation.ObjectAnimator
import android.graphics.BlurMaskFilter
import android.text.TextPaint
import android.text.style.CharacterStyle
import android.util.Property
import kotlin.math.abs
import kotlin.math.max

class CompositeSplashSpan : CharacterStyle() {
    private var percentageComplete: Float = -1f
    private val percentageCompleteInverted: Float
        get() = 1 - abs(percentageComplete)

    private val alpha: Int
        get() = (MAXIMUM_ALPHA * percentageCompleteInverted).toInt()
    private val blurRadius: Float
        get() = max(MAXIMUM_BLUR_RADIUS * abs(percentageComplete), 0.1f)
    private val translationY: Int
        get() = (MAXIMUM_TRANSLATION_Y * percentageComplete).toInt()

    private val percentageCompleteProperty by lazy {
        object : Property<CompositeSplashSpan, Float>(Float::class.java, "PERCENTAGE_COMPLETE") {
            override fun get(span: CompositeSplashSpan): Float = percentageComplete

            override fun set(span: CompositeSplashSpan, value: Float) {
                span.percentageComplete = value
            }
        }
    }


    fun getAnimateInAnimator(): ObjectAnimator {
        return ObjectAnimator.ofFloat(this, percentageCompleteProperty, -1f, 0f)
    }

    fun getAnimateOutAnimator(): ObjectAnimator {
        return ObjectAnimator.ofFloat(this, percentageCompleteProperty, 0f, 1f)
    }

    override fun updateDrawState(paint: TextPaint) {
        paint.alpha = alpha
        paint.maskFilter = BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.NORMAL)
        paint.baselineShift = translationY
    }

    companion object {
        private const val MAXIMUM_ALPHA = 255
        private const val MAXIMUM_BLUR_RADIUS = 100f
        private const val MAXIMUM_TRANSLATION_Y = 100f
    }
}