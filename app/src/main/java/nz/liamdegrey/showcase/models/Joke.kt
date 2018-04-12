package nz.liamdegrey.showcase.models

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Joke(@JsonProperty("id") private val id: Long,
                @JsonProperty("joke") val body: String) : Parcelable