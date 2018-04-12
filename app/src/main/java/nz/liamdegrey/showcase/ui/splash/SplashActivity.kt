package nz.liamdegrey.showcase.ui.splash

import android.os.Bundle
import android.text.SpannableString
import android.view.View
import kotlinx.android.synthetic.main.activity_splash.*
import nz.liamdegrey.showcase.R
import nz.liamdegrey.showcase.ui.common.BaseActivity
import nz.liamdegrey.showcase.ui.home.HomeActivity

class SplashActivity : BaseActivity<SplashPresenter, SplashViewMask>(),
        SplashViewMask {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        splash_animationTextView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)//Required for Blur animation
    }

    override fun createPresenter(): SplashPresenter = SplashPresenter(getString(R.string.app_name))

    //region: ViewMask methods

    override fun updateAnimationText(animationText: SpannableString) {
        splash_animationTextView.text = animationText
    }

    override fun startHomeActivity() {
        startActivity(HomeActivity::class.java)
    }

    //endregion
}
