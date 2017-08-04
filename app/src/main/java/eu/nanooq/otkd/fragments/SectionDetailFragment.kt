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
import timber.log.Timber

/**
 *
 * Created by rd on 04/08/2017.
 */
class SectionDetailFragment : ViewModelFragment<IView, BaseViewModel<IView>>(), IView {

    companion object {
        fun newInstance(sectionId: Int): SectionDetailFragment {
            val args = Bundle()
            args.putInt("sectionId", sectionId)
            val frag = SectionDetailFragment()
            frag.arguments = args
            return frag // TODO change to SectionDetailFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.d("onCreateView()")
        super.onCreateView(inflater, container, savedInstanceState)
        val view = container?.inflate(R.layout.fragment_section_detail)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}
