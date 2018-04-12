package nz.liamdegrey.showcase.models

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.android.parcel.Parcelize

@Parcelize
data class JokeHolder(@JsonProperty("type") private val valid: Boolean,
                      @JsonProperty("value") val joke: Joke) : Parcelable