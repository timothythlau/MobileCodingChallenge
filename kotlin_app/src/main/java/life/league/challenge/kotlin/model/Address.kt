package life.league.challenge.kotlin.model

import android.os.Parcelable
import androidx.room.Embedded
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(
        val street: String? = null,
        val suite: String? = null,
        val city: String? = null,
        val zipcode: String? = null,
        @Embedded val geo: Geo? = null
) : Parcelable