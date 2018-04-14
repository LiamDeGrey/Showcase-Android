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
                                    val blurSpan = BlurSpan(BLUR_RADIUS_OFFSET)
                                    val translationYSpan = TranslationYSpan(-TRANSLATION_Y_OFFSET)
                                    val alphaSpan = AlphaSpan(ALPHA_OFFSET)

                                    val textAnimatorSet = AnimatorSet()

                                    animationText.setSpan(blurSpan, index, index + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                                    animationText.setSpan(translationYSpan, index, index + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                                    animationText.setSpan(alphaSpan, index, index + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

                                    val blurAnimation = ObjectAnimator.ofFloat(blurSpan, BlurSpan.BLUR_RADIUS, BLUR_RADIUS_STANDARD).apply { addUpdateListener(updateListener) }
                                    val translationYAnimation = ObjectAnimator.ofFloat(translationYSpan, TranslationYSpan.TRANSLATION_Y, TRANSLATION_Y_STANDARD).apply { addUpdateListener(updateListener) }
                                    val alphaAnimation = ObjectAnimator.ofFloat(alphaSpan, AlphaSpan.ALPHA, ALPHA_STANDARD).apply { addUpdateListener(updateListener) }

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
                                    val blurSpan = BlurSpan(BLUR_RADIUS_STANDARD)
                                    val translationYSpan = TranslationYSpan(TRANSLATION_Y_STANDARD)
                                    val alphaSpan = AlphaSpan(ALPHA_STANDARD)

                                    val textAnimatorSet = AnimatorSet()

                                    val startIndex = animationText.lastIndex - index
                                    val endIndex = startIndex + 1

                                    animationText.setSpan(blurSpan, startIndex, endIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                                    animationText.setSpan(translationYSpan, startIndex, endIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                                    animationText.setSpan(alphaSpan, startIndex, endIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

                                    val blurAnimation = ObjectAnimator.ofFloat(blurSpan, BlurSpan.BLUR_RADIUS, BLUR_RADIUS_OFFSET).apply { addUpdateListener(updateListener) }
                                    val translationYAnimation = ObjectAnimator.ofFloat(translationYSpan, TranslationYSpan.TRANSLATION_Y, TRANSLATION_Y_OFFSET).apply { addUpdateListener(updateListener) }
                                    val alphaAnimation = ObjectAnimator.ofFloat(alphaSpan, AlphaSpan.ALPHA, ALPHA_OFFSET).apply { addUpdateListener(updateListener) }

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
                        startDelay = ANIMATE_OUT_DELAY
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
        private const val LETTER_ANIMATION_DURATION = 200L
        private const val SUBSEQUENT_LETTER_ANIMATION_DELAY = (LETTER_ANIMATION_DURATION * 0.75).toLong()
        private const val ANIMATE_OUT_DELAY = 500L

        private const val BLUR_RADIUS_STANDARD = 0.1f
        private const val BLUR_RADIUS_OFFSET = 100f
        private const val TRANSLATION_Y_STANDARD = 0f
        private const val TRANSLATION_Y_OFFSET = 100f
        private const val ALPHA_OFFSET = 0f
        private const val ALPHA_STANDARD = 1f
    }
}
