package life.league.challenge.kotlin.api

import life.league.challenge.kotlin.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

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

    @GET("albums")
    suspend fun albums(@Header(accessTokenHeader) accessToken: String?, @Query("userId") userId: Int?): Response<List<Album>>

    @GET("photos")
    suspend fun photos(@Header(accessTokenHeader) accessToken: String?, @Query("albumId") albumId: Int?): Response<List<Photo>>
}
