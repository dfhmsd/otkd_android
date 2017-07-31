package eu.nanooq.otkd.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.nanooq.otkd.R
import eu.nanooq.otkd.activities.SplashActivity
import eu.nanooq.otkd.fragments.base.ViewModelFragment
import eu.nanooq.otkd.inflate
import eu.nanooq.otkd.viewModels.userProfile.IUserProfileView
import eu.nanooq.otkd.viewModels.userProfile.UserProfileViewModel
import kotlinx.android.synthetic.main.fragment_user_profile.*
import timber.log.Timber

/**
 *
 * Created by rd on 31/07/2017.
 */
class UserProfileFragment : ViewModelFragment<IUserProfileView, UserProfileViewModel>(), IUserProfileView {

    companion object {
        fun newInstance(): UserProfileFragment {
            return UserProfileFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.d("onCreateView()")
        super.onCreateView(inflater, container, savedInstanceState)
        val view = container?.inflate(R.layout.fragment_user_profile)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        vLogoff.setOnClickListener {
            viewModel.logoff()
        }

    }

    override fun logout() {
        startActivity(Intent(context, SplashActivity::class.java))
        activity.finish()
    }
}
