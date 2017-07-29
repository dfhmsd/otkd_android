package eu.nanooq.otkd.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.Toolbar
import android.text.InputType
import android.view.View
import android.widget.TextView
import eu.nanooq.otkd.R
import eu.nanooq.otkd.activities.base.ViewModelActivity
import eu.nanooq.otkd.models.API.UserCaptain
import eu.nanooq.otkd.models.API.UserRunner
import eu.nanooq.otkd.s
import eu.nanooq.otkd.viewModels.login.ILoginView
import eu.nanooq.otkd.viewModels.login.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import timber.log.Timber


class LoginActivity : ViewModelActivity<ILoginView, LoginViewModel>(), ILoginView {

    var mIsUserCaptain: Boolean? = null
    var mProgressDialog: ProgressDialog? = null

    var mUserName: String = "userName"
    var mPassword: String = "password"
    var mTeamName: String = "teamName"


    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("onCreate()")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupToolbar()
        mLoginBtn.setOnClickListener { onLoginBtnClick() }

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

        outState.putString(mUserName, vUserName.text.toString() )
        outState.putString(mPassword, vPassword.text.toString() )
        outState.putString(mTeamName, vTeamName.text.toString() )

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

    override fun onUserLogedIn(captain: UserCaptain) {
        Timber.d("onUserLogedIn()")

        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onUserLogedIn(runner: UserRunner) {
        Timber.d("onUserLogedIn()")

        startActivity(Intent(this, MainActivity::class.java))
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
        slogan.text = getString(R.string.label_captain_title)
        vUserName.hint = getString(R.string.label_captain_username)
        vPassword.hint = s("label_captain_password")
    }

    private fun showRunner() {
        Timber.d("showRunner()")

        mIsUserCaptain = false
        slogan.text = getString(R.string.label_runner_title)
        vUserName.hint = getString(R.string.label_runner_username)
        vPassword.hint = getString(R.string.label_runner_nickname)
        vPassword.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
        vTeamName.visibility = View.VISIBLE
        vTeamName.hint = getString(R.string.label_runner_team_name)

        //set test runner
        vUserName.setText("Barbora", TextView.BufferType.EDITABLE)
        vPassword.setText("Rakacká", TextView.BufferType.EDITABLE)
        vTeamName.setText("IT Girls", TextView.BufferType.EDITABLE)
    }
}
