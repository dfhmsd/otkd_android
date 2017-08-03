package eu.nanooq.otkd.viewModels.main.results

import android.os.Bundle
import com.androidhuman.rxfirebase2.database.dataChanges
import com.google.firebase.database.GenericTypeIndicator
import eu.nanooq.otkd.helpers.FirebaseHelper
import eu.nanooq.otkd.models.API.Section
import eu.nanooq.otkd.models.API.TeamResult
import eu.nanooq.otkd.models.API.User
import eu.nanooq.otkd.models.UI.SectionResult
import eu.nanooq.otkd.viewModels.base.BaseViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import timber.log.Timber

/**
 *
 * Created by rd on 03/08/2017.
 */
class ResultPerSectionViewModel : BaseViewModel<IResultPerSectionView>() {

    var mDisposable: Disposable? = null


    override fun onCreate(arguments: Bundle?, savedInstanceState: Bundle?) {
        Timber.d("onCreate()")
        super.onCreate(arguments, savedInstanceState)

    }

    override fun onStart() {
        Timber.d("onStart()")
        super.onStart()
        val user = mPreferencesHelper.getUser()

        user?.let {
            loadResults(user)

        }


    }

    private fun loadResults(user: User) {
        mDisposable = Flowable.combineLatest(sectionsFlowable,
                resultsFlowable,
                BiFunction<ArrayList<Section>, ArrayList<TeamResult>, ArrayList<SectionResult>> {
                    sections, results ->
                    val sectionsResults = ArrayList<SectionResult>()

                    sections.sortBy { it.id }
                    val firstSectionId: Int = sections.first().id ?: 1
                    val lastSectionId: Int = sections.last().id ?: 36
                    val teamsStartedCount = results.filter { it.sections?.firstOrNull { it.section_id == firstSectionId }?.time_when_run != "" }.size
                    val teamsFinishedCount = results.filter { it.sections?.firstOrNull { it.section_id == lastSectionId }?.real_time != 0.0f }.size
                    var lenghtOfAllSections: Float = 0.0f
                    sections.forEach {
                        lenghtOfAllSections += (it.km ?: 0.0f)
                        val sectionResult = SectionResult()
                        sectionResult.mSectionId = it.id ?: 0
                        sectionResult.mSectionName = it.section_name ?: ""

                        val mIsSectionCompletedByUserTeam = results.filter {
                            it.team_name == user.team_name &&
                                    it.sections
                                            ?.firstOrNull {
                                                it.section_id == sectionResult.mSectionId
                                            }
                                            ?.real_time
                                            ?: 0.0f != 0.0f
                        }.isNotEmpty()

                        sectionResult.mSectionCompletedByYourTeam = mIsSectionCompletedByUserTeam
                        val teamCount = results.filter { it.sections?.firstOrNull { it.section_id == sectionResult.mSectionId }?.real_time ?: 0.0f != 0.0f }.size
                        sectionResult.mSectionTeamCount = teamCount
                        sectionsResults.add(sectionResult)
                    }
                    view?.updateLenghtOfAllSections(lenghtOfAllSections)
                    view?.updateSectionsCount(sections.size)
                    view?.updateTeamsStartedCount(teamsStartedCount)
                    view?.updateTeamsFinishedCount(teamsFinishedCount)
                    sectionsResults.sortBy { it.mSectionId }
                    sectionsResults
                })
                .subscribe({
                    Timber.d("onNext() $it")
                    view?.updateAdapter(it)
                }, {
                    Timber.e("onError() ${it.message}")
                })
    }

    private val sectionsFlowable by lazy {
        mFirebaseHelper.mFBDBReference
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