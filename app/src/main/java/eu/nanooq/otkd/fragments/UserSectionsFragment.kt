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
import eu.nanooq.otkd.viewModels.main.sections.IUserSectionsView
import eu.nanooq.otkd.viewModels.main.sections.UserSectionsViewModel
import kotlinx.android.synthetic.main.fragment_user_sections.*
import timber.log.Timber

/**
 *
 * Created by rd on 29/07/2017.
 */

/**
 *
 * Created by rd on 28/07/2017.
 */
class UserSectionsFragment : ViewModelFragment<IUserSectionsView, UserSectionsViewModel>() , IUserSectionsView {

    lateinit var mAdapter: SectionsRecAdapter

    companion object {
        fun newInstance(): UserSectionsFragment {
            return UserSectionsFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.d("onCreateView()")
        super.onCreateView(inflater, container, savedInstanceState)
        val view = container?.inflate(R.layout.fragment_user_sections)

        mAdapter = SectionsRecAdapter(ArrayList(), context) {
            onDetailItemClick(it)
        }

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        Timber.d("onViewCreated()")
        super.onViewCreated(view, savedInstanceState)

        mAdapter.hasStableIds()
        vUserSectionsRecView.adapter = mAdapter
        vUserSectionsRecView.layoutManager = LinearLayoutManager(context)
        vUserSectionsRecView.hasFixedSize()


    }

    override fun updateAdapter(list: ArrayList<SectionItem>) {
        Timber.d("updateAdapter() $list")
        vUserSectionsCount.text = "${list.size} úsekov"
        mAdapter.addItems(list)
    }

    private fun onDetailItemClick(sectionItem: SectionItem) {
        //todo pass sectionID and runner name
        val intent = Intent(context, SectionDetailActivity::class.java)
        intent.putExtra("item", Gson().toJson(sectionItem))
        startActivity(intent)
    }
}
