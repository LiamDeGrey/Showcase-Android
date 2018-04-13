package nz.liamdegrey.showcase.ui.home.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_sensor.*
import nz.liamdegrey.showcase.R
import nz.liamdegrey.showcase.ui.common.BaseFragment
import nz.liamdegrey.showcase.ui.common.views.Toolbar
import java.util.*

class SensorFragment : BaseFragment<SensorPresenter, SensorViewMask>(),
        SensorViewMask, SensorEventListener {
    private val sensorManager by lazy { activity!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    private val sensor by lazy { sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) }

    private val sensorValues by lazy { LinkedList<FloatArray>() }

    override val layoutResId: Int
        get() = R.layout.fragment_sensor


    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
    }

    override fun onResume() {
        super.onResume()

        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST)
    }

    override fun onPause() {
        super.onPause()

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
        view?.run {
            sensorValues.offer(floatArrayOf(event.values[0], event.values[1]))

            while (sensorValues.count() > MAX_STORED_VALUES) {
                sensorValues.poll()
            }

            var dx = 0f
            var dy = 0f

            sensorValues.forEach {
                dx += it[0]
                dy += it[1]
            }

            dx /= sensorValues.count()
            dy /= sensorValues.count()

            dx *= SENSOR_VALUE_MULTIPLIER
            dy *= SENSOR_VALUE_MULTIPLIER

            val currentTranslationValues = FloatArray(9)
            sensor_worldView.imageMatrix.getValues(currentTranslationValues)

            sensor_worldView.imageMatrix = sensor_worldView.imageMatrix.apply {
                postTranslate(dx, dy)
            }
            sensor_worldView.invalidate()
        }
    }

    //endregion

    companion object {
        private const val SENSOR_VALUE_MULTIPLIER = 5f
        private const val MAX_STORED_VALUES = 25
    }

    //region: ViewMask methods

    override fun showInstructionToast() {
        Toast.makeText(activity, R.string.sensor_instructions, Toast.LENGTH_LONG).show()
    }

    //endregion
}
