package nz.liamdegrey.showcase.ui.splash

import android.os.Bundle
import android.text.SpannableString
import kotlinx.android.synthetic.main.activity_splash.*
import nz.liamdegrey.showcase.R
import nz.liamdegrey.showcase.ui.common.BaseActivity

class SplashActivity : BaseActivity<SplashPresenter, SplashViewMask>(),
        SplashViewMask {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)
    }

    override fun createPresenter(): SplashPresenter = SplashPresenter(getString(R.string.app_name))

    //region: ViewMask methods

    override fun updateAnimationText(animationText: SpannableString) {
        splash_animationTextView.text = animationText
    }

    override fun startHomeActivity() {
//        startActivity(HomeActivity::class.java)//TODO: Create home activity
    }

    //endregion
}
