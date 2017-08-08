package eu.nanooq.otkd.activities

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import eu.nanooq.otkd.R
import eu.nanooq.otkd.activities.base.ViewModelActivity
import eu.nanooq.otkd.models.UI.UserDetailItem
import eu.nanooq.otkd.viewModels.userDetail.IUserDetailView
import eu.nanooq.otkd.viewModels.userDetail.UserDetailViewModel
import kotlinx.android.synthetic.main.activity_user_detail.*
import timber.log.Timber

/**
 *
 * Created by rd on 07/08/2017.
 */
class UserDetailActivity : ViewModelActivity<IUserDetailView, UserDetailViewModel>(), IUserDetailView {

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("onCreate()")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        setupToolbar()
    }

    override fun updateData(userDetail: UserDetailItem) {
        vBestTime.text = userDetail.mBestTime
        vTeamResult.text = userDetail.mTeamStanding
        vTeamResultInCategory.text = userDetail.mStandingInCategory
        vStandingOnSection.text = userDetail.mStandingInSection
        vExpectedTime.text = userDetail.mExpectedTime
        vRealTime.text = userDetail.mRealTime
        vWorstTime.text = userDetail.mWorstTime
        vTimeAtFinish.text = userDetail.mTimeAtFinish
        vTeamTime.text = userDetail.mTeamTime
    }

    override fun setToolbarTitle(title: String) {
        supportActionBar?.title = title
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = mToolbar as Toolbar
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        supportActionBar?.title = ""
    }
}