package nz.liamdegrey.showcase.ui.mvvm.splash

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.SpannableString
import android.view.View
import kotlinx.android.synthetic.main.activity_splash.*
import nz.liamdegrey.showcase.R
import nz.liamdegrey.showcase.ui.mvvm.common.BaseActivity

class SplashActivity : BaseActivity() {
    override val viewModel by lazy {
        ViewModelProviders.of(this).get(SplashViewModel::class.java)
                .apply { createAnimation(getString(R.string.app_name)) }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        splash_animationTextView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)//Required for Blur animation

        viewModel.animationTextSpan.observe(this, Observer {
            updateAnimationText(it)
        })

        viewModel.showHomeActivity.observe(this, Observer {
            showHomeActivity(it)
        })
    }

    override fun onResume() {
        super.onResume()

        viewModel.startAnimation()
    }

    override fun onPause() {
        super.onPause()

        viewModel.stopAnimation()
    }

    //Private methods

    private fun updateAnimationText(animationText: SpannableString?) {
        splash_animationTextView?.text = animationText
    }

    private fun showHomeActivity(show: Boolean?) {
        if (show == true) {
//            startActivity()
        }
    }

    //endregion
}