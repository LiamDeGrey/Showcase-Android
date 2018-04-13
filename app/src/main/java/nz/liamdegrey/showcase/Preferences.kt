package nz.liamdegrey.showcase

import android.content.SharedPreferences

class Preferences(private val sharedPreferences: SharedPreferences) {
    var hasViewedHomeActivity: Boolean
        get() = sharedPreferences.getBoolean(PREF_HAS_VIEWED_HOME_ACTIVITY, false)
        set(value) = sharedPreferences.edit().putBoolean(PREF_HAS_VIEWED_HOME_ACTIVITY, value).apply()

    companion object {
        private const val PREF_HAS_VIEWED_HOME_ACTIVITY = "hasViewedHomeActivity"
    }
}