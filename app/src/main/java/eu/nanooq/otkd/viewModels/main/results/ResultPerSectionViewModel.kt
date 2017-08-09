package eu.nanooq.otkd.viewModels.main.results

import android.os.Bundle
import com.androidhuman.rxfirebase2.database.dataChangesOf
import com.google.firebase.database.GenericTypeIndicator
import eu.nanooq.otkd.helpers.FirebaseHelper
import eu.nanooq.otkd.models.API.Section
import eu.nanooq.otkd.models.API.TeamResult
import eu.nanooq.otkd.models.API.User
import eu.nanooq.otkd.models.UI.SectionResult
import eu.nanooq.otkd.viewModels.base.BaseViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
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

    override fun onStop() {
        Timber.d("onStop()")
        super.onStop()
        mDisposable?.dispose()
    }

    override fun onDestroy() {
        Timber.d("onDestroy()")
        super.onDestroy()
    }

    private fun loadResults(user: User) {
        Timber.d("loadResults()")

        mDisposable = Flowable.combineLatest(sectionsFlowable,
                resultsFlowable,
                BiFunction<ArrayList<Section>, ArrayList<TeamResult>, ArrayList<SectionResult>> {
                    sections, results ->
                    Timber.d("startParsing()")

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
                    Timber.d("parsing finished")

                    sectionsResults
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Timber.d("onNext() $it")
                    view?.updateAdapter(it)
                }, {
                    Timber.e("onError() ${it.message}")
                })

    }

    val typeListSections = object : GenericTypeIndicator<HashMap<String, Section>>() {}


    private val sectionsFlowable by lazy {
        mFirebaseHelper.mFBDBReference
                .child(FirebaseHelper.SECTIONS)
                .dataChangesOf(typeListSections)
                .map {
                    Timber.d("resultsFlowable map()")

                    val sections = it.get()
                    val array = ArrayList<Section>()
                    sections?.forEach {
                        array.add(it.value)
                    }
                    array
                }
                .toFlowable(BackpressureStrategy.BUFFER)
    }

    val typeListTeamResult = object : GenericTypeIndicator<HashMap<String, TeamResult>>() {}

    private val resultsFlowable by lazy {
        mFirebaseHelper.mFBDBReference
                .child(FirebaseHelper.RESULTS)
                .dataChangesOf(typeListTeamResult)
                .map {
                    Timber.d("resultsFlowable map()")
                    val results = it.get()
                    val array = ArrayList<TeamResult>()
                    results?.forEach {
                        array.add(it.value)
                    }
                    array
                }
                .toFlowable(BackpressureStrategy.BUFFER)
    }
}