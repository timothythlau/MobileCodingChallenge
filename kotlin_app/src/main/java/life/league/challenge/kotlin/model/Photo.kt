package life.league.challenge.kotlin.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Photo(
        val albumId: Int? = null,
        @PrimaryKey val id: Int? = null,
        val title: String? = null,
        val url: String? = null,
        val thumbnailUrl: String? = null
)