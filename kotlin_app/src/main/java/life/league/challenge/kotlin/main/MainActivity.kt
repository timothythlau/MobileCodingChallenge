package life.league.challenge.kotlin.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import life.league.challenge.kotlin.R
import life.league.challenge.kotlin.api.AppExecutors
import life.league.challenge.kotlin.api.LeagueRepository
import life.league.challenge.kotlin.db.AppDatabase
import life.league.challenge.kotlin.userprofile.getUserProfileActivityIntent

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        val adapter = MainListAdapter(MainListViewClickListenerImpl())
        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_view.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        val viewModel = MainListViewModel(LeagueRepository(AppDatabase.getInstance(applicationContext), AppExecutors.getInstance(applicationContext)))
        viewModel.getPosts().observe(this,
                Observer {
                    adapter.adapterList.clear()
                    adapter.adapterList.addAll(it)
                    adapter.notifyDataSetChanged()
                })

    }

    inner class MainListViewClickListenerImpl : MainListViewClickListener {
        override fun onAvatarUsernameClicked(postWithUserDTO: PostWithUserDTO) {
            this@MainActivity.startActivity(this@MainActivity.getUserProfileActivityIntent(postWithUserDTO.user))
        }
    }
}
