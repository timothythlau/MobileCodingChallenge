package life.league.challenge.kotlin.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import life.league.challenge.kotlin.api.SingletonHolder
import life.league.challenge.kotlin.model.Album
import life.league.challenge.kotlin.model.Photo
import life.league.challenge.kotlin.model.Post
import life.league.challenge.kotlin.model.User

@Database(entities = [User::class, Post::class, Album::class, Photo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao
    abstract fun albumDao(): AlbumDao
    abstract fun photoDao(): PhotoDao

    companion object : SingletonHolder<AppDatabase, Context>({
        Room.databaseBuilder(it.applicationContext,
                AppDatabase::class.java, "Sample.db")
                .build()
    })
}