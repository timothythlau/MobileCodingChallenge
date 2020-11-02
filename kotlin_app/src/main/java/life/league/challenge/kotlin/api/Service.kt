package life.league.challenge.kotlin.api

import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import life.league.challenge.kotlin.model.Account
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object Service {

    private const val HOST = "https://engineering.league.dev/challenge/api/"
    private const val TAG = "Service"

    private val api: Api by lazy {
        val retrofit = Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        retrofit.create<Api>(Api::class.java)
    }

    fun login(username: String, password: String, callback: (result: Outcome<Account>) -> Unit) {

        val credentials = "$username:$password"
        val auth = "Basic " + Base64.encodeToString(credentials.toByteArray(),
                Base64.NO_WRAP)
        Thread(Runnable {
            try {
                val response: Response<Account> = api.login(auth).execute()
                Handler(Looper.getMainLooper()).post {
                    if (response.isSuccessful) {
                        callback.invoke(Success(response.body()))
                    } else {
                        callback.invoke(Failure(response.message()))
                    }
                }
            } catch (e: IOException) {
                Log.e(TAG, "Login failed", e)
                callback.invoke(Failure(e.message ?: ""))
            }
        }).start()
    }
}
