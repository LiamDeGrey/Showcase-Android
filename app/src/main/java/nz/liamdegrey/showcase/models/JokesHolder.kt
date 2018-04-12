package nz.liamdegrey.showcase.models

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.android.parcel.Parcelize

@Parcelize
data class JokesHolder(@JsonProperty("value") val jokes: List<Joke>) : Parcelable