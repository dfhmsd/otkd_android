package eu.nanooq.otkd.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.nanooq.otkd.R
import eu.nanooq.otkd.adapters.SectionsPagerAdapter
import eu.nanooq.otkd.fragments.base.ViewModelFragment
import eu.nanooq.otkd.inflate
import eu.nanooq.otkd.viewModels.IActivityToolbar
import eu.nanooq.otkd.viewModels.main.sections.ISectionsView
import eu.nanooq.otkd.viewModels.main.sections.SectionsViewModel
import kotlinx.android.synthetic.main.fragment_sections.*
import timber.log.Timber

/**
 *
 * Created by rd on 23/07/2017.
 */
class SectionsFragment : ViewModelFragment<ISectionsView, SectionsViewModel>() , ISectionsView {

    lateinit var mToolbarCallback: IActivityToolbar
    companion object {

        const val ALL_SECTION_TAB = "VŠETKY ÚSEKY"
        const val USER_SECTION_TAB = "MOJE ÚSEKY"

        fun newInstance(): SectionsFragment {
            return SectionsFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.d("onCreateView()")
        super.onCreateView(inflater, container, savedInstanceState)
        val view = container?.inflate(R.layout.fragment_sections)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mToolbarCallback = activity as IActivityToolbar

        vSectionsPager.adapter = SectionsPagerAdapter(childFragmentManager)
        vSectionTabs.setupWithViewPager(vSectionsPager)

        mToolbarCallback.onToolbarTitleChange(getString(R.string.sections_toolbar_title))

    }
}
