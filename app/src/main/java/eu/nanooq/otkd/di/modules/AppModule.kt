package eu.nanooq.otkd.di.modules

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton



/**
 *
 * Created by rd on 26/07/2017.
 */
@Module
class AppModule(private val mApp: Application) {
    @Provides
    @Singleton
    fun provideApplication(): Application {
        return mApp
    }

    @Provides
    @Singleton
    fun provideContext(): Context {
        return mApp.applicationContext
    }
}
