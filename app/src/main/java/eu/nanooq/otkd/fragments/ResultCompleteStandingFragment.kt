package eu.nanooq.otkd.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.nanooq.otkd.R
import eu.nanooq.otkd.fragments.base.ViewModelFragment
import eu.nanooq.otkd.inflate
import eu.nanooq.otkd.viewModels.main.results.IResultCompleteStandingView
import eu.nanooq.otkd.viewModels.main.results.ResultCompleteStandingViewModel
import timber.log.Timber

/**
 *
 * Created by rd on 03/08/2017.
 */
class ResultCompleteStandingFragment : ViewModelFragment<IResultCompleteStandingView, ResultCompleteStandingViewModel>(), IResultCompleteStandingView {

    companion object {
        fun newInstance(): ResultCompleteStandingFragment {
            return ResultCompleteStandingFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.d("onCreateView()")
        super.onCreateView(inflater, container, savedInstanceState)
        val view = container?.inflate(R.layout.fragment_complete_standing)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}
