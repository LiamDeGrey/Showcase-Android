package nz.liamdegrey.showcase.ui.home.views

import android.content.Context
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.text.Html
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_jokepage.view.*
import nz.liamdegrey.showcase.R
import nz.liamdegrey.showcase.models.Joke

class JokePageView private constructor(context: Context) : LinearLayout(context) {

    init {
        orientation = VERTICAL
        inflate(context, R.layout.view_jokepage, this)

        ViewCompat.setBackground(this, ContextCompat.getDrawable(context, R.drawable.bg_dark))
        ViewCompat.setElevation(this, resources.getDimensionPixelSize(R.dimen.elevation).toFloat())
    }

    class Holder(context: Context) {
        val rootView: JokePageView = JokePageView(context)

        fun populateView(number: Int, joke: Joke) {
            rootView.jokePage_characterImageView.updateCharacter(number)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                rootView.jokePage_jokeView.text = Html.fromHtml(joke.body, Html.FROM_HTML_MODE_COMPACT)
            } else {
                rootView.jokePage_jokeView.text = Html.fromHtml(joke.body)
            }
        }
    }
}
