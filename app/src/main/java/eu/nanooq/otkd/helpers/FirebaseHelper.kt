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
class FirebaseHelper : IDependency {

    lateinit var mFBDBReference: DatabaseReference

    @Inject
    lateinit var mContext: Context

    override fun init() {
        super.init()
        val dep = object : IDependency {
            override fun init() {
                super.init()
                Timber.d("initFire")
            }

            override fun destroy() {


            }
        }
        dep.init()
        App.component.inject(this)

        val fireApp = FirebaseApp.initializeApp(mContext)

        mFBDBReference = FirebaseDatabase.getInstance()
                .reference

    }

    override fun destroy() {

    }
}
