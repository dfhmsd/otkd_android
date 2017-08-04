package eu.nanooq.otkd.fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.nanooq.otkd.R
import eu.nanooq.otkd.adapters.SectionStandingsRecAdapter
import eu.nanooq.otkd.fragments.base.ViewModelFragment
import eu.nanooq.otkd.inflate
import eu.nanooq.otkd.models.UI.ItemType
import eu.nanooq.otkd.viewModels.IActivityToolbar
import eu.nanooq.otkd.viewModels.sectionDetail.IStandingsDetailView
import eu.nanooq.otkd.viewModels.sectionDetail.StandingDetailViewModel
import kotlinx.android.synthetic.main.fragment_standings_detail.*
import timber.log.Timber

/**
 *
 * Created by rd on 04/08/2017.
 */
class StandingsDetailFragment : ViewModelFragment<IStandingsDetailView, StandingDetailViewModel>(), IStandingsDetailView {

    lateinit var mAdapter: SectionStandingsRecAdapter

    companion object {
        fun newInstance(sectionId: Int): StandingsDetailFragment {

            val args = Bundle()
            args.putInt("sectionId", sectionId)
            val frag = StandingsDetailFragment()
            frag.arguments = args
            return frag
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.d("onCreateView()")
        super.onCreateView(inflater, container, savedInstanceState)
        val view = container?.inflate(R.layout.fragment_standings_detail)

        mAdapter = SectionStandingsRecAdapter(ArrayList())

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        Timber.d("onViewCreated()")
        super.onViewCreated(view, savedInstanceState)

        mAdapter.hasStableIds()
        vSectionStandingsRecView.adapter = mAdapter
        vSectionStandingsRecView.layoutManager = LinearLayoutManager(context)

    }

    override fun updateAdapter(newItems: ArrayList<ItemType>) {
        Timber.d("updateAdapter()")

        mAdapter.addItems(newItems)
    }

    override fun updateTeamsStats(finishedCount: Int, runningCount: Int) {
        vTeamsFineshedCount.text = "Dobehli: $finishedCount"
        vTeamsRunningCount.text = "Beží: $runningCount"
    }

    override fun setToolbarTitle(title: String) {
        val activity = activity as IActivityToolbar
        activity.onToolbarTitleChange(title)
    }
}
