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
            val user = (usersAwait as Success).response?.find { it.id == post.userId }
            PostWithUserDTO(
                    userId = post.userId,
                    avatar = user?.avatar,
                    username = user?.username,
                    title = post.title,
                    body = post.body
            )
        })
    }
}

data class PostWithUserDTO(
        val userId: Int? = null,
        val avatar: String? = null,
        val username: String? = null,
        val title: String? = null,
        val body: String? = null
)