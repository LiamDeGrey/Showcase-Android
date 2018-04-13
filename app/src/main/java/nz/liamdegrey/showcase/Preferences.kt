package nz.liamdegrey.showcase

import android.content.SharedPreferences

class Preferences(private val sharedPreferences: SharedPreferences) {
    var hasViewedSensorFragment: Boolean
        get() = sharedPreferences.getBoolean(PREF_HAS_VIEWED_SENSOR_FRAGMENT, false)
        set(value) = sharedPreferences.edit().putBoolean(PREF_HAS_VIEWED_SENSOR_FRAGMENT, value).apply()

    companion object {
        private const val PREF_HAS_VIEWED_SENSOR_FRAGMENT = "hasViewedSensorFragment"
    }
}