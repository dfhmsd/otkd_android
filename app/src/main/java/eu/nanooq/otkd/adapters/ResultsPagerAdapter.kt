package eu.nanooq.otkd.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import eu.nanooq.otkd.fragments.ResultCompleteStandingFragment
import eu.nanooq.otkd.fragments.ResultsFragment
import eu.nanooq.otkd.fragments.ResultsPerSectionFragment

/**
 *
 * Created by rd on 03/08/2017.
 */
class ResultsPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {


    val tabs: MutableList<String> = mutableListOf(ResultsFragment.STANDINGS, ResultsFragment.PER_SECTIONS)

    override fun getItem(position: Int): Fragment {
        return if (tabs[position] == ResultsFragment.PER_SECTIONS) {
            ResultsPerSectionFragment.newInstance()
        } else {
            ResultCompleteStandingFragment.newInstance()
        }
    }

    override fun getCount(): Int = tabs.size

    override fun getPageTitle(position: Int): CharSequence = tabs[position].toUpperCase()
}
