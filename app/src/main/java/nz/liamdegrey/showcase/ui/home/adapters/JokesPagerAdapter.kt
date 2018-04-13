package nz.liamdegrey.showcase.ui.home.adapters

import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import nz.liamdegrey.showcase.models.Joke
import nz.liamdegrey.showcase.ui.home.views.JokePageView
import kotlin.math.absoluteValue
import kotlin.math.min

class JokesPagerAdapter : PagerAdapter(),
        ViewPager.PageTransformer {
    private val jokes = ArrayList<Joke>()

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
        val scrollRatio = 1f - min(scrollPosition.absoluteValue, 1f)

        val pageScale = MINIMUM_SCALE_FACTOR + INTERPOLATED_SCALE_FACTOR * scrollRatio
        view.scaleX = pageScale
        view.scaleY = pageScale

        val pageAlpha = MINIMUM_ALPHA_FACTOR + INTERPOLATED_ALPHA_FACTOR * scrollRatio
        view.alpha = pageAlpha
    }

    companion object {
        private const val MINIMUM_SCALE_FACTOR = 0.9f
        private const val INTERPOLATED_SCALE_FACTOR = 1f - MINIMUM_SCALE_FACTOR
        private const val MINIMUM_ALPHA_FACTOR = 0.5f
        private const val INTERPOLATED_ALPHA_FACTOR = 1f - MINIMUM_ALPHA_FACTOR
    }
}
