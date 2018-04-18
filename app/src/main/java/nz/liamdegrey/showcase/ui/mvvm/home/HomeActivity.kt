package nz.liamdegrey.showcase.ui.mvvm.home

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.view.Gravity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_home.*
import nz.liamdegrey.showcase.R
import nz.liamdegrey.showcase.models.Joke
import nz.liamdegrey.showcase.ui.mvvm.common.BaseActivity
import nz.liamdegrey.showcase.ui.mvvm.home.about.AboutFragment
import nz.liamdegrey.showcase.ui.mvvm.home.acknowledgements.AcknowledgementsFragment
import nz.liamdegrey.showcase.ui.mvvm.home.search.SearchFragment
import nz.liamdegrey.showcase.ui.mvvm.splash.SplashActivity
import nz.liamdegrey.showcase.ui.shared.common.views.Toolbar
import nz.liamdegrey.showcase.ui.shared.home.adapters.JokesPagerAdapter
import nz.liamdegrey.showcase.ui.shared.home.views.DrawerView

class HomeActivity : BaseActivity(), DrawerView.Callbacks {
    override val viewModel by lazy { ViewModelProviders.of(this).get(HomeViewModel::class.java) }

    private val jokesPagerAdapter by lazy { JokesPagerAdapter() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        home_drawerLayout.setScrimColor(ContextCompat.getColor(this, R.color.semiTransparent))
        home_drawerView.callbacks = this

        home_jokesPager.pageMargin = resources.getDimensionPixelSize(R.dimen.padding_16)
        home_jokesPager.setPageTransformer(false, jokesPagerAdapter, View.LAYER_TYPE_HARDWARE)
        home_jokesPager.adapter = jokesPagerAdapter
    }

    override fun initToolbar(toolbar: Toolbar) {
        super.initToolbar(toolbar)

        toolbar.setTitle(R.string.home_title)
        toolbar.setHomeAsDrawer()
        toolbar.setExtraAsSearch()
    }

    override fun setLoading(loading: Boolean) {
        super.setLoading(loading)

        if (loading) {
            home_drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        } else {
            home_drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        }
    }

    override fun onHomeClicked() {
        home_drawerLayout.openDrawer(Gravity.START)
    }

    override fun onExtraClicked() {
        showFragment(SearchFragment())
    }

    override fun consumeBackPress() {
        if (home_drawerLayout.isDrawerOpen(Gravity.START)) {
            home_drawerLayout.closeDrawer(Gravity.START)
            return
        }

        super.consumeBackPress()
    }

    //region: Drawer methods

    override fun onAboutClicked() {
        showFragment(AboutFragment())
    }

    override fun onAcknowledgementsClicked() {
        showFragment(AcknowledgementsFragment())
    }

    override fun onLikedTheSplashClicked() {
        startActivity(SplashActivity::class.java)
    }

    override fun closeDrawer() {
        home_drawerLayout.closeDrawer(Gravity.START)
    }

    //endregion

    //region: Private methods

    fun showWelcomeMessage() {
        Toast.makeText(this, R.string.home_welcomeMessage, Toast.LENGTH_LONG).show()
    }

    fun showNoContentView(show: Boolean) {
        home_noContentView.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun populateJokes(jokes: List<Joke>) {
        jokesPagerAdapter.populateJokes(jokes)
        home_jokesPager_indicator.setViewPager(home_jokesPager)
        home_jokesPager.offscreenPageLimit = jokesPagerAdapter.count - 1
        home_jokesPager.setCurrentItem(jokes.lastIndex, false)
        home_jokesPager.setCurrentItem(0, true)
    }

    fun showSplashActivity() {
        startActivity(SplashActivity::class.java)
    }

    //endregion
}