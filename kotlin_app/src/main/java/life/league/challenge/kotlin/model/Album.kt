package life.league.challenge.kotlin.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Album(
        val userId: Int? = null,
        @PrimaryKey val id: Int? = null,
        val title: String? = null
)