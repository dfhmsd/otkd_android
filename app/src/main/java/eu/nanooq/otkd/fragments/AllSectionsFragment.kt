package eu.nanooq.otkd.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import eu.nanooq.otkd.R
import eu.nanooq.otkd.activities.SectionDetailActivity
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

    lateinit var mAdapter: SectionsRecAdapter

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

        vSectionsCount
        mAdapter = SectionsRecAdapter(ArrayList(), context) {
            onDetailItemClick(it)
        }

        return view
    }

    private fun onDetailItemClick(sectionItem: SectionItem) {
        //todo pass sectionID and runner name
//in your OnCreate() method

        val intent = Intent(context, SectionDetailActivity::class.java)
        intent.putExtra("item", Gson().toJson(sectionItem))
        startActivity(intent)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        Timber.d("onViewCreated()")
        super.onViewCreated(view, savedInstanceState)

        mAdapter.hasStableIds()
        vAllSectionsRecView.adapter = mAdapter
        vAllSectionsRecView.isNestedScrollingEnabled = false
        vAllSectionsRecView.layoutManager = LinearLayoutManager(context)
        vAllSectionsRecView.hasFixedSize()
    }

    override fun updateAdapter(newItems: ArrayList<SectionItem>) {
        Timber.d("updateAdapter() $newItems")
        vSectionsCount.text = "${newItems.size} úsekov"

        mAdapter.addItems(newItems)
    }
}
