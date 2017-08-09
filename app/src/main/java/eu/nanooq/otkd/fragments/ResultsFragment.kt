package eu.nanooq.otkd.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.inloop.viewmodel.IView
import eu.nanooq.otkd.R
import eu.nanooq.otkd.adapters.ResultsPagerAdapter
import eu.nanooq.otkd.fragments.base.ViewModelFragment
import eu.nanooq.otkd.inflate
import eu.nanooq.otkd.viewModels.base.BaseViewModel
import kotlinx.android.synthetic.main.fragment_results.*
import timber.log.Timber

/**
 *
 * Created by rd on 31/07/2017.
 */
class ResultsFragment : ViewModelFragment<IView, BaseViewModel<IView>>(), IView {

    companion object {
        const val PER_SECTIONS = "Po úsekoch"
        const val STANDINGS = "Celkové poradie"

        fun newInstance(): ResultsFragment {
            return ResultsFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.d("onCreateView()")
        super.onCreateView(inflater, container, savedInstanceState)
        val view = container?.inflate(R.layout.fragment_results)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        vResultsPager.adapter = ResultsPagerAdapter(childFragmentManager)
        vResultsTabs.setupWithViewPager(vResultsPager)

//        val mToolbarCallback = activity as IActivityToolbar
//        mToolbarCallback.onToolbarTitleChange(getString(R.string.results_toolbar_title))
    }
}
