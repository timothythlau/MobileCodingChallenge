package life.league.challenge.kotlin.api

import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import life.league.challenge.kotlin.model.Account
import life.league.challenge.kotlin.model.Post
import life.league.challenge.kotlin.model.User
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object Service {

    private const val HOST = "https://engineering.league.dev/challenge/api/"
    private const val TAG = "Service"
    var accessToken = ""

    private val api: Api by lazy {
        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(HOST)
                .client(okHttpClient)
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

    suspend fun login(username: String, password: String): Outcome<Account> {
        val credentials = "$username:$password"
        val auth = "Basic " + Base64.encodeToString(credentials.toByteArray(),
                Base64.NO_WRAP)
        return getOutcome(api.suspendLogin(auth))
                .also { outcome ->
                    if (outcome is Success) {
                        outcome.response?.apiKey?.let { accessToken = it }
                    }
                }
    }

    suspend fun getUsers(): Outcome<List<User>> {
        checkAccessToken()
        return getOutcome(api.users(accessToken))
    }

    suspend fun getPosts(): Outcome<List<Post>> {
        checkAccessToken()
        return getOutcome(api.posts(accessToken))
    }

    private suspend fun checkAccessToken() {
        if (accessToken.isEmpty()) {
            login("Hello", "World")
        }
    }

    private fun <T> getOutcome(response: Response<T>): Outcome<T> {
        return if (response.isSuccessful) {
            Success(response.body())
        } else {
            Failure(response.message())
        }
    }
}
