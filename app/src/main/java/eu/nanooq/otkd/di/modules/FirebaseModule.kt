package eu.nanooq.otkd.di.modules

import android.app.Application
import dagger.Module
import dagger.Provides
import eu.nanooq.otkd.helpers.FirebaseHelper
import javax.inject.Singleton

/**
 *
 * Created by rd on 26/07/2017.
 */
@Module
class FirebaseModule(private val mApp: Application) {
    @Provides
    @Singleton
    fun provideFirebaseHelper() : FirebaseHelper {
        val helper = FirebaseHelper(mApp.applicationContext)
        helper.init()
        return helper
    }
}
