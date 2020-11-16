package life.league.challenge.kotlin.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class User(
        @PrimaryKey val id: Int? = null,
        val avatar: String? = null,
        val name: String? = null,
        val username: String? = null,
        val email: String? = null,
        @Embedded val address: Address? = null,
        val phone: String? = null,
        val website: String? = null,
        @Embedded val company: Company? = null
) : Parcelable