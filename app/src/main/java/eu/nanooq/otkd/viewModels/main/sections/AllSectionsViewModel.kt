package eu.nanooq.otkd.viewModels.main.sections

import android.os.Bundle
import com.androidhuman.rxfirebase2.database.dataChanges
import com.google.firebase.database.GenericTypeIndicator
import eu.nanooq.otkd.helpers.FirebaseHelper
import eu.nanooq.otkd.models.API.Member
import eu.nanooq.otkd.models.API.Section
import eu.nanooq.otkd.models.API.User
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
 * Created by rd on 28/07/2017.
 */
class AllSectionsViewModel : BaseViewModel<IAllSectionsView>() {

    var mDisposable: Disposable? = null

    private lateinit var sectionsFlowable: Flowable<ArrayList<Section>>

    override fun onCreate(arguments: Bundle?, savedInstanceState: Bundle?) {
        Timber.d("onCreate()")
        super.onCreate(arguments, savedInstanceState)


    }

    override fun onStart() {
        Timber.d("onStart()")
        super.onStart()
        val user = mPreferencesHelper.getUser()

        user?.let { loadUserData(user) }


    }

    override fun onStop() {
        Timber.d("onStop()")
        super.onStop()
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        Timber.d("onSaveInstanceState()")
        super.onSaveInstanceState(bundle)
    }

    override fun onDestroy() {
        Timber.d("onDestroy()")
        super.onDestroy()
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

            sections.forEach {
                val sectionItem = SectionItem()
                with(sectionItem) {
                    val sectionId = it.id ?: 0
                    Id = sectionId
                    name = it.section_name ?: ""
                    length = it.km ?: 0.0f
                    difficulty = it.hard ?: 0
                    high = it.high.toString()
                    down = it.down.toString()
                    description = it.description ?: ""
                    val member = members.filter { it.sections?.contains(sectionId) ?: false }.firstOrNull()
                    runnerName = "${member?.first_name ?: ""} ${member?.last_name ?: ""}"
                    runnerImgUrl = member?.user_photo ?: ""
                    runnerOrder = member?.order ?: 0
                    runnerAverageTime = member?.time_per_10_km ?: ""
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
