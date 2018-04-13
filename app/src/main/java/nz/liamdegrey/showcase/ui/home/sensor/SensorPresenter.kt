package nz.liamdegrey.showcase.ui.home.sensor

import nz.liamdegrey.showcase.Application
import nz.liamdegrey.showcase.ui.common.BasePresenter

class SensorPresenter : BasePresenter<SensorViewMask>() {
    private val preferences by lazy { Application.instance.preferences }

    override fun onViewAttached() {
        if (!preferences.hasViewedSensorFragment) {
            showInstructionToast()
            preferences.hasViewedSensorFragment = true
        }
    }

    override fun onViewDetached() {}

    //region: Presenter methods


    //endregion

    //region: Private methods


    //endregion

    //region: View methods

    private fun showInstructionToast() {
        getViewMask()?.showInstructionToast()
    }

    //endregion
}