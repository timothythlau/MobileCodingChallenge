package life.league.challenge.kotlin.userprofile

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import kotlinx.android.synthetic.main.activity_user_profile.view.*

class UserProfileCardViewPagerAdapter : PagerAdapter() {
    override fun getCount(): Int {
        return 2
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return when (position) {
            0 -> container.user_card
            1 -> container.company_card
            else -> Object()
        }
    }
}