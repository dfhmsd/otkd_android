package eu.nanooq.otkd.viewModels.main.sections

import android.os.Bundle
import com.androidhuman.rxfirebase2.database.dataChanges
import com.google.firebase.database.GenericTypeIndicator
import eu.nanooq.otkd.helpers.FirebaseHelper
import eu.nanooq.otkd.models.API.*
import eu.nanooq.otkd.models.UI.SectionItem
import eu.nanooq.otkd.toBase64
import eu.nanooq.otkd.viewModels.base.BaseViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.toFlowable
import io.reactivex.schedulers.Schedulers
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
        mDisposable?.dispose()
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
                .toFlowable(BackpressureStrategy.BUFFER)
        val teamObservable = mFirebaseHelper.mFBDBReference
                .child(FirebaseHelper.TEAM_MEMBERS)
                .child(user.team_name.toBase64())
                .child(FirebaseHelper.MEMBERS)
                .dataChanges()
                .map {
                    var array = ArrayList<Member>()
                    try {
                        val typeList = object : GenericTypeIndicator<HashMap<String, Member>>() {}
                        val team = it.getValue(typeList)
                        team?.forEach {
                            array.add(it.value)
                        }
                    } catch (exc: Exception) {
                        val typeList = object : GenericTypeIndicator<ArrayList<Member>>() {}
                        array = it.getValue(typeList) ?: ArrayList()
                    }
                    array
                }
                .toFlowable(BackpressureStrategy.BUFFER)

        val obsList = listOf(sectionsObservabel, teamObservable)
        obsList.toFlowable()


        mDisposable = Flowable.combineLatest(sectionsObservabel, teamObservable, BiFunction<ArrayList<Section>, ArrayList<Member>, ArrayList<SectionItem>> { sections, members ->
            val sectionItems = ArrayList<SectionItem>()

            var userFirstName: String = ""
            var userLastName: String = ""
            var userOrder: String = ""
            var userAverageTime: String = ""
            var userImgUrl: String = ""
            val usersSections: ArrayList<Int> = if (user is UserCaptain) {
                val captainSections: ArrayList<Int> = ArrayList()
                val captain: Member? = members.filter { it.is_captain }
                        .firstOrNull()
                Timber.d("userIsCaptain $captain")

                captain.also {
                            userFirstName = it?.first_name ?: ""
                            userLastName = it?.last_name ?: ""
                    userOrder = it?.order.toString()
                    userAverageTime = it?.time_per_10_km.toString()
                            userImgUrl = it?.user_photo ?: ""
                        }
                        ?.sections
                        ?.forEach {
                            captainSections.add(it)
                        }
                captainSections

            } else {
                val runnerSections: ArrayList<Int> = ArrayList()

                if (user is UserRunner) {
                    Timber.d("userIsRunner ${user.first_name}")
                    members.filter {
                        it.first_name == user.first_name &&
                                it.last_name == user.last_name
                    }
                            .firstOrNull()
                            .also {
                                userFirstName = it?.first_name ?: ""
                                userLastName = it?.last_name ?: ""
                                userImgUrl = it?.user_photo ?: ""
                                userOrder = it?.order.toString()
                                userAverageTime = it?.time_per_10_km.toString()
                            }
                            ?.sections
                            ?.forEach {
                                runnerSections.add(it)
                            }
                }
                runnerSections
            }

            sections.filter { usersSections.contains(it.id) }
                    .orEmpty()
                    .forEach {
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
                            runnerName = "$userFirstName $userLastName"
                            runnerImgUrl = userImgUrl
                            runnerOrder = userOrder
                            runnerAverageTime = userAverageTime
                        }
                        sectionItems.add(sectionItem)

                    }
            sectionItems.sortBy { it.Id }
            sectionItems
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
            Timber.d("nice combinelatest ${it}it")
            view?.updateAdapter(it)

        }, {
            Timber.e("onError() ${it.message}")
        }, {
            Timber.e("onComplete()")
        })


    }
}