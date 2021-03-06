package eu.nanooq.otkd.fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.nanooq.otkd.R
import eu.nanooq.otkd.adapters.CompleteStandingRecAdapter
import eu.nanooq.otkd.fragments.base.ViewModelFragment
import eu.nanooq.otkd.inflate
import eu.nanooq.otkd.models.UI.ItemType
import eu.nanooq.otkd.viewModels.main.results.IResultCompleteStandingView
import eu.nanooq.otkd.viewModels.main.results.ResultCompleteStandingViewModel
import kotlinx.android.synthetic.main.fragment_complete_standing.*
import timber.log.Timber

/**
 *
 * Created by rd on 03/08/2017.
 */
class ResultCompleteStandingFragment : ViewModelFragment<IResultCompleteStandingView, ResultCompleteStandingViewModel>(), IResultCompleteStandingView {

    lateinit var mAdapter: CompleteStandingRecAdapter
    companion object {
        fun newInstance(): ResultCompleteStandingFragment {
            return ResultCompleteStandingFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.d("onCreateView()")
        super.onCreateView(inflater, container, savedInstanceState)
        val view = container?.inflate(R.layout.fragment_complete_standing)

        mAdapter = CompleteStandingRecAdapter(ArrayList())

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        Timber.d("onViewCreated()")
        super.onViewCreated(view, savedInstanceState)
        mAdapter.hasStableIds()
        vStandingsRecView.adapter = mAdapter
        vStandingsRecView.layoutManager = LinearLayoutManager(context)

    }

    override fun updateAdapter(newItems: ArrayList<ItemType>) {
        Timber.d("updateAdapter()")

        mAdapter.addItems(newItems)
    }

    override fun updateTeamsStats(finishedCount: Int, runningCount: Int) {
        vTeamsFineshedCount.text = "Dobehli: $finishedCount"
        vTeamsRunningCount.text = "Beží: $runningCount"
    }
}
