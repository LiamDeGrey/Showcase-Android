package nz.liamdegrey.showcase.ui.splash

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.Spanned
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import nz.liamdegrey.showcase.ui.common.BasePresenter
import nz.liamdegrey.showcase.ui.common.helpers.BasicAnimatorListener
import nz.liamdegrey.showcase.ui.splash.spans.CompositeSplashSpan
import kotlin.coroutines.experimental.AbstractCoroutineContextElement
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.ContinuationInterceptor


class SplashPresenter(animationText: CharSequence) : BasePresenter<SplashViewMask>() {
    private object Android : AbstractCoroutineContextElement(ContinuationInterceptor), ContinuationInterceptor {
        override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> =
                AndroidContinuation(continuation)
    }

    private val animationText by lazy { SpannableString(animationText) }
    private val animationTextAnimator: Deferred<AnimatorSet> by lazy {
        async {
            createAnimationTextAnimator(ValueAnimator.AnimatorUpdateListener {
                updateAnimationText()
            })
        }
    }


    override fun onViewAttached() {
        launch(Android) {
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

                animationText.forEachIndexed { index, character ->
                    character.takeUnless { it.isWhitespace() }
                            ?.run {
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
                                    startDelay = invertedIndex * SUBSEQUENT_LETTER_ANIMATION_DELAY
                                    addUpdateListener(updateListener)
                                })
                            }
                }

                val animateInAnimatorSet = AnimatorSet().apply {
                    playTogether(animateInAnimators)
                    startDelay = ANIMATE_IN_DELAY
                }
                val animateOutAnimatorSet = AnimatorSet().apply {
                    playTogether(animateOutAnimators)
                    startDelay = ANIMATE_OUT_DELAY
                }

                playSequentially(animateInAnimatorSet, animateOutAnimatorSet)

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

//endregion

//region: ViewMask methods

    private fun updateAnimationText() {
        getViewMask()?.updateAnimationText(animationText)
    }

    private fun startHomeActivity() {
        getViewMask()?.startHomeActivity()
    }

//endregion


    /* https://gitlab.com/Starcarr/android-coroutines/blob/master/app/src/main/kotlin/com/starcarrlane/coroutines/experimental/Android.kt */
    private class AndroidContinuation<T>(val cont: Continuation<T>) : Continuation<T> by cont {
        override fun resume(value: T) {
            if (Looper.myLooper() == Looper.getMainLooper()) cont.resume(value)
            else Handler(Looper.getMainLooper()).post { cont.resume(value) }
        }

        override fun resumeWithException(exception: Throwable) {
            if (Looper.myLooper() == Looper.getMainLooper()) cont.resumeWithException(exception)
            else Handler(Looper.getMainLooper()).post { cont.resumeWithException(exception) }
        }
    }

    companion object {
        private const val LETTER_ANIMATION_DURATION = 200L
        private const val SUBSEQUENT_LETTER_ANIMATION_DELAY = (LETTER_ANIMATION_DURATION * 0.75).toLong()
        private const val ANIMATE_IN_DELAY = 500L
        private const val ANIMATE_OUT_DELAY = 500L
    }
}
