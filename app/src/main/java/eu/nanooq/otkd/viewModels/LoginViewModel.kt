package eu.nanooq.otkd.viewModels

import android.os.Bundle
import android.util.Base64
import com.androidhuman.rxfirebase2.database.data
import eu.nanooq.otkd.viewModels.base.ActivityViewModel
import timber.log.Timber
import java.nio.charset.Charset
import eu.nanooq.otkd.models.API.UserRunner
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit


/**
 *
 * Created by rd on 23/07/2017.
 */
class LoginViewModel : ActivityViewModel<ILoginView>() {


    lateinit var mSub: Disposable

    companion object {
        const val RUNNERS_DB = "login_runner_" // TODO it will be login_runner in prod ver
    }

    var mIsUserCaptain: Boolean = false

    override fun onCreate(arguments: Bundle?, savedInstanceState: Bundle?) {
        Timber.d("onCreate")

        super.onCreate(arguments, savedInstanceState)

        val cap = arguments?.getBoolean(SplashViewModel.CAPTAIN)
        if (cap == null) {
            view?.finishLoginActivity()
        } else {
            mIsUserCaptain = cap
        }

//        val sections = mFirebaseHelper.mFBDBReference.child("sections")
    }

    override fun onStart() {
        Timber.d("onStart")
        super.onStart()

        if (mIsUserCaptain) {
            view?.showCaptainLoginOptions()
        } else {
            view?.showRunnerLoginOptions()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun onCaptainLoginClick(captainName: String, captainPassword: String) {
        if (captainName.isFieldValid() && captainPassword.isFieldValid()) {
            loginCaptain(captainName, captainPassword)
        } else {
            showError("Invalid fields")
        }

    }

    private fun loginCaptain(captainName: String, captainPassword: String) {

        mApiProvider.loginCaptain(captainName, captainPassword)
    }

    private fun showError(msg: String) {
        view?.showError(msg)
    }

    private fun String?.isFieldValid(): Boolean {
        return (this != null && this.isNotBlank())
    }

    fun onRunnerLoginClick(runnerFirstName: String, runnerSurname: String, runnerTeam: String) {
        Timber.d("onRunnerLoginClick() $runnerFirstName , $runnerSurname, $runnerTeam")
        if (runnerFirstName.isFieldValid() &&
                runnerSurname.isFieldValid() &&
                runnerTeam.isFieldValid()) {
            loginRunner(runnerFirstName, runnerSurname, runnerTeam)
        } else {
            showError("Invalid fields")
        }
    }

    private fun loginRunner(runnerFirstName: String, runnerSurname: String, runnerTeam: String) {
        Timber.d("loginRunner() $runnerFirstName , $runnerSurname, $runnerTeam ")

        val login = "$runnerTeam $runnerFirstName $runnerSurname".toByteArray(Charset.forName("UTF-8"))
        val baseLogin = Base64.encodeToString(login, Base64.NO_WRAP)
        var user: UserRunner? = null



        mSub = mFirebaseHelper
                .mFBDBReference
                .child(RUNNERS_DB)
                .child(baseLogin)
                .data()
                .timeout(10, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    Timber.e("doOnSubscribe")
                    view?.showProgressBar()
                }
                .doFinally {
                    Timber.e("doFinally")
                    view?.hideProgressBar()
                }
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    //onSuccess
                    Timber.d("onSuccess $it")
                    user = it.getValue(UserRunner::class.java)
                }, {
                    //onError
                    Timber.e("onError ${it.message} ")
                })
    }


}