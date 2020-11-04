package life.league.challenge.kotlin.main

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

@BindingAdapter("imageSrcCircleCrop")
fun ImageView.loadImageCircleCrop(url: String?) {
    Glide.with(this)
            .load(url)
            .circleCrop()
            .into(this)
}

@BindingAdapter("imageSrc")
fun ImageView.loadImage(url: String?) {
    Picasso.get()
            .load(url)
            .into(this)
}