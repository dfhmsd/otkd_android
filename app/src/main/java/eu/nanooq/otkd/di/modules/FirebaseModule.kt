package eu.nanooq.otkd.di.modules

import dagger.Module
import dagger.Provides
import eu.nanooq.otkd.helpers.FirebaseHelper
import javax.inject.Singleton

/**
 *
 * Created by rd on 26/07/2017.
 */
@Module
class FirebaseModule {
    @Provides
    @Singleton
    fun provideFirebaseHelper() : FirebaseHelper {
        val helper = FirebaseHelper()
        helper.init()
        return helper
    }
}
