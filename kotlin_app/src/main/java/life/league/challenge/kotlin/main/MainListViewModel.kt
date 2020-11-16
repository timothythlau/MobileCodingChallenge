package life.league.challenge.kotlin.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import life.league.challenge.kotlin.api.LeagueRepository
import life.league.challenge.kotlin.api.Outcome
import life.league.challenge.kotlin.api.Success
import life.league.challenge.kotlin.model.Post
import life.league.challenge.kotlin.model.User

class MainListViewModel(private val repository: LeagueRepository) : ViewModel() {
    private var postList: List<Post>? = null
    private var userList: List<User>? = null

    fun getPosts(): LiveData<List<PostWithUserDTO>> {
        return getPostsWithUserLiveData(repository.getPosts(), repository.getUsers())
    }

    private fun getPostsWithUserLiveData(posts: LiveData<Outcome<List<Post>>>, users: LiveData<Outcome<List<User>>>): MediatorLiveData<List<PostWithUserDTO>> {
        val result = MediatorLiveData<List<PostWithUserDTO>>()
        result.addSource(posts) {
            (it as? Success)?.response?.let {
                postList = it
                mapPostsWithUserDTO()?.let { result.postValue(it) }
            }
        }
        result.addSource(users) {
            (it as? Success)?.response?.let {
                userList = it
                mapPostsWithUserDTO()?.let { result.postValue(it) }
            }
        }
        return result
    }

    private fun mapPostsWithUserDTO(): List<PostWithUserDTO>? {
        postList?.let { postList ->
            userList?.let { userList ->
                return postList.map { post -> PostWithUserDTO(post, userList.find { user -> post.userId == user.id }) }
            }
        }

        return null
    }
}

data class PostWithUserDTO(
        val post: Post? = null,
        val user: User? = null
)