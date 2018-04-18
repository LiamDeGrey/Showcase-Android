package nz.liamdegrey.showcase.ui.shared.launch

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_launch.*
import nz.liamdegrey.showcase.R
import nz.liamdegrey.showcase.ui.mvp.splash.SplashActivity as MvpSplashActivity
import nz.liamdegrey.showcase.ui.mvvm.splash.SplashActivity as MvvmSplashActivity

class LaunchActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_launch)

        launch_mvpButton.setOnClickListener(this)
        launch_mvvmButton.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.launch_mvpButton -> startMvpSplash()
            R.id.launch_mvvmButton -> startMvvmSplash()
        }
    }

    //region: Private methods

    private fun startMvpSplash() {
        startActivity(MvpSplashActivity::class.java)
    }

    private fun startMvvmSplash() {
        startActivity(MvvmSplashActivity::class.java)
    }

    private fun startActivity(activityClass: Class<out AppCompatActivity>) {
        startActivity(Intent(this, activityClass).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }

    //endregion
}