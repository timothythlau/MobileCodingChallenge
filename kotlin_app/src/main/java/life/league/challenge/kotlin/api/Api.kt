package life.league.challenge.kotlin.api

import life.league.challenge.kotlin.model.Account
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface Api {

    @GET("login")
    fun login(@Header("Authorization") credentials: String?): Call<Account>

}
