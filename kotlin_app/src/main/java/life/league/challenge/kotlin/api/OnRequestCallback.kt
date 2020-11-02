package life.league.challenge.kotlin.api

sealed class Outcome<out T>
data class Success<T>(val response: T?) : Outcome<T>()
data class Failure(val errorResponse: String) : Outcome<Nothing>()

