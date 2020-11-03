package life.league.challenge.kotlin.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import life.league.challenge.kotlin.R
import life.league.challenge.kotlin.api.Failure
import life.league.challenge.kotlin.api.Success
import life.league.challenge.kotlin.userprofile.UserProfileActivity

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

        val viewModel = MainListViewModel()
        viewModel.getPosts().observe(this,
                Observer {
                    when (it) {
                        is Failure -> Toast.makeText(this, it.errorResponse, Toast.LENGTH_SHORT).show()
                        is Success -> {
                            adapter.adapterList.clear()
                            adapter.adapterList.addAll(it.response!!)
                            adapter.notifyDataSetChanged()
                        }
                    }
                })
    }

    inner class MainListViewClickListenerImpl : MainListViewClickListener {
        override fun onAvatarUsernameClicked(postWithUserDTO: PostWithUserDTO) {
            this@MainActivity.startActivity(Intent(this@MainActivity, UserProfileActivity::class.java))
        }
    }
}
