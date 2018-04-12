package nz.liamdegrey.showcase.ui.home.adapters

import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import nz.liamdegrey.showcase.models.Joke
import nz.liamdegrey.showcase.ui.home.views.JokePageView

class JokesPagerAdapter(private val pager: ViewPager) : PagerAdapter(),
        ViewPager.PageTransformer {
    private val jokes = ArrayList<Joke>()

    private val transformationInterpolator = AccelerateDecelerateInterpolator()
    private val pagerWidth: Int
        get() = pager.width
    private val pagerMargin: Int
        get() = pager.pageMargin

    fun populateJokes(jokes: List<Joke>) {
        this.jokes.clear()
        this.jokes.addAll(jokes)
        notifyDataSetChanged()
    }

    override fun getCount(): Int = jokes.size

    override fun isViewFromObject(view: View, viewHolder: Any): Boolean = (viewHolder as? JokePageView.Holder)?.rootView === view

    override fun instantiateItem(container: ViewGroup, itemPosition: Int): Any {
        val viewHolder = JokePageView.Holder(container.context)
        val joke = jokes[itemPosition]

        viewHolder.populateView(itemPosition, joke)
        container.addView(viewHolder.rootView)

        return viewHolder
    }

    override fun destroyItem(container: ViewGroup, itemPosition: Int, holder: Any) {
        container.removeView((holder as JokePageView.Holder).rootView)
    }

    override fun transformPage(view: View, scrollPosition: Float) {
        val pagePadding = (pagerWidth - view.width) / 2
        val pagePaddingRatio = pagePadding / pagerWidth
        val pagePosition = scrollPosition - pagePaddingRatio
        val pageScale = Math.max(0f, 1 - Math.abs(pagePosition))

        val pageInterpolatedScale = transformationInterpolator.getInterpolation(pageScale)
        val pageScaleFactor = 0.90f + 0.10f * pageInterpolatedScale

        val pageInterpolatedTranslation = transformationInterpolator.getInterpolation(1f - pageScale)
        val pageTranslation = if (pagePosition < 0) pagerMargin / 2 * pageInterpolatedTranslation else -pagerMargin / 2 * pageInterpolatedTranslation

        view.scaleX = pageScaleFactor
        view.scaleY = pageScaleFactor
        view.translationX = pageTranslation
    }
}
