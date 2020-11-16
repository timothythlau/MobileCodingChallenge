package life.league.challenge.kotlin.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import life.league.challenge.kotlin.model.Album
import life.league.challenge.kotlin.model.Photo
import life.league.challenge.kotlin.model.Post
import life.league.challenge.kotlin.model.User

interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg obj: T)
}

@Dao
abstract class UserDao : BaseDao<User> {
    @Query("SELECT * FROM User")
    abstract fun getAll(): LiveData<List<User>>
}

@Dao
abstract class PostDao : BaseDao<Post> {
    @Query("SELECT * FROM Post")
    abstract fun getAll(): LiveData<List<Post>>
}

@Dao
abstract class AlbumDao : BaseDao<Album> {
    @Query("SELECT * FROM  Album WHERE userId = :userId")
    abstract fun getForUser(userId: Int): LiveData<List<Album>>
}

@Dao
abstract class PhotoDao : BaseDao<Photo> {
    @Query("SELECT * FROM Photo WHERE albumId = :albumId")
    abstract fun getForAlbum(albumId: Int): LiveData<List<Photo>>
}