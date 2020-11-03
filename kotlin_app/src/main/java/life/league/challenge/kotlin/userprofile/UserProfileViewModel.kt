package life.league.challenge.kotlin.userprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import life.league.challenge.kotlin.model.User

class UserProfileViewModel : ViewModel() {
    fun setUser(user: User?): LiveData<UserProfileDTO> {
        return liveData {
            user?.let {
                emit(UserProfileDTO(
                        name = user.name,
                        avatar = user.avatar,
                        email = user.email,
                        phone = user.phone,
                        website = user.website
                ))
            }
        }
    }
}

data class UserProfileDTO(
        val name: String?,
        val avatar: String?,
        val email: String?,
        val phone: String?,
        val website: String?
)