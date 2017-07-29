package eu.nanooq.otkd.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.nanooq.otkd.R
import eu.nanooq.otkd.fragments.base.ViewModelFragment
import eu.nanooq.otkd.inflate
import eu.nanooq.otkd.viewModels.main.sections.IUserSectionsView
import eu.nanooq.otkd.viewModels.main.sections.UserSectionsViewModel
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

    companion object {
        fun newInstance(): UserSectionsFragment {
            return UserSectionsFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.d("onCreateView()")
        super.onCreateView(inflater, container, savedInstanceState)
        val view = container?.inflate(R.layout.fragment_user_sections)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        Timber.d("onViewCreated()")

        super.onViewCreated(view, savedInstanceState)


    }
}
