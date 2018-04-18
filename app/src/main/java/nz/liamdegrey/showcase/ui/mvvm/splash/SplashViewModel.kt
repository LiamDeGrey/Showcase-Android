package nz.liamdegrey.showcase.ui.mvvm.splash

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.arch.lifecycle.MutableLiveData
import android.text.SpannableString
import android.text.Spanned
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import nz.liamdegrey.showcase.ui.mvvm.common.BaseViewModel
import nz.liamdegrey.showcase.ui.shared.common.helpers.BasicAnimatorListener
import nz.liamdegrey.showcase.ui.shared.common.helpers.MainThread
import nz.liamdegrey.showcase.ui.shared.splash.spans.CompositeSplashSpan

class SplashViewModel : BaseViewModel() {
    val showHomeActivity = MutableLiveData<Boolean>()
    val animationTextSpan = MutableLiveData<SpannableString>()

    private lateinit var animationTextAnimator: Deferred<AnimatorSet>

    init {
        showHomeActivity.value = false
        animationTextSpan.value = null
    }

    //Public methods

    fun createAnimation(animationText: String) {
        animationTextSpan.value = SpannableString(animationText)
        animationTextAnimator = async {
            createAnimationTextAnimator(animationText, ValueAnimator.AnimatorUpdateListener {
                animationTextSpan.value = animationTextSpan.value//TODO: Is there a better way of updating this?
            })
        }
    }

    fun startAnimation() {
        launch(MainThread) {
            animationTextAnimator.await().start()
        }
    }

    fun stopAnimation() {
        animationTextAnimator.getCompletedOrNull()?.cancel()
    }

    //endregion

    //region: Private methods

    //FIXME: too much work being done on main thread during animation
    private fun createAnimationTextAnimator(animationText: String, updateListener: ValueAnimator.AnimatorUpdateListener): AnimatorSet =
            AnimatorSet().apply {
                val animateInAnimators = ArrayList<Animator>()
                val animateOutAnimators = ArrayList<Animator>()

                var animateInDuration = 0L
                animationText.removeWhitespace()
                        .also { animateInDuration = it.lastIndex * SUBSEQUENT_LETTER_ANIMATION_DELAY + LETTER_ANIMATION_DURATION }
                        .forEachIndexed { index, _ ->
                            val compositeSplashSpan = CompositeSplashSpan()

                            animationTextSpan.value?.setSpan(compositeSplashSpan, index, index + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

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

                        showHomeActivity.value = true
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

    private fun String.removeWhitespace(): String = filter { !it.isWhitespace() }

//endregion

    companion object {
        private const val LETTER_ANIMATION_DURATION = 200L
        private const val SUBSEQUENT_LETTER_ANIMATION_DELAY = (LETTER_ANIMATION_DURATION * 0.75).toLong()
        private const val ANIMATE_IN_DELAY = 500L
        private const val ANIMATE_OUT_DELAY = 500L
    }
}