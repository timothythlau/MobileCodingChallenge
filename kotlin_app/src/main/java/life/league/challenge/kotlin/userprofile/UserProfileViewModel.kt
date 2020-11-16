package life.league.challenge.kotlin.userprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import life.league.challenge.kotlin.api.LeagueRepository
import life.league.challenge.kotlin.api.Outcome
import life.league.challenge.kotlin.model.Album
import life.league.challenge.kotlin.model.Photo
import life.league.challenge.kotlin.model.User

class UserProfileViewModel(private val leagueRepository: LeagueRepository) : ViewModel() {
    val photosLiveData = MutableLiveData<List<AlbumPhotoDTO>>()
    var albumList: List<Album> = emptyList()

    fun setUser(user: User?): LiveData<UserProfileDTO> {
        return liveData {
            user?.let {
                emit(UserProfileDTO(it.id, it.name, it.avatar, it.email, it.phone, it.website, it.company?.name, it.company?.catchPhrase, it.company?.bs))
            }
        }
    }

    fun getAlbumsForUser(userId: Int): LiveData<Outcome<List<Album>>> {
        return leagueRepository.getAlbums(userId)
    }

    fun getNextAlbumThumbnails(): LiveData<Outcome<List<Photo>>>? {
        val indexToGet = when (val lastRetrievedAlbumId = photosLiveData.value?.lastOrNull()?.albumId) {
            null -> 0
            else -> albumList.indexOfLast { it.id == lastRetrievedAlbumId }.plus(1).takeIf { it < albumList.size }
        }

        indexToGet?.let {
            return leagueRepository.getPhotos(albumList[it].id!!)
        }

        return null
    }

    fun setAlbumThumbnails(photos: List<Photo>) {
        val newPhotosToAdd = photos.map { AlbumPhotoDTO(it.id, it.albumId, it.url, it.thumbnailUrl) }
        photosLiveData.postValue(photosLiveData.value?.plus(newPhotosToAdd) ?: newPhotosToAdd)
    }
}

data class UserProfileDTO(
        val userId: Int? = null,
        val name: String? = null,
        val avatar: String? = null,
        val email: String? = null,
        val phone: String? = null,
        val website: String? = null,
        val companyName: String? = null,
        val companyCatchphrase: String? = null,
        val companyBs: String? = null
)

data class AlbumPhotoDTO(
        val id: Int? = null,
        val albumId: Int? = null,
        val url: String? = null,
        val thumbnailUrl: String? = null
)