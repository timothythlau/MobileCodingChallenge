package life.league.challenge.kotlin.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import life.league.challenge.kotlin.db.AppDatabase
import life.league.challenge.kotlin.model.Album
import life.league.challenge.kotlin.model.Photo
import life.league.challenge.kotlin.model.Post
import life.league.challenge.kotlin.model.User

class LeagueRepository(private val database: AppDatabase, private val appExecutors: AppExecutors) {
    private val service = Service

    fun getUsers(): LiveData<Outcome<List<User>>> = object : NetworkBoundResource<List<User>, List<User>>(appExecutors) {
        override fun saveCallResult(item: List<User>) {
            item.forEach { database.userDao().insert(it) }
        }

        override fun loadFromDb(): LiveData<List<User>> {
            return database.userDao().getAll()
        }

        override fun createCall(): LiveData<Outcome<List<User>>> {
            return liveData { emit(service.getUsers()) }
        }
    }.asLiveData()

    fun getPosts(): LiveData<Outcome<List<Post>>> = object : NetworkBoundResource<List<Post>, List<Post>>(appExecutors) {
        override fun saveCallResult(item: List<Post>) {
            item.forEach { database.postDao().insert(it) }
        }

        override fun loadFromDb(): LiveData<List<Post>> {
            return database.postDao().getAll()
        }

        override fun createCall(): LiveData<Outcome<List<Post>>> {
            return liveData { emit(service.getPosts()) }
        }
    }.asLiveData()

    fun getAlbums(userId: Int): LiveData<Outcome<List<Album>>> = object : NetworkBoundResource<List<Album>, List<Album>>(appExecutors) {
        override fun saveCallResult(item: List<Album>) {
            item.forEach { database.albumDao().insert(it) }
        }

        override fun loadFromDb(): LiveData<List<Album>> {
            return database.albumDao().getForUser(userId)
        }

        override fun createCall(): LiveData<Outcome<List<Album>>> {
            return liveData { emit(service.getAlbums(userId)) }
        }
    }.asLiveData()

    fun getPhotos(albumId: Int): LiveData<Outcome<List<Photo>>> = object : NetworkBoundResource<List<Photo>, List<Photo>>(appExecutors) {
        override fun saveCallResult(item: List<Photo>) {
            item.forEach { database.photoDao().insert(it) }
        }

        override fun loadFromDb(): LiveData<List<Photo>> {
            return database.photoDao().getForAlbum(albumId)
        }

        override fun createCall(): LiveData<Outcome<List<Photo>>> {
            return liveData { emit(service.getPhotos(albumId)) }
        }
    }.asLiveData()
}

