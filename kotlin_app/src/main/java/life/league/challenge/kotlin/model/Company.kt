package life.league.challenge.kotlin.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Company(
        @ColumnInfo(name = "companyName") val name: String? = null,
        val catchPhrase: String? = null,
        val bs: String? = null
) : Parcelable