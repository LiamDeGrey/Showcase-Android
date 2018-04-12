package nz.liamdegrey.showcase.ui.splash

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.text.SpannableString
import android.text.Spanned
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import nz.liamdegrey.showcase.ui.common.BasePresenter
import nz.liamdegrey.showcase.ui.common.helpers.BasicAnimatorListener
import nz.liamdegrey.showcase.ui.splash.spans.AlphaSpan
import nz.liamdegrey.showcase.ui.splash.spans.BlurSpan
import nz.liamdegrey.showcase.ui.splash.spans.TranslationYSpan

class SplashPresenter(animationText: CharSequence) : BasePresenter<SplashViewMask>() {
    private val animationText = SpannableString(animationText)

    private val animationTextAnimator by lazy {
        createAnimationTextAnimator(ValueAnimator.AnimatorUpdateListener {
            updateAnimationText()
        })
    }

    override fun onViewAttached() {
        animationTextAnimator.start()
    }

    override fun onViewDetached() {
        animationTextAnimator.cancel()
    }

    //region: Private methods

    //TODO: Optimise code - minimise duplication, minimise object creation, tidy
    private fun createAnimationTextAnimator(updateListener: ValueAnimator.AnimatorUpdateListener): AnimatorSet {
        fun createAnimateInAnimator(): AnimatorSet =
                AnimatorSet().apply {
                    val characterAnimatorSets = ArrayList<Animator>()

                    animationText.forEachIndexed { index, character ->
                        character.takeUnless { it.isWhitespace() }
                                .run {
                                    val blurSpan = BlurSpan(100f)
                                    val translationYSpan = TranslationYSpan(-100f)
                                    val alphaSpan = AlphaSpan(0f)

                                    val textAnimatorSet = AnimatorSet()

                                    animationText.setSpan(blurSpan, index, index + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                                    animationText.setSpan(translationYSpan, index, index + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                                    animationText.setSpan(alphaSpan, index, index + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

                                    val blurAnimation = ObjectAnimator.ofFloat(blurSpan, BlurSpan.BLUR_RADIUS, 0.1f).apply { addUpdateListener(updateListener) }
                                    val translationYAnimation = ObjectAnimator.ofFloat(translationYSpan, TranslationYSpan.TRANSLATION_Y, 0f).apply { addUpdateListener(updateListener) }
                                    val alphaAnimation = ObjectAnimator.ofFloat(alphaSpan, AlphaSpan.ALPHA, 1f).apply { addUpdateListener(updateListener) }

                                    textAnimatorSet.playTogether(blurAnimation, translationYAnimation, alphaAnimation)
                                    textAnimatorSet.interpolator = DecelerateInterpolator()
                                    textAnimatorSet.duration = LETTER_ANIMATION_DURATION
                                    textAnimatorSet.startDelay = index * SUBSEQUENT_LETTER_ANIMATION_DELAY
                                    characterAnimatorSets.add(textAnimatorSet)
                                }
                    }

                    playTogether(characterAnimatorSets)
                }

        fun createAnimateOutAnimator(): AnimatorSet =
                AnimatorSet().apply {
                    val characterAnimatorSets = ArrayList<Animator>()

                    animationText.forEachIndexed { index, character ->
                        character.takeUnless { it.isWhitespace() }
                                .run {
                                    val blurSpan = BlurSpan(0.1f)
                                    val translationYSpan = TranslationYSpan(0f)
                                    val alphaSpan = AlphaSpan(1f)

                                    val textAnimatorSet = AnimatorSet()

                                    val startIndex = animationText.lastIndex - index
                                    val endIndex = startIndex + 1

                                    animationText.setSpan(blurSpan, startIndex, endIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                                    animationText.setSpan(translationYSpan, startIndex, endIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                                    animationText.setSpan(alphaSpan, startIndex, endIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

                                    val blurAnimation = ObjectAnimator.ofFloat(blurSpan, BlurSpan.BLUR_RADIUS, 100f).apply { addUpdateListener(updateListener) }
                                    val translationYAnimation = ObjectAnimator.ofFloat(translationYSpan, TranslationYSpan.TRANSLATION_Y, 100f).apply { addUpdateListener(updateListener) }
                                    val alphaAnimation = ObjectAnimator.ofFloat(alphaSpan, AlphaSpan.ALPHA, 0f).apply { addUpdateListener(updateListener) }

                                    textAnimatorSet.playTogether(blurAnimation, translationYAnimation, alphaAnimation)
                                    textAnimatorSet.interpolator = AccelerateInterpolator()
                                    textAnimatorSet.duration = LETTER_ANIMATION_DURATION
                                    textAnimatorSet.startDelay = index * SUBSEQUENT_LETTER_ANIMATION_DELAY
                                    characterAnimatorSets.add(textAnimatorSet)
                                }
                    }

                    addListener(object : BasicAnimatorListener() {
                        override fun onAnimationEnd(animation: Animator?) {
                            startHomeActivity()
                        }
                    })

                    playTogether(characterAnimatorSets)
                }

        return createAnimateInAnimator().apply {
            addListener(object : BasicAnimatorListener() {
                override fun onAnimationEnd(animation: Animator?) {
                    createAnimateOutAnimator().apply {
                        startDelay = 500
                        start()
                    }
                }
            })
        }
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

    companion object {
        private const val LETTER_ANIMATION_DURATION: Long = 200
        private const val SUBSEQUENT_LETTER_ANIMATION_DELAY: Long = (LETTER_ANIMATION_DURATION * 0.75).toLong()
    }
}
