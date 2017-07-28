package eu.nanooq.otkd.apiService

import eu.nanooq.otkd.di.modules.RetrofitModule
import eu.nanooq.otkd.models.API.UserCaptain
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 *
 * Created by rd on 28/07/2017.
 */
@Singleton
class ApiProvider
@Inject constructor(
        @Named(RetrofitModule.RETROFIT_MOSHI_SERVICE)
        var api: IOtkdService
) {

    fun loginCaptain(userName: String, password: String): Observable<UserCaptain> {
        return api.getCaptainLogin(username = userName, password = password)
                .flatMap {
                    if (it.isSuccessful && it.body() != null) {
                        Observable.just(it.body())
                    } else {
                        Observable.error<UserCaptain>(Throwable("${it.message()} ${it.errorBody()}"))
                    }
                }
    }
}
