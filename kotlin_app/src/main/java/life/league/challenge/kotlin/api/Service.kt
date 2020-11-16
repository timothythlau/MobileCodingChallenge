package life.league.challenge.kotlin.api

import android.util.Base64
import life.league.challenge.kotlin.model.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Service {

    private const val HOST = "https://engineering.league.dev/challenge/api/"
    private var accessToken = ""

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

    private suspend fun login(username: String, password: String): Response<Account> {
        val credentials = "$username:$password"
        val auth = "Basic " + Base64.encodeToString(credentials.toByteArray(),
                Base64.NO_WRAP)
        return api.suspendLogin(auth)
    }

    suspend fun getUsers(): Outcome<List<User>> {
        (checkAccessToken() as? Failure)?.let { return it }
        return getOutcome { api.users(accessToken) }
    }

    suspend fun getPosts(): Outcome<List<Post>> {
        (checkAccessToken() as? Failure)?.let { return it }
        return getOutcome { api.posts(accessToken) }
    }

    suspend fun getAlbums(userId: Int?): Outcome<List<Album>> {
        (checkAccessToken() as? Failure)?.let { return it }
        return getOutcome { api.albums(accessToken, userId) }
    }

    suspend fun getPhotos(albumId: Int?): Outcome<List<Photo>> {
        (checkAccessToken() as? Failure)?.let { return it }
        return getOutcome { api.photos(accessToken, albumId) }
    }

    private suspend fun checkAccessToken(): Outcome<String> {
        if (accessToken.isEmpty()) {
            when (val outcome = getOutcome { login("Hello", "World") }) {
                is Success -> outcome.response?.apiKey?.let { accessToken = it }
                is Failure -> return outcome
            }
        }

        return Success(accessToken)
    }

    private suspend fun <T> getOutcome(apiMethod: suspend () -> Response<T>): Outcome<T> {
        return try {
            val response = apiMethod.invoke()
            if (response.isSuccessful) {
                Success(response.body())
            } else {
                Failure(response.message())
            }
        } catch (e: Exception) {
            Failure(e.message)
        }
    }
}
