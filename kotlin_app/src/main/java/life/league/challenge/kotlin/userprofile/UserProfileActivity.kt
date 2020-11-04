package life.league.challenge.kotlin.userprofile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
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

        val thumbnailAdapter = UserProfileThumbnailAdapter()
        binding.albumRecyclerView.adapter = thumbnailAdapter
        binding.albumRecyclerView.layoutManager = GridLayoutManager(this, 3)

        val viewModel = UserProfileViewModel()
        viewModel.setUser(intent.getParcelableExtra(USER_EXTRA)).observe(this, Observer {
            binding.userProfileDto = it
            binding.executePendingBindings()

            thumbnailAdapter.itemList.clear()
            it.albumPhotoDtoList?.let { thumbnailAdapter.itemList.addAll(it) }
            thumbnailAdapter.notifyDataSetChanged()
        })
    }
}