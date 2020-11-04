package life.league.challenge.kotlin.userprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import life.league.challenge.kotlin.api.Service
import life.league.challenge.kotlin.api.Success
import life.league.challenge.kotlin.model.User

class UserProfileViewModel : ViewModel() {
    val mergedLiveData = MediatorLiveData<UserProfileDTO>()

    fun setUser(user: User?): LiveData<UserProfileDTO> {
        mergedLiveData.addSource(getUserLiveData(user)) { user ->
            mergedLiveData.postValue(
                    UserProfileDTO(
                            userId = user.id,
                            name = user.name,
                            avatar = user.avatar,
                            email = user.email,
                            phone = user.phone,
                            website = user.website
                    )
            )

            mergedLiveData.addSource(getAlbumAndPhotosForUser(mergedLiveData.value?.userId)) { albumPhotoList ->
                val newAlbumList = mergedLiveData.value?.albumPhotoDtoList?.toMutableList()?.apply { addAll(albumPhotoList) }
                        ?: albumPhotoList
                mergedLiveData.postValue(mergedLiveData.value?.copy(albumPhotoDtoList = newAlbumList))
            }
        }
        return mergedLiveData
    }

    private fun getUserLiveData(user: User?): LiveData<User> {
        return liveData { user?.let { emit(it) } }
    }

    private fun getAlbumAndPhotosForUser(userId: Int?): LiveData<List<AlbumPhotoDTO>> {
        return liveData(Dispatchers.IO) {
            val albumsOutcome = Service.getAlbums(userId)
            when (albumsOutcome) {
                is Success -> {
                    albumsOutcome.response?.forEach {
                        val photos = Service.getPhotos(it.id)
                        if (photos is Success) {
                            emit(photos.response!!.map { AlbumPhotoDTO(it.albumId, it.url, it.thumbnailUrl) })
                        }
                    }
                }
            }
        }
    }
}

data class UserProfileDTO(
        val userId: Int? = null,
        val name: String? = null,
        val avatar: String? = null,
        val email: String? = null,
        val phone: String? = null,
        val website: String? = null,
        val albumPhotoDtoList: List<AlbumPhotoDTO>? = null
)

data class AlbumPhotoDTO(
        val albumId: Int? = null,
        val url: String? = null,
        val thumbnailUrl: String? = null
)