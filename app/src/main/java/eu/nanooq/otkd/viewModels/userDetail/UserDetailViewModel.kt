package eu.nanooq.otkd.viewModels.userDetail

import android.os.Bundle
import com.androidhuman.rxfirebase2.database.dataChanges
import com.google.firebase.database.GenericTypeIndicator
import eu.nanooq.otkd.formatTimeInMinutes
import eu.nanooq.otkd.helpers.FirebaseHelper
import eu.nanooq.otkd.models.API.Member
import eu.nanooq.otkd.models.API.TeamResult
import eu.nanooq.otkd.models.API.User
import eu.nanooq.otkd.models.UI.UserDetailItem
import eu.nanooq.otkd.toBase64
import eu.nanooq.otkd.viewModels.base.BaseViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import timber.log.Timber

/**
 *
 * Created by rd on 07/08/2017.
 */
class UserDetailViewModel : BaseViewModel<IUserDetailView>() {

    var mUser: User? = null
    var mSectionId: Int = 0
    var mDisposable: Disposable? = null


    override fun onCreate(arguments: Bundle?, savedInstanceState: Bundle?) {
        Timber.d("onCreate()")
        super.onCreate(arguments, savedInstanceState)
        mSectionId = arguments?.getInt("sectionId", 0) ?: 0
        mUser = mPreferencesHelper.getUser()


    }

    override fun onStart() {
        Timber.d("onStart()")

        super.onStart()
        mUser?.let {
            loadSectionDetail()
        }
    }

    private fun loadSectionDetail() {
        Timber.d("loadSectionDetail()")

        mDisposable = Flowable
                .combineLatest(teamFlowable,
                        resultsFlowable,
                        BiFunction<ArrayList<Member>,
                                ArrayList<TeamResult>,
                                UserDetailItem> { teams, results ->

                            val teamResult = results.firstOrNull { it.team_name == mUser?.team_name }
                            val sectionTeamResult = teamResult?.sections?.firstOrNull { it.section_id == mSectionId }
                            view?.setToolbarTitle("${sectionTeamResult?.section_name}")
                            val detailItem = UserDetailItem().apply {
                                mBestTime = sectionTeamResult?.best_time.formatTimeInMinutes()
                                mTeamStanding = "${teamResult?.final_result}."
                                mStandingInCategory = "${teamResult?.final_result_in_category}."
                                mStandingInSection = "${sectionTeamResult?.result_on_section}."
                                mExpectedTime = sectionTeamResult?.estimated_time.formatTimeInMinutes()
                                mRealTime = sectionTeamResult?.real_time.formatTimeInMinutes()
                                mWorstTime = sectionTeamResult?.worst_time.formatTimeInMinutes()
                                mTimeAtFinish = "${sectionTeamResult?.time_when_run}"
                                mTeamTime = "${teamResult?.final_team_time}"
                            }

                            detailItem
                        }).subscribe {
            view?.updateData(it)
        }

    }

    private val teamFlowable by lazy {
        mFirebaseHelper.mFBDBReference
                .child(FirebaseHelper.TEAM_MEMBERS)
                .child(mUser?.team_name.toBase64())
                .child(FirebaseHelper.MEMBERS)
                .dataChanges()
                .map {
                    Timber.d("teamObservable map() $it")
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
    }


    private val resultsFlowable by lazy {
        mFirebaseHelper.mFBDBReference
                .child(FirebaseHelper.RESULTS)
                .dataChanges()
                .map {
                    val typeList = object : GenericTypeIndicator<java.util.HashMap<String, TeamResult>>() {}
                    val results = it.getValue(typeList)
                    val array = java.util.ArrayList<TeamResult>()
                    results?.forEach {
                        array.add(it.value)
                    }
                    array
                }
                .toFlowable(BackpressureStrategy.BUFFER)
    }
}
