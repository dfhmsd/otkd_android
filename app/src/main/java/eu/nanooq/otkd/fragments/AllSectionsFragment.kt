package eu.nanooq.otkd.fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.nanooq.otkd.R
import eu.nanooq.otkd.adapters.SectionsRecAdapter
import eu.nanooq.otkd.fragments.base.ViewModelFragment
import eu.nanooq.otkd.inflate
import eu.nanooq.otkd.models.UI.SectionItem
import eu.nanooq.otkd.viewModels.main.sections.AllSectionsViewModel
import eu.nanooq.otkd.viewModels.main.sections.IAllSectionsView
import kotlinx.android.synthetic.main.fragment_all_sections.*
import timber.log.Timber

/**
 *
 * Created by rd on 28/07/2017.
 */
class AllSectionsFragment : ViewModelFragment<IAllSectionsView, AllSectionsViewModel>() , IAllSectionsView {

    var mAdapter = SectionsRecAdapter(ArrayList())

    companion object {
        fun newInstance(): AllSectionsFragment {
            Timber.d("newInstance()")

            return AllSectionsFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.d("onCreateView()")
        super.onCreateView(inflater, container, savedInstanceState)
        val view = container?.inflate(R.layout.fragment_all_sections)




        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        Timber.d("onViewCreated()")
        super.onViewCreated(view, savedInstanceState)

        mAdapter.hasStableIds()
        vAllSectionsRecView.adapter = mAdapter
        vAllSectionsRecView.layoutManager = LinearLayoutManager(context)
        vAllSectionsRecView.hasFixedSize()
    }

    override fun updateAdapter(newItems: ArrayList<SectionItem>) {
        Timber.d("updateAdapter() $newItems")
        vSectionsCount.text = newItems.size.toString()

        mAdapter.addItems(newItems)
    }
}
