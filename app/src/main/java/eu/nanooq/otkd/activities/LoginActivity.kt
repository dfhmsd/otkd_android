package eu.nanooq.otkd.activities

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.text.InputType
import android.view.View
import com.jakewharton.rxbinding2.widget.RxTextView
import eu.nanooq.otkd.R
import eu.nanooq.otkd.activities.base.ViewModelActivity
import eu.nanooq.otkd.models.API.User
import eu.nanooq.otkd.s
import eu.nanooq.otkd.viewModels.login.ILoginView
import eu.nanooq.otkd.viewModels.login.LoginViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import io.reactivex.subscribers.DisposableSubscriber
import kotlinx.android.synthetic.main.activity_login.*
import timber.log.Timber


class LoginActivity : ViewModelActivity<ILoginView, LoginViewModel>(), ILoginView {

    var mIsUserCaptain: Boolean? = null
    var mProgressDialog: ProgressDialog? = null

    var mUserName: String = "userName"
    var mPassword: String = "password"
    var mTeamName: String = "teamName"

    private var disposableObserver: DisposableSubscriber<Boolean> = object : DisposableSubscriber<Boolean>() {
        override fun onNext(it: Boolean) {
            if (it) {
                enableSignIn()
            } else {
                disableSignIn()
            }
        }

        override fun onComplete() {}
        override fun onError(e: Throwable) {}
    }


    private lateinit var usernameFlowable: Flowable<CharSequence>
    private lateinit var passwordFlowable: Flowable<CharSequence>
    private lateinit var teamNameFlowable: Flowable<CharSequence>


    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("onCreate()")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupToolbar()

        usernameFlowable =
                RxTextView.textChanges(vUserName).skip(1).toFlowable(BackpressureStrategy.LATEST)
        passwordFlowable =
                RxTextView.textChanges(vPassword).skip(1).toFlowable(BackpressureStrategy.LATEST)
        teamNameFlowable =
                RxTextView.textChanges(vTeamName).skip(1).toFlowable(BackpressureStrategy.LATEST)

        mLoginBtn.setOnClickListener { onLoginBtnClick() }
        vResetPwd.setOnClickListener { forwardToWeb() }

    }

    private fun forwardToWeb() {
        val url = "http://www.odtatierkdunaju.sk/index.php/sk/prihlasit-sa?view=reset"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    override fun onPause() {
        Timber.d("onPause()")

        super.onPause()
    }

    override fun onStop() {
        Timber.d("onStop()")

        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Timber.d("onSaveInstanceState()")
        super.onSaveInstanceState(outState)

        outState.putString(mUserName, vUserName.text.toString())
        outState.putString(mPassword, vPassword.text.toString())
        outState.putString(mTeamName, vTeamName.text.toString())

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        Timber.d("onRestoreInstanceState()")
        super.onRestoreInstanceState(savedInstanceState)

        savedInstanceState?.let {
            vUserName.setText(it.getString(mUserName, ""))
            vPassword.setText(it.getString(mPassword, ""))
            vTeamName.setText(it.getString(mTeamName, ""))
        }

    }

    override fun onDestroy() {
        Timber.d("onDestroy()")
        dismissProgressBar()
        disposableObserver.dispose()

        super.onDestroy()
    }

    private fun onLoginBtnClick() {
        Timber.d("onLoginBtnClick()")

        mIsUserCaptain?.also {
            if (it) {
                val captainName = vUserName.text.toString()
                val captainPassword = vPassword.text.toString()
                viewModel.onCaptainLoginClick(captainName, captainPassword)
            } else {
                val runnerFirstName = vUserName.text.toString()
                val runnerSurname = vPassword.text.toString()
                val runnerTeam = vTeamName.text.toString()
                viewModel.onRunnerLoginClick(runnerFirstName, runnerSurname, runnerTeam)
            }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(mToolbar as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onUserLogedIn(user: User) {
        Timber.d("onUserLogedIn()")
        val mainIntent = Intent(this, MainActivity::class.java)
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(mainIntent)
        finish()
    }

    override fun finishLoginActivity() {
        Timber.d("finishLoginActivity()")

        finish()
    }

    override fun showCaptainLoginOptions() {
        Timber.d("showCaptainLoginOptions()")

        showCaptain()
    }

    override fun showRunnerLoginOptions() {
        Timber.d("showRunnerLoginOptions()")

        showRunner()
    }

    override fun showError(errorMsg: String) {
        Timber.d("showError() $errorMsg")

        Snackbar
                .make(activity_login, errorMsg, Snackbar.LENGTH_LONG)
                .show()
    }

    override fun showProgressBar() {
        Timber.d("showProgressBar() ${Thread.currentThread().name}")
        mProgressDialog = ProgressDialog(this)
        mProgressDialog?.apply {
            isIndeterminate = true
            setCancelable(false)
            setMessage(getString(R.string.signing_in))
            show()
        }

    }

    override fun dismissProgressBar() {
        Timber.d("dismissProgressBar() ${Thread.currentThread().name}")

        mProgressDialog?.dismiss()
//        mProgressDialog = null
    }

    private fun showCaptain() {
        Timber.d("showCaptain()")

        mIsUserCaptain = true
        slogan.text = getString(R.string.label_captain_title).toUpperCase()
        vUserName.hint = getString(R.string.label_captain_username)
        vPassword.hint = s("label_captain_password")

        combineCaptainEvents()
    }

    private fun combineCaptainEvents() {
        Flowable.combineLatest(
                usernameFlowable,
                passwordFlowable,
                BiFunction<CharSequence, CharSequence, Boolean>
                { username, password -> username.isNotBlank() && password.isNotBlank() })
                .subscribe(disposableObserver)
    }

    private fun showRunner() {
        Timber.d("showRunner()")

        combineRunnerEvents()


        mIsUserCaptain = false
        slogan.text = getString(R.string.label_runner_title).toUpperCase()
        vUserName.hint = getString(R.string.label_runner_username)
        vPassword.hint = getString(R.string.label_runner_nickname)
        vPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_user, 0, 0, 0)

        vPassword.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
        vTeamName.visibility = View.VISIBLE
        vTeamName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_group, 0, 0, 0)
        vResetPwd.visibility = View.GONE
        vTeamName.hint = getString(R.string.label_runner_team_name)

    }

    private fun combineRunnerEvents() {
        Flowable.combineLatest(
                usernameFlowable,
                passwordFlowable,
                teamNameFlowable,
                Function3<CharSequence, CharSequence, CharSequence, Boolean>
                { username, password, teamname -> username.isNotBlank() && password.isNotBlank() && teamname.isNotBlank() })
                .subscribe(disposableObserver)
    }

    private fun disableSignIn() {
        with(mLoginBtn) {
            isEnabled = false
            isClickable = false
            background = ContextCompat.getDrawable(this@LoginActivity, R.drawable.bg_btn_disabled)
        }
    }

    private fun enableSignIn() {
        with(mLoginBtn) {
            isEnabled = true
            isClickable = true
            background = ContextCompat.getDrawable(this@LoginActivity, R.drawable.bg_btn_accent)
        }
    }
}
