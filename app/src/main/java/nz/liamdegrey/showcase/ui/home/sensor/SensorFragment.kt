package nz.liamdegrey.showcase.ui.home.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_sensor.*
import nz.liamdegrey.showcase.R
import nz.liamdegrey.showcase.ui.common.BaseFragment
import nz.liamdegrey.showcase.ui.common.views.Toolbar

class SensorFragment : BaseFragment<SensorPresenter, SensorViewMask>(),
        SensorViewMask, SensorEventListener {
    private val sensorManager by lazy { activity!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    private val sensor by lazy { sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION) }

    override val layoutResId: Int
        get() = R.layout.fragment_sensor


    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST)
    }

    override fun onDestroy() {
        super.onDestroy()

        sensorManager.unregisterListener(this)
    }

    override fun initToolbar(toolbar: Toolbar) {
        super.initToolbar(toolbar)

        toolbar.setTitle(R.string.sensor_title)
        toolbar.setHomeAsBack()
    }

    override fun createPresenter(): SensorPresenter = SensorPresenter()

    //region: Sensor methods

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent) {
        Log.v("debug", "CHANGED")
        view?.run {
            val dx = event.values[0]
            val dy = event.values[1]

            val potentialX = dx + sensor_worldView.x
            val potentialY = dy + sensor_worldView.y

            val newDx = when {
                potentialX > width -> width - sensor_worldView.x
                potentialX < 0 -> 0f
                else -> dx
            }

            val newDy = when {
                potentialY > height -> height - sensor_worldView.y
                potentialY < 0 -> 0f
                else -> dy
            }

            Log.v("debug", "SETUP")

            val matrix = sensor_worldView.imageMatrix
            matrix.postTranslate(newDx, newDy)
            sensor_worldView.imageMatrix = matrix
            sensor_worldView.invalidate()
        }
    }

    //endregion

    //region: ViewMask methods

    override fun showInstructionToast() {
        Toast.makeText(activity, R.string.sensor_instructions, Toast.LENGTH_LONG).show()
    }

    //endregion
}
