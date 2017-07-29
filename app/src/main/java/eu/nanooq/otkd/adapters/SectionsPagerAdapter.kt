package eu.nanooq.otkd.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import eu.nanooq.otkd.fragments.AllSectionsFragment
import eu.nanooq.otkd.fragments.SectionsFragment
import eu.nanooq.otkd.fragments.UserSectionsFragment

/**
 *
 * Created by rd on 28/07/2017.
 */
class SectionsPagerAdapter( fm: FragmentManager) : FragmentStatePagerAdapter(fm) {


    val tabs: MutableList<String> = mutableListOf( SectionsFragment.ALL_SECTION_TAB, SectionsFragment.USER_SECTION_TAB)

    override fun getItem(position: Int): Fragment {
        return if (tabs[position] == SectionsFragment.ALL_SECTION_TAB) {
            AllSectionsFragment.newInstance()
        } else {
            UserSectionsFragment.newInstance()
        }
    }

    override fun getCount(): Int = tabs.size


    override fun getPageTitle(position: Int): CharSequence = tabs[position]
}