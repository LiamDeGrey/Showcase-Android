package nz.liamdegrey.showcase.ui.mvp.splash

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.text.SpannableString
import android.text.Spanned
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import nz.liamdegrey.showcase.ui.mvp.common.BasePresenter
import nz.liamdegrey.showcase.ui.shared.common.helpers.BasicAnimatorListener
import nz.liamdegrey.showcase.ui.shared.common.helpers.MainThread
import nz.liamdegrey.showcase.ui.shared.splash.spans.CompositeSplashSpan


class SplashPresenter(animationText: CharSequence) : BasePresenter<SplashViewMask>() {
    private val animationText by lazy { SpannableString(animationText) }
    private val animationTextAnimator: Deferred<AnimatorSet> by lazy {
        async {
            createAnimationTextAnimator(ValueAnimator.AnimatorUpdateListener {
                updateAnimationText()
            })
        }
    }


    override fun onViewAttached() {
        launch(MainThread) {
            animationTextAnimator.await().start()
        }
    }

    override fun onViewDetached() {
        animationTextAnimator.getCompletedOrNull()?.cancel()
    }

//region: Private methods

    //FIXME: too much work being done on main thread during animation
    private fun createAnimationTextAnimator(updateListener: ValueAnimator.AnimatorUpdateListener): AnimatorSet =
            AnimatorSet().apply {
                val animateInAnimators = ArrayList<Animator>()
                val animateOutAnimators = ArrayList<Animator>()

                var animateInDuration = 0L
                animationText.removeWhitespace()
                        .also { animateInDuration = it.lastIndex * SUBSEQUENT_LETTER_ANIMATION_DELAY + LETTER_ANIMATION_DURATION }
                        .forEachIndexed { index, _ ->
                            val compositeSplashSpan = CompositeSplashSpan()

                            animationText.setSpan(compositeSplashSpan, index, index + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

                            animateInAnimators.add(compositeSplashSpan.getAnimateInAnimator().apply {
                                interpolator = DecelerateInterpolator()
                                duration = LETTER_ANIMATION_DURATION
                                startDelay = index * SUBSEQUENT_LETTER_ANIMATION_DELAY
                                addUpdateListener(updateListener)
                            })

                            val invertedIndex = animationText.lastIndex - index

                            animateOutAnimators.add(compositeSplashSpan.getAnimateOutAnimator().apply {
                                interpolator = AccelerateInterpolator()
                                duration = LETTER_ANIMATION_DURATION
                                startDelay = animateInDuration + ANIMATE_OUT_DELAY + invertedIndex * SUBSEQUENT_LETTER_ANIMATION_DELAY
                                addUpdateListener(updateListener)
                            })
                        }

                playTogether(animateInAnimators + animateOutAnimators)
                startDelay = ANIMATE_IN_DELAY

                addListener(object : BasicAnimatorListener() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)

                        startHomeActivity()
                    }
                })
            }

    private fun <T> Deferred<T>.getCompletedOrNull(): T? =
            if (!isActive && isCompleted &&
                    !isCompletedExceptionally &&
                    !isCancelled) {
                getCompleted()
            } else {
                null
            }

    private fun SpannableString.removeWhitespace(): String = this.toString().filter { !it.isWhitespace() }

//endregion

//region: ViewMask methods

    private fun updateAnimationText() {
        getViewMask()?.updateAnimationText(animationText)
    }

    private fun startHomeActivity() {
        getViewMask()?.startHomeActivity()
    }

//endregion

    companion object {
        private const val LETTER_ANIMATION_DURATION = 200L
        private const val SUBSEQUENT_LETTER_ANIMATION_DELAY = (LETTER_ANIMATION_DURATION * 0.75).toLong()
        private const val ANIMATE_IN_DELAY = 500L
        private const val ANIMATE_OUT_DELAY = 500L
    }
}
