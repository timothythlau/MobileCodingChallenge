package life.league.challenge.kotlin.userprofile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import life.league.challenge.kotlin.databinding.ActivityUserProfileBinding
import life.league.challenge.kotlin.model.User

val USER_EXTRA = "USER_EXTRA"

fun Context.getUserProfileActivityIntent(user: User?): Intent {
    return Intent(this, UserProfileActivity::class.java)
            .apply {
                putExtra(USER_EXTRA, user)
            }
}

class UserProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        val viewModel = UserProfileViewModel()
        viewModel.setUser(intent.getParcelableExtra(USER_EXTRA)).observe(this, Observer {
            binding.userProfileDto = it
            binding.executePendingBindings()
        })
    }
}