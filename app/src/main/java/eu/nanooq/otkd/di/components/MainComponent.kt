package eu.nanooq.otkd.di.components

import android.app.Application
import dagger.Component
import eu.inloop.viewmodel.IView
import eu.nanooq.otkd.activities.base.ViewModelActivity
import eu.nanooq.otkd.apiService.ApiProvider
import eu.nanooq.otkd.di.IDependency
import eu.nanooq.otkd.di.modules.AppModule
import eu.nanooq.otkd.di.modules.FirebaseModule
import eu.nanooq.otkd.di.modules.RetrofitModule
import eu.nanooq.otkd.fragments.base.ViewModelFragment
import eu.nanooq.otkd.viewModels.base.BaseViewModel
import javax.inject.Singleton
import eu.nanooq.otkd.helpers.FirebaseHelper



/**
 *
 * Created by rd on 26/07/2017.
 */

@Singleton
@Component(modules = arrayOf(AppModule::class, FirebaseModule::class, RetrofitModule::class))
interface MainComponent {
    fun inject(app: Application)
    fun inject(baseViewModel: BaseViewModel<IView>)
    fun inject(firebaseHelper: FirebaseHelper)
    fun inject(apiProvider: ApiProvider)
    fun inject(viewModelActivity: ViewModelActivity<IView, BaseViewModel<IView>>)
    fun inject(viewModelFragment: ViewModelFragment<IView, BaseViewModel<IView>>)
    fun inject(dependency: IDependency)


}
