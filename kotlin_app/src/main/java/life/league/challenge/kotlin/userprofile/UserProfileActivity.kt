package life.league.challenge.kotlin.userprofile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import life.league.challenge.kotlin.api.AppExecutors
import life.league.challenge.kotlin.api.LeagueRepository
import life.league.challenge.kotlin.api.Outcome
import life.league.challenge.kotlin.api.Success
import life.league.challenge.kotlin.databinding.ActivityUserProfileBinding
import life.league.challenge.kotlin.db.AppDatabase
import life.league.challenge.kotlin.model.Photo
import life.league.challenge.kotlin.model.User

const val USER_EXTRA = "USER_EXTRA"

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

        title = "User Profile"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.photoContainerListener = PhotoContainerListenerImpl()
        binding.photoContainerVisibility = View.GONE
        binding.executePendingBindings()

        val thumbnailAdapter = UserProfileThumbnailAdapter(UserProfileThumbnailClickListenerImpl())
        thumbnailAdapter.setHasStableIds(true)
        binding.albumRecyclerView.adapter = thumbnailAdapter
        binding.albumRecyclerView.layoutManager = GridLayoutManager(this, 3)

        viewModel = UserProfileViewModel(LeagueRepository(AppDatabase.getInstance(applicationContext), AppExecutors.getInstance(applicationContext)))
        viewModel.setUser(intent.getParcelableExtra(USER_EXTRA)).observe(this, Observer {
            binding.userProfileDto = it
            binding.executePendingBindings()

            viewModel.getAlbumsForUser(it.userId!!).observe(this, Observer {
                when (it) {
                    is Success -> {
                        viewModel.albumList = it.response!!
                        viewModel.getNextAlbumThumbnails()?.observe(this, PhotosObserver())
                    }
                }
            })
        })

        viewModel.photosLiveData.observe(this, Observer {
            thumbnailAdapter.itemList.clear()
            thumbnailAdapter.itemList.addAll(it)
            thumbnailAdapter.notifyDataSetChanged()
        })

        binding.albumRecyclerView.addOnScrollListener(AlbumScrollListener())
    }

    inner class PhotosObserver : Observer<Outcome<List<Photo>>> {
        override fun onChanged(t: Outcome<List<Photo>>?) {
            when (t) {
                is Success -> t.response?.let { viewModel.setAlbumThumbnails(it) }
            }
        }
    }

    inner class AlbumScrollListener : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            (recyclerView.layoutManager as? GridLayoutManager)?.let {
                if (it.findLastVisibleItemPosition() == it.itemCount - 1) {
                    viewModel.getNextAlbumThumbnails()?.observe(this@UserProfileActivity, PhotosObserver())
                }
            }
        }
    }

    inner class UserProfileThumbnailClickListenerImpl : UserProfileThumbnailClickListener {
        override fun onThumbnailClicked(albumPhotoDTO: AlbumPhotoDTO) {
            binding.fullPhotoUrl = albumPhotoDTO.url
            binding.photoContainerVisibility = View.VISIBLE
            binding.executePendingBindings()
        }
    }

    inner class PhotoContainerListenerImpl : PhotoContainerListener {
        override fun onClick() {
            binding.photoContainerVisibility = View.GONE
            binding.executePendingBindings()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}

interface PhotoContainerListener {
    fun onClick()
}