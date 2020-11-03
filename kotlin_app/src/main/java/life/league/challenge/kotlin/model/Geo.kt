package life.league.challenge.kotlin.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Geo(
        val lat: String? = null,
        val lng: String? = null
) : Parcelable