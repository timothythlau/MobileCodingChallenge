package life.league.challenge.kotlin.userprofile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import life.league.challenge.kotlin.databinding.AlbumThumbnailListViewBinding

class UserProfileThumbnailAdapter(val listener: UserProfileThumbnailClickListener) : RecyclerView.Adapter<UserProfileThumbnailViewHolder>() {
    val itemList = ArrayList<AlbumPhotoDTO>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserProfileThumbnailViewHolder {
        return UserProfileThumbnailViewHolder(AlbumThumbnailListViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: UserProfileThumbnailViewHolder, position: Int) {
        holder.bind(itemList[position], listener)
    }

    override fun getItemId(position: Int): Long {
        return itemList[position].id!!.toLong()
    }
}

class UserProfileThumbnailViewHolder(private val binding: AlbumThumbnailListViewBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(albumPhotoDTO: AlbumPhotoDTO, listener: UserProfileThumbnailClickListener) {
        binding.albumPhotoDto = albumPhotoDTO
        binding.listener = listener
        binding.executePendingBindings()
    }
}

interface UserProfileThumbnailClickListener {
    fun onThumbnailClicked(albumPhotoDTO: AlbumPhotoDTO)
}