package eu.nanooq.otkd.helpers

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import eu.nanooq.otkd.App
import eu.nanooq.otkd.di.IDependency
import eu.nanooq.otkd.edit
import eu.nanooq.otkd.models.API.User
import eu.nanooq.otkd.models.API.UserCaptain
import eu.nanooq.otkd.models.API.UserRunner
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


/**
 *
 * Created by rd on 29/07/2017.
 */
@Singleton
class PreferencesHelper
@Inject
constructor(private var mContext: Context) : IDependency {


    private val RUNNER = "runner"

    private val CAPTAIN = "captain"

    private lateinit var mPreferences: SharedPreferences

    private lateinit var runnerMoshiAdapter: JsonAdapter<UserRunner>
    private lateinit var captainMoshiAdapter: JsonAdapter<UserCaptain>


    override fun init() {
        App.component.inject(this)

        mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)


        val moshi = Moshi.Builder().build()
        runnerMoshiAdapter = moshi.adapter<UserRunner>(UserRunner::class.java)
        captainMoshiAdapter = moshi.adapter<UserCaptain>(UserCaptain::class.java)
    }

    override fun destroy() {
    }

    fun saveUser(runner: UserRunner) {
        Timber.d("saveUser()")
        mPreferences.edit {
            putString(RUNNER, runnerMoshiAdapter.toJson(runner))
            putString(CAPTAIN, null)
        }
    }

    fun saveUser(captain: UserCaptain) {
        Timber.d("saveUser()")
        mPreferences.edit {
            putString(CAPTAIN, captainMoshiAdapter.toJson(captain))
            putString(RUNNER, null)
        }
    }

    fun getUser(): User? {
        Timber.d("getUser()")

        val captain = mPreferences.getString(CAPTAIN, null)
        val runner = mPreferences.getString(RUNNER, null)

        return if (captain != null) {
            captainMoshiAdapter.fromJson(captain)
        } else {
            if (runner != null) {
                runnerMoshiAdapter.fromJson(runner)
            } else return null
        }
    }

    fun deleteUserData() {
        mPreferences.edit {
            putString(RUNNER, null)
            putString(CAPTAIN, null)
        }
    }


}
