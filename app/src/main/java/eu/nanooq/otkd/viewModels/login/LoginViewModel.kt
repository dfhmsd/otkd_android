package eu.nanooq.otkd.viewModels.login

import android.os.Bundle
import com.androidhuman.rxfirebase2.database.data
import eu.nanooq.otkd.helpers.FirebaseHelper
import eu.nanooq.otkd.models.API.UserCaptain
import eu.nanooq.otkd.models.API.UserData
import eu.nanooq.otkd.models.API.UserRunner
import eu.nanooq.otkd.toBase64
import eu.nanooq.otkd.viewModels.base.BaseViewModel
import eu.nanooq.otkd.viewModels.splash.SplashViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit


/**
 *
 * Created by rd on 23/07/2017.
 */
class LoginViewModel : BaseViewModel<ILoginView>() {


    lateinit var mSub: Disposable

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
            showError("Nesprávne zadané údaje. Údaje vyplňte zhodne s údajmi v administrácii vrátane interpunkcie a veľkých/malých písmen")
        }

    }



    fun onRunnerLoginClick(runnerFirstName: String, runnerSurname: String, runnerTeam: String) {
        Timber.d("onRunnerLoginClick() $runnerFirstName , $runnerSurname, $runnerTeam")
        if (runnerFirstName.isFieldValid() &&
                runnerSurname.isFieldValid() &&
                runnerTeam.isFieldValid()) {
            loginRunner(runnerFirstName, runnerSurname, runnerTeam)
        } else {
            showError("Nesprávne zadané údaje. Údaje vyplňte zhodne s údajmi v administrácii vrátane interpunkcie a veľkých/malých písmen")
        }
    }

    private fun loginCaptain(captainName: String, captainPassword: String) {

        mApiProvider
                .loginCaptain(captainName, captainPassword)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    Timber.d("doOnSubscribe() ")
                    view?.showProgressBar()
                }
                .doFinally {
                    Timber.d("doFinaly() ")
                    view?.dismissProgressBar()
                }
                .subscribe ({
                    Timber.d("onNext() $it ")
                    getCaptainTeam(it)
                },{
                    Timber.e("onError $it ")
                    view?.showError("Nesprávne zadané údaje. Údaje vyplňte zhodne s údajmi v administrácii.")
                })
    }

    private fun loginRunner(runnerFirstName: String, runnerSurname: String, runnerTeam: String) {
        Timber.d("loginRunner() $runnerFirstName , $runnerSurname, $runnerTeam ")

        val login = "$runnerTeam $runnerFirstName $runnerSurname".toBase64()
        var user: UserRunner?

        mSub = mFirebaseHelper
                .mFBDBReference
                .child(FirebaseHelper.LOGIN_RUNNER)
                .child(login)
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

    private fun getCaptainTeam(captain: UserCaptain) {
        mFirebaseHelper
                .mFBDBReference
                .child(FirebaseHelper.USER_DATA)
                .child(captain.email.toBase64())
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
                    val userData: UserData? = it.getValue(UserData::class.java)

                    userData?.let {
                        captain.team_name = it.team_name ?: ""
                        saveUser(captain)
                        view?.onUserLogedIn(captain)
                    }
                }, {
                    //onError
                    Timber.e("onError ${it.message} ")
                })
    }

    private fun saveUser(captain: UserCaptain) {
        mPreferencesHelper.saveUser(captain)
    }

    private fun showError(msg: String) { view?.showError(msg) }

    private fun String?.isFieldValid(): Boolean {
        return (this != null && this.isNotBlank())
    }

}