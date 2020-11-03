package life.league.challenge.kotlin.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import life.league.challenge.kotlin.api.Failure
import life.league.challenge.kotlin.api.Outcome
import life.league.challenge.kotlin.api.Service
import life.league.challenge.kotlin.api.Success
import life.league.challenge.kotlin.model.Post
import life.league.challenge.kotlin.model.User

class MainListViewModel : ViewModel() {
    fun getPosts(): LiveData<Outcome<List<PostWithUserDTO>>> {
        return liveData(Dispatchers.IO) {
            emit(getPostsWithUsers())
        }
    }

    private suspend fun getPostsWithUsers(): Outcome<List<PostWithUserDTO>> = coroutineScope {
        val posts = async { Service.getPosts() }
        val users = async { Service.getUsers() }

        val usersAwait = users.await()
        val postsAwait = posts.await()

        postsAwait as? Failure ?: usersAwait as? Failure
        ?: Success((postsAwait as Success).response?.map { post ->
            PostWithUserDTO(post, (usersAwait as Success).response?.find { user -> post.userId == user.id })
        })
    }
}

data class PostWithUserDTO(
        val post: Post? = null,
        val user: User? = null
)