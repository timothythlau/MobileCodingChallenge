package life.league.challenge.kotlin.userprofile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    lateinit var viewModel: UserProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        val thumbnailAdapter = UserProfileThumbnailAdapter()
        thumbnailAdapter.setHasStableIds(true)
        binding.albumRecyclerView.adapter = thumbnailAdapter
        binding.albumRecyclerView.layoutManager = GridLayoutManager(this, 3)

        viewModel = UserProfileViewModel()
        viewModel.setUser(intent.getParcelableExtra(USER_EXTRA)).observe(this, Observer {
            binding.userProfileDto = it
            binding.executePendingBindings()

            viewModel.getAlbumsForUser(it.userId).observe(this, Observer {
                viewModel.albumList = it
                viewModel.getNextAlbumThumbnails()
            })
        })

        viewModel.photosLiveData.observe(this, Observer {
            thumbnailAdapter.itemList.clear()
            thumbnailAdapter.itemList.addAll(it)
            thumbnailAdapter.notifyDataSetChanged()
        })

        binding.albumRecyclerView.addOnScrollListener(AlbumScrollListener())
    }

    inner class AlbumScrollListener : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            (recyclerView.layoutManager as? GridLayoutManager)?.let {
                if (it.findLastVisibleItemPosition() == it.itemCount - 1) {
                    viewModel.getNextAlbumThumbnails()
                }
            }
        }
    }
}