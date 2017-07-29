package eu.nanooq.otkd.helpers

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import eu.nanooq.otkd.App
import eu.nanooq.otkd.di.IDependency
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 *
 * Created by rd on 26/07/2017.
 */
@Singleton
class FirebaseHelper @Inject constructor(private var mContext: Context) : IDependency {

    lateinit var mFBDBReference: DatabaseReference


    companion object {
        const val SECTIONS = "sections"
        const val TEAM_MEMBERS = "team_members"
        const val MEMBERS = "members"
        const val USER_DATA = "user_data"
        const val RESULTS = "results"
        const val LOGIN_RUNNER = "login_runner_"
    }

    override fun init() {
        App.component.inject(this)

        FirebaseApp.initializeApp(mContext)

        mFBDBReference = FirebaseDatabase.getInstance()
                .reference

    }

    override fun destroy() {

    }
}
