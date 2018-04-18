package nz.liamdegrey.showcase.ui.shared.common.helpers

import android.os.Handler
import android.os.Looper
import kotlin.coroutines.experimental.Continuation

/* https://gitlab.com/Starcarr/android-coroutines/blob/master/app/src/main/kotlin/com/starcarrlane/coroutines/experimental/Android.kt */
class AndroidContinuation<T>(val cont: Continuation<T>) : Continuation<T> by cont {
    override fun resume(value: T) {
        if (Looper.myLooper() == Looper.getMainLooper()) cont.resume(value)
        else Handler(Looper.getMainLooper()).post { cont.resume(value) }
    }

    override fun resumeWithException(exception: Throwable) {
        if (Looper.myLooper() == Looper.getMainLooper()) cont.resumeWithException(exception)
        else Handler(Looper.getMainLooper()).post { cont.resumeWithException(exception) }
    }
}