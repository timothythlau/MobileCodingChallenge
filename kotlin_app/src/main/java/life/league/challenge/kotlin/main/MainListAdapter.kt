package life.league.challenge.kotlin.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import life.league.challenge.kotlin.databinding.PostWithUserBinding

class MainListAdapter(private val listener: MainListViewClickListener) : RecyclerView.Adapter<MainListViewHolder>() {
    val adapterList = ArrayList<PostWithUserDTO>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainListViewHolder {
        return MainListViewHolder(PostWithUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return adapterList.size
    }

    override fun onBindViewHolder(holder: MainListViewHolder, position: Int) {
        holder.bind(adapterList[position], listener)
    }
}

class MainListViewHolder(private val binding: PostWithUserBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(postWithUserDTO: PostWithUserDTO, listener: MainListViewClickListener) {
        binding.postWithUser = postWithUserDTO
        binding.listener = listener
        binding.executePendingBindings()
    }
}

interface MainListViewClickListener {
    fun onAvatarUsernameClicked(postWithUserDTO: PostWithUserDTO)
}