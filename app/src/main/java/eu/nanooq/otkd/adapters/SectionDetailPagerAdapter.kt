package eu.nanooq.otkd.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import eu.nanooq.otkd.fragments.RunnerSectionDetailFragment

/**
 *
 * Created by rd on 31/07/2017.
 */
class SectionDetailPagerAdapter(val sectionJson: String, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {


    val FOR_RUNNERS = "PRE BEŽCA"
    val FOR_CARS = "PRE VOZIDLA"
    //    val tabs: MutableList<String> = mutableListOf(FOR_RUNNERS, FOR_CARS)
    val tabs: MutableList<String> = mutableListOf(FOR_RUNNERS)

    override fun getItem(position: Int): Fragment = RunnerSectionDetailFragment.newInstance(sectionJson)

//    {
//        return if (tabs[position] == FOR_RUNNERS) {
//            RunnerSectionDetailFragment.newInstance(sectionJson)
//        } else {
//            CarsSectionDetailFragment.newInstance() //TODO CarsSectionDetailFragment
//        }
//    }

    override fun getCount(): Int = tabs.size


    override fun getPageTitle(position: Int): CharSequence = tabs[position]
}
