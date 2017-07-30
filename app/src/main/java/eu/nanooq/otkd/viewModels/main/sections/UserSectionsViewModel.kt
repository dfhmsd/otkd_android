package eu.nanooq.otkd.viewModels.main.sections

import android.os.Bundle
import com.androidhuman.rxfirebase2.database.dataChanges
import com.google.firebase.database.GenericTypeIndicator
import eu.nanooq.otkd.helpers.FirebaseHelper
import eu.nanooq.otkd.models.API.*
import eu.nanooq.otkd.models.UI.SectionItem
import eu.nanooq.otkd.viewModels.base.BaseViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.toFlowable
import timber.log.Timber

/**
 *
 * Created by rd on 29/07/2017.
 */
class UserSectionsViewModel : BaseViewModel<IUserSectionsView>() {
    var mDisposable: Disposable? = null

    override fun onCreate(arguments: Bundle?, savedInstanceState: Bundle?) {
        Timber.d("onCreate()")
        super.onCreate(arguments, savedInstanceState)

        val user = mPreferencesHelper.getUser()
        user?.let { loadUserData(user) }

    }


    private fun loadUserData(user: User) {
        Timber.i("loadUserData() ${user.team_name}")


        val sectionsObservabel = mFirebaseHelper.mFBDBReference
                .child(FirebaseHelper.SECTIONS)
                .dataChanges()
                .map {
                    val typeList = object : GenericTypeIndicator<HashMap<String, Section>>() {}
                    val sections = it.getValue(typeList)
                    val array = ArrayList<Section>()
                    sections?.forEach {
                        array.add(it.value)
                    }
                    array
                }
                .toFlowable(BackpressureStrategy.LATEST)
        val teamObservable = mFirebaseHelper.mFBDBReference
                .child(FirebaseHelper.TEAM_MEMBERS)
//                .child(user.team_name)  TODO odkomentovat az v team_members budou vsechny teamy
                .child("Povieme to behom")
                .child(FirebaseHelper.MEMBERS)
                .dataChanges()
                .map {
                    var team: ArrayList<Member>?
                    try {
                        val typeList = object : GenericTypeIndicator<ArrayList<Member>>() {}
                        team = it.getValue(typeList)
                    } catch (exc: Exception) {
                        Timber.e("catch() ${exc.message}")
                        team = ArrayList()
                    }

//                    val array = ArrayList<Member>()
//                    team?.forEach {
//                        array.add(it.value)
//                    }
                    team ?: ArrayList()
                }
                .toFlowable(BackpressureStrategy.LATEST)

        val obsList = listOf(sectionsObservabel, teamObservable)
        obsList.toFlowable()


        mDisposable = Flowable.combineLatest(sectionsObservabel, teamObservable, BiFunction<ArrayList<Section>, ArrayList<Member>, ArrayList<SectionItem>> { sections, members ->
            val sectionItems = ArrayList<SectionItem>()

            var userFirstName: String = ""
            var userLastName: String = ""
            val usersSections: ArrayList<Int>
            if (user is UserCaptain) {
                usersSections = members.filter {
                    it.is_captain ?: false
                }
                        .firstOrNull()
                        .also {
                            userFirstName = it?.first_name ?: ""
                            userLastName = it?.last_name ?: ""
                        }
                        ?.sections
                        ?: ArrayList()
            } else {
                usersSections = if (user is UserRunner) {
                    members.filter {
                        it.first_name == user.first_name
                                && it.last_name == user.last_name
                    }
                            .firstOrNull()
                            .also {
                                userFirstName = it?.first_name ?: ""
                                userLastName = it?.last_name ?: ""
                            }
                            ?.sections
                            ?: ArrayList()
                } else {
                    ArrayList()
                }
            }
            sections.filter { usersSections.contains(it.id) }.orEmpty().forEach {
                val sectionItem = SectionItem()
                with(sectionItem) {
                    val sectionId = it.id ?: 0
                    Id = sectionId
                    name = it.section_name ?: ""
                    length = it.km ?: 0.0f
                    difficulty = it.hard ?: 0
                    val high = it.high ?: 0
                    val down = it.down ?: 0
                    crossFall = "$high - $down"
                    runnerName = "$userFirstName $userLastName"
                }
                sectionItems.add(sectionItem)

            }

            sectionItems
        }).subscribe({
            Timber.d("nice combinelatest ${it}it")
            view?.updateAdapter(it)

        }, {
            Timber.e("onError() ${it.message}")
        }, {
            Timber.e("onComplete()")
        })


    }
}