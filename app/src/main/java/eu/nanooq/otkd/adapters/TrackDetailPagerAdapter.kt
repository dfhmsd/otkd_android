package eu.nanooq.otkd.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import eu.nanooq.otkd.fragments.RunnerOnSectionDetailFragment
import eu.nanooq.otkd.fragments.SectionDetailFragment
import eu.nanooq.otkd.fragments.StandingsDetailFragment

/**
 *
 * Created by rd on 04/08/2017.
 */
class TrackDetailPagerAdapter(fm: FragmentManager, val sectionId: Int) : FragmentStatePagerAdapter(fm) {


    val STANDIGS = "PORADIE"
    val RUNNER_ON_SECTION = "BEŽEC NA ÚSEKU"
    val SECTION_DETAIL = "DETAIL ÚSEKU"
    //    val tabs: MutableList<String> = mutableListOf(FOR_RUNNERS, FOR_CARS)
    val tabs: MutableList<String> = mutableListOf(SECTION_DETAIL, RUNNER_ON_SECTION, STANDIGS)

    override fun getItem(position: Int): Fragment {
        return when (tabs[position]) {
            SECTION_DETAIL -> SectionDetailFragment.newInstance(sectionId)
            RUNNER_ON_SECTION -> RunnerOnSectionDetailFragment.newInstance(sectionId)
            STANDIGS -> StandingsDetailFragment.newInstance(sectionId)
            else -> SectionDetailFragment.newInstance(sectionId)
        }
    }

    override fun getCount(): Int = tabs.size


    override fun getPageTitle(position: Int): CharSequence = tabs[position]
}
