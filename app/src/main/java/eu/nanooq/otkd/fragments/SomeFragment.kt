package eu.nanooq.otkd.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.inloop.viewmodel.IView
import eu.nanooq.otkd.R
import eu.nanooq.otkd.fragments.base.ViewModelFragment
import eu.nanooq.otkd.inflate
import eu.nanooq.otkd.viewModels.base.BaseViewModel
import eu.nanooq.otkd.viewModels.main.sections.IAllSectionsView
import eu.nanooq.otkd.viewModels.main.sections.AllSectionsViewModel
import timber.log.Timber

/**
 *
 * Created by rd on 28/07/2017.
 */
class SomeFragment : ViewModelFragment<IView, BaseViewModel<IView>>() , IView {

    companion object {
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


    }
}