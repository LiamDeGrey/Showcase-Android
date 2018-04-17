package nz.liamdegrey.showcase.ui.splash.spans

import android.animation.ValueAnimator
import android.graphics.BlurMaskFilter
import android.text.TextPaint
import android.text.style.CharacterStyle
import kotlin.math.abs
import kotlin.math.max

class CompositeSplashSpan : CharacterStyle() {
    private val percentageCompleteUpdateListener by lazy {
        ValueAnimator.AnimatorUpdateListener {
            percentageComplete = it.animatedValue as Float
        }
    }

    private var percentageComplete: Float = -1f
    private val percentageCompleteInverted: Float
        get() = 1 - abs(percentageComplete)

    private val alpha: Int
        get() = (MAXIMUM_ALPHA * percentageCompleteInverted).toInt()
    private val blurRadius: Float
        get() = max(MAXIMUM_BLUR_RADIUS * abs(percentageComplete), 0.1f)
    private val translationY: Int
        get() = (MAXIMUM_TRANSLATION_Y * percentageComplete).toInt()


    fun getAnimateInAnimator(): ValueAnimator =
            ValueAnimator.ofFloat(-1f, 0f).apply {
                addUpdateListener(percentageCompleteUpdateListener)
            }

    fun getAnimateOutAnimator(): ValueAnimator =
            ValueAnimator.ofFloat(0f, 1f).apply {
                addUpdateListener(percentageCompleteUpdateListener)
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