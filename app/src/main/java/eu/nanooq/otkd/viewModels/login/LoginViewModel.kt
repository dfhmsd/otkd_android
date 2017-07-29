package eu.nanooq.otkd.viewModels.login

import android.os.Bundle
import android.util.Base64
import com.androidhuman.rxfirebase2.database.data
import eu.nanooq.otkd.models.API.UserCaptain
import eu.nanooq.otkd.viewModels.base.BaseViewModel
import timber.log.Timber
import java.nio.charset.Charset
import eu.nanooq.otkd.models.API.UserRunner
import eu.nanooq.otkd.viewModels.splash.SplashViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


/**
 *
 * Created by rd on 23/07/2017.
 */
class LoginViewModel : BaseViewModel<ILoginView>() {


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

    private fun loginCaptain(captainName: String, captainPassword: String) {

        mApiProvider
                .loginCaptain(captainName, captainPassword)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    Timber.d("doOnSubscribe() ${Thread.currentThread().name}")
                    view?.showProgressBar()
                }
                .doFinally {
                    Timber.d("doFinaly() ${Thread.currentThread().name}")
                    view?.dismissProgressBar()
                }
                .subscribe ({
                    Timber.d("onNext() $it  ${Thread.currentThread().name}")
                    saveUser(it)
                    view?.onUserLogedIn(it)
                },{
                    Timber.e("onError $it ${Thread.currentThread().name}")
                        view?.showError(it.message?: "Could not sign in")
                })
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
                    view?.dismissProgressBar()
                }
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    //onSuccess
                    Timber.d("onSuccess $it")
                    user = it.getValue(UserRunner::class.java)

                    user?.let {
                        saveUser(it)
                        view?.onUserLogedIn(it)
                    }
                }, {
                    //onError
                    Timber.e("onError ${it.message} ")
                })
    }

    private fun saveUser(runner: UserRunner) { mPreferencesHelper.saveUser(runner) }
    private fun saveUser(captain: UserCaptain) { mPreferencesHelper.saveUser(captain) }

    private fun showError(msg: String) { view?.showError(msg) }

    private fun String?.isFieldValid(): Boolean {
        return (this != null && this.isNotBlank())
    }

}