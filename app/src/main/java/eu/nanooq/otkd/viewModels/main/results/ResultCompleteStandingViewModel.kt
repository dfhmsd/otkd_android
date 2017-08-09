package eu.nanooq.otkd.viewModels.main.results

import android.os.Bundle
import com.androidhuman.rxfirebase2.database.dataChanges
import com.google.firebase.database.GenericTypeIndicator
import eu.nanooq.otkd.helpers.FirebaseHelper
import eu.nanooq.otkd.models.API.TeamResult
import eu.nanooq.otkd.models.API.User
import eu.nanooq.otkd.models.UI.CompleteStandingHeader
import eu.nanooq.otkd.models.UI.CompleteStandingItem
import eu.nanooq.otkd.models.UI.ItemType
import eu.nanooq.otkd.viewModels.base.BaseViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.*

/**
 *
 * Created by rd on 03/08/2017.
 */
class ResultCompleteStandingViewModel : BaseViewModel<IResultCompleteStandingView>() {

    var mDisposable: Disposable? = null


    override fun onCreate(arguments: Bundle?, savedInstanceState: Bundle?) {
        Timber.d("onCreate()")
        super.onCreate(arguments, savedInstanceState)

    }

    override fun onStart() {
        Timber.d("onStart()")
        super.onStart()
        val user = mPreferencesHelper.getUser()



        user?.run { loadCompleteResults(user) }
    }

    override fun onStop() {
        Timber.d("onDestroy()")
        super.onStop()
        mDisposable?.dispose()
    }

    override fun onDestroy() {
        Timber.d("onDestroy()")
        super.onDestroy()
    }

    private fun loadCompleteResults(user: User) {
        Timber.d("loadCompleteResults()")

        mDisposable = resultsFlowable
                .map {
                    val finishedTeams = it.filter {
                        it.sections?.lastOrNull()?.real_time != 0.0f
                    }
                    val completeStandingsItems = ArrayList<CompleteStandingItem>()

                    val runningTeamsSize = it.filter { it.sections?.firstOrNull()?.time_when_run?.isNotBlank() ?: false }.size - finishedTeams.size

                    view?.updateTeamsStats(finishedTeams.size, runningTeamsSize)

                    finishedTeams.forEach {
                        val item = CompleteStandingItem().apply {
                            mStanding = it.final_result ?: 0
                            mTeamName = it.team_name ?: ""
                            mTeamTime = it.final_team_time ?: ""
                            mCategoryStanding = it.final_result_in_category ?: 0
                            mIsYourTeam = user.team_name == it.team_name
                        }
                        completeStandingsItems.add(item)
                    }

                    completeStandingsItems.sortBy { it.mStanding }
                    completeStandingsItems
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    val header = CompleteStandingHeader()
                    val itemsWithHeader = ArrayList<ItemType>()
                    itemsWithHeader.add(header)
                    itemsWithHeader.addAll(it)
                    view?.updateAdapter(itemsWithHeader)
                }
    }

    private val resultsFlowable by lazy {
        mFirebaseHelper.mFBDBReference
                .child(FirebaseHelper.RESULTS)
                .dataChanges()
                .map {
                    val typeList = object : GenericTypeIndicator<HashMap<String, TeamResult>>() {}
                    val results = it.getValue(typeList)
                    val array = ArrayList<TeamResult>()
                    results?.forEach {
                        array.add(it.value)
                    }
                    array
                }
                .toFlowable(BackpressureStrategy.BUFFER)
    }
}
