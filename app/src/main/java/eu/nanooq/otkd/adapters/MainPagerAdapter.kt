package eu.nanooq.otkd.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import eu.nanooq.otkd.activities.MainActivity
import eu.nanooq.otkd.fragments.*

/**
 *
 * Created by rd on 31/07/2017.
 */
class MainPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {


    val tabs: MutableList<String> = MainActivity.FRAGMENT_TAGS

    override fun getItem(position: Int): Fragment {
        return when (tabs[position]) {
            MainActivity.FRAGMENT_SECTIONS_TAG -> SectionsFragment.newInstance()
            MainActivity.FRAGMENT_TEAM_TAG -> TeamFragment.newInstance()
            MainActivity.FRAGMENT_CHAT_TAG -> ChatFragment.newInstance()
            MainActivity.FRAGMENT_RESULTS_TAG -> ResultsFragment.newInstance()
            else -> UserProfileFragment.newInstance()

        }
    }

    override fun getCount(): Int = tabs.size

    override fun getPageTitle(position: Int): CharSequence = tabs[position]
}
