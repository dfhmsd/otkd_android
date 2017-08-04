package eu.nanooq.otkd.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import eu.nanooq.otkd.R
import eu.nanooq.otkd.activities.base.ViewModelActivity
import eu.nanooq.otkd.adapters.MainPagerAdapter
import eu.nanooq.otkd.fragments.*
import eu.nanooq.otkd.viewModels.IActivityToolbar
import eu.nanooq.otkd.viewModels.main.IMainView
import eu.nanooq.otkd.viewModels.main.MainViewModel
import kotlinx.android.synthetic.main.main_activity.*
import timber.log.Timber

/**
 *
 * Created by rd on 28/07/2017.
 */
class MainActivity : ViewModelActivity<IMainView, MainViewModel>(), IMainView, IActivityToolbar {

    companion object {
        val FRAGMENT_SECTIONS_TAG = "fragment_sections"
        val FRAGMENT_TEAM_TAG = "fragment_team"
        val FRAGMENT_CHAT_TAG = "fragment_chat"
        val FRAGMENT_RESULTS_TAG = "fragment_results"
        val FRAGMENT_PROFILE_TAG = "fragment_user_profile"
        val FRAGMENT_TAGS: MutableList<String> = mutableListOf(
                FRAGMENT_SECTIONS_TAG,
                FRAGMENT_TEAM_TAG,
                FRAGMENT_CHAT_TAG,
                FRAGMENT_RESULTS_TAG,
                FRAGMENT_PROFILE_TAG)
    }


    lateinit var mAdapter: MainPagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("onCreate()")
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)

        val toolbar: Toolbar = mToolbar as Toolbar
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
//        supportActionBar?.title = getString(R.string.sections_toolbar_title)

        mAdapter = MainPagerAdapter(supportFragmentManager)
        vMainContentPager.adapter = mAdapter
//        vMainContentPager.setOnTouchListener { _, _ ->
//            true
//        }



        val sectionsFrag = SectionsFragment.newInstance()
        val teamFrag = TeamFragment.newInstance()
        val chatFrag = ChatFragment.newInstance()
        val resultsFrag = ResultsFragment.newInstance()
        val userProfile = UserProfileFragment.newInstance()
        replaceContent(sectionsFrag, FRAGMENT_SECTIONS_TAG)

        vMainNavigation?.setOnTabSelectListener {
            Timber.d("onMainNavigationClick() $it")

            when (it) {
                R.id.action_sections -> {
                    replaceContent(sectionsFrag, FRAGMENT_SECTIONS_TAG)
                }
                R.id.action_team -> {
                    replaceContent(teamFrag, FRAGMENT_TEAM_TAG)
                }
                R.id.action_chat -> {
                    replaceContent(chatFrag, FRAGMENT_CHAT_TAG)
                }
                R.id.action_results -> {
                    replaceContent(resultsFrag, FRAGMENT_RESULTS_TAG)
                }
                R.id.action_profile -> {
                    replaceContent(userProfile, FRAGMENT_PROFILE_TAG)
                }
            }
        }
    }

    private fun replaceContent(fragment: Fragment, tag: String) {
        Timber.d("replaceContent() $tag")
//        supportFragmentManager.beginTransaction()
//                .replace(R.id.vMainContent, fragment)
//                .commit()
        onChangeMainPage(FRAGMENT_TAGS.indexOf(tag))
    }

    private fun onChangeMainPage(pos: Int) {
        vMainContentPager.setCurrentItem(pos, false)
    }

    override fun onNewResult() {
    }

    override fun onToolbarTitleChange(title: String) {
        supportActionBar?.title = title
    }
}
