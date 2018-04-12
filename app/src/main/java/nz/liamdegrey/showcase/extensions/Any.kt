package nz.liamdegrey.showcase.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.View
import android.view.inputmethod.InputMethodManager
import nz.liamdegrey.showcase.Application

fun hideKeyboard(view: View) {
    val inputMethodManager = Application.instance.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun isNetworkConnected(context: Context): Boolean {
    return try {
        getNetworkInfo(context).isConnected
    } catch (e: NullPointerException) {
        false
    }
}

private fun getNetworkInfo(context: Context): NetworkInfo {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return connectivityManager.activeNetworkInfo
}