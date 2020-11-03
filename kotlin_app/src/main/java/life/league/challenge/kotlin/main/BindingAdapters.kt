package life.league.challenge.kotlin.main

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageSrcCircleCrop")
fun ImageView.loadImageCircleCrop(url: String) {
    Glide.with(this)
            .load(url)
            .circleCrop()
            .into(this)
}