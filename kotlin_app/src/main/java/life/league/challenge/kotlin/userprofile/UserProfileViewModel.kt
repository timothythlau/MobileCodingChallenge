package life.league.challenge.kotlin.userprofile

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import life.league.challenge.kotlin.api.Service
import life.league.challenge.kotlin.api.Success
import life.league.challenge.kotlin.model.Album
import life.league.challenge.kotlin.model.User

class UserProfileViewModel : ViewModel() {
    val photosLiveData = MutableLiveData<List<AlbumPhotoDTO>>()
    var albumList: List<Album> = emptyList()

    fun setUser(user: User?): LiveData<UserProfileDTO> {
        return liveData {
            user?.let {
                emit(UserProfileDTO(it.id, it.name, it.avatar, it.email, it.phone, it.website))
            }
        }
    }

    fun getAlbumsForUser(userId: Int?): LiveData<List<Album>> {
        return liveData {
            userId?.let {
                val albumsOutcome = Service.getAlbums(it)
                (albumsOutcome as? Success)?.response?.let { emit(it) }
            }
        }
    }

    fun getNextAlbumThumbnails() {
        viewModelScope.launch {
            val indexToGet = when (val lastRetrievedAlbumId = photosLiveData.value?.lastOrNull()?.albumId) {
                null -> 0
                else -> albumList.indexOfLast { it.id == lastRetrievedAlbumId }.plus(1).takeIf { it < albumList.size }
            }

            indexToGet?.let {
                val photosOutcome = Service.getPhotos(albumList[it].id)
                if (photosOutcome is Success) {
                    photosOutcome.response?.let {
                        val newPhotosToAdd = it.map { AlbumPhotoDTO(it.id, it.albumId, it.url, it.thumbnailUrl) }
                        photosLiveData.postValue(photosLiveData.value?.plus(newPhotosToAdd)
                                ?: newPhotosToAdd)
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
        val website: String? = null
)

data class AlbumPhotoDTO(
        val id: Int? = null,
        val albumId: Int? = null,
        val url: String? = null,
        val thumbnailUrl: String? = null
)