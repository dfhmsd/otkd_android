package eu.nanooq.otkd

import android.app.Application
import android.util.Log
import eu.nanooq.otkd.di.components.DaggerMainComponent
import eu.nanooq.otkd.di.components.MainComponent
import eu.nanooq.otkd.di.modules.AppModule
import eu.nanooq.otkd.di.modules.FirebaseModule
import eu.nanooq.otkd.di.modules.RetrofitModule
import timber.log.Timber
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

/**
 *
 * Created by rd on 26/07/2017.
 */
class App : Application() {
    companion object {
        lateinit var component: MainComponent
    }

    override fun onCreate() {
        super.onCreate()

        setupFont()
        initTimber()
        initDagger()
    }

    private fun setupFont() {
        CalligraphyConfig.initDefault( CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.opensans_font_path))
                .setFontAttrId(R.attr.fontPath)
                .build())
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initDagger() {
        component = DaggerMainComponent
                .builder()
                .appModule(AppModule(this))
                .firebaseModule(FirebaseModule(this))
                .retrofitModule(RetrofitModule())
                .build()
    }
}
