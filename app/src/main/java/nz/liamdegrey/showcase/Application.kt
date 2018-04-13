package nz.liamdegrey.showcase

import android.preference.PreferenceManager
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import nz.liamdegrey.showcase.brokers.JokeBroker
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit
import android.app.Application as BaseApplication

class Application : BaseApplication() {
    val jokeBroker by lazy { JokeBroker(BuildConfig.SERVICE_BASEURL_KEY, okHttpClient, jsonConverterFactory) }

    val preferences by lazy { Preferences(PreferenceManager.getDefaultSharedPreferences(this)) }

    private val okHttpClient by lazy { createOkHttpClient() }
    private val jsonConverterFactory by lazy { createJsonConverterFactory() }


    override fun onCreate() {
        instance = this

        super.onCreate()
    }

    private fun createOkHttpClient(): OkHttpClient =
            OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor()
                            .setLevel(
                                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                                    else HttpLoggingInterceptor.Level.NONE
                            ))
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build()

    private fun createJsonConverterFactory(): Converter.Factory =
            JacksonConverterFactory.create(ObjectMapper().apply {
                registerModule(SimpleModule())
                configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            })

    companion object {
        lateinit var instance: Application
            private set
    }
}