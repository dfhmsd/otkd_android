package eu.nanooq.otkd.viewModels.sectionDetail

import android.os.Bundle
import com.androidhuman.rxfirebase2.database.dataChanges
import com.google.firebase.database.GenericTypeIndicator
import eu.nanooq.otkd.helpers.FirebaseHelper
import eu.nanooq.otkd.models.API.TeamResult
import eu.nanooq.otkd.models.API.User
import eu.nanooq.otkd.models.UI.CompleteStandingHeader
import eu.nanooq.otkd.models.UI.ItemType
import eu.nanooq.otkd.models.UI.SectionStandingItem
import eu.nanooq.otkd.viewModels.base.BaseViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.disposables.Disposable
import timber.log.Timber
import java.util.HashMap
import kotlin.collections.ArrayList

/**
 *
 * Created by rd on 04/08/2017.
 */
class StandingDetailViewModel : BaseViewModel<IStandingsDetailView>() {

    var mDisposable: Disposable? = null
    var sectionId: Int? = null


    override fun onCreate(arguments: Bundle?, savedInstanceState: Bundle?) {
        Timber.d("onCreate()")
        super.onCreate(arguments, savedInstanceState)

        sectionId = arguments?.let {
            it.getInt("sectionId", 0)
        }
    }

    override fun onStart() {
        Timber.d("onStart()")
        super.onStart()
        val user = mPreferencesHelper.getUser()



        user?.run { loadSectionStandingsResults(user, sectionId ?: 0) }
    }


    override fun onDestroy() {
        Timber.d("onDestroy()")

        super.onDestroy()
        mDisposable?.dispose()
    }

    private fun loadSectionStandingsResults(user: User, sectionId: Int) {
        Timber.d("loadSectionStandingsResults()")

        resultsFlowable.map {
            // this filter team which have completed section specified with sectionId param
            var sectionName: String = ""
            val teams = it.filter { it.sections?.firstOrNull { it.section_id == sectionId && it.real_time != 0.0f }.also { sectionName = it?.section_name ?: "" } != null }
            val resultItems = ArrayList<SectionStandingItem>()
            Timber.i("poradie $sectionId $sectionName $teams")

            teams.orEmpty().forEach {
                val sectionStandingItem = SectionStandingItem().apply {
                    mStanding = it.sections?.firstOrNull { it.section_id == sectionId }?.result_on_section ?: 0
                    mTeamName = it.team_name ?: ""
                    mTeamTime = it.sections?.firstOrNull { it.section_id == sectionId }?.real_time ?: 0.0f
                    val completedSections = it.sections?.filter { it.section_id ?: 0 <= sectionId }
                    var completeTime: Float = 0.0f
                    completedSections.orEmpty().forEach {
                        completeTime += (it.real_time ?: 0.0f)
                    }
                    mCompleteTime = completeTime
                    mIsYourTeam = it.team_name == user.team_name
                }
                resultItems.add(sectionStandingItem)
            }


            //TODO
            val teamsWhichAreStillRunningThisSection = it.filter { it.sections?.firstOrNull { it.section_id == sectionId && !it.time_when_run.isNullOrBlank() } != null }.size
            view?.updateTeamsStats(teams.size, teamsWhichAreStillRunningThisSection - teams.size)
            view?.setToolbarTitle(sectionName)
            resultItems.sortBy { it.mStanding }
            resultItems
        }
                .subscribe({
                    Timber.d("onNext()")
                    val header = CompleteStandingHeader()
                    val iteams = ArrayList<ItemType>()
                    iteams.add(header)
                    iteams.addAll(it)
                    view?.updateAdapter(iteams)
                }, {
                    Timber.d("onError() ${it.message}")
                })

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
                .toFlowable(BackpressureStrategy.LATEST)
    }
}
