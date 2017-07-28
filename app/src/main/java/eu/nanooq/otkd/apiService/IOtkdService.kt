package eu.nanooq.otkd.apiService

import eu.nanooq.otkd.models.API.UserCaptain
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *
 * Created by rd on 28/07/2017.
 */


interface IOtkdService {

    @GET("index.php")
    fun getCaptainLogin(
            @Query("option") option: String  = "com_odtatierkdunaju",
            @Query("task") task: String  = "nanook.login",
            @Query("username") username: String ,
            @Query("password") password: String
    ): Observable<Response<UserCaptain>>
}
