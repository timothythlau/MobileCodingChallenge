package life.league.challenge.kotlin.api

import life.league.challenge.kotlin.model.Account
import life.league.challenge.kotlin.model.Post
import life.league.challenge.kotlin.model.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface Api {
    companion object {
        private const val accessTokenHeader = "x-access-token"
    }

    @GET("login")
    fun login(@Header("Authorization") credentials: String?): Call<Account>

    @GET("login")
    suspend fun suspendLogin(@Header("Authorization") credentials: String?): Response<Account>

    @GET("users")
    suspend fun users(@Header(accessTokenHeader) accessToken: String?): Response<List<User>>

    @GET("posts")
    suspend fun posts(@Header(accessTokenHeader) accessToken: String?): Response<List<Post>>
}
