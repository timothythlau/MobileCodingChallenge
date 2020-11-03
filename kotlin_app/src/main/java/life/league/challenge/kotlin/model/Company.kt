package life.league.challenge.kotlin.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Company(
        val name: String? = null,
        val catchPhrase: String? = null,
        val bs: String? = null
) : Parcelable