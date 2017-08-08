package eu.nanooq.otkd.activities

import android.os.Bundle
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

    }

    override fun updateData(userDetail: UserDetailItem) {
        vBestTime.text = userDetail.mBestTime
        vFinalTeamStanding.text = userDetail.mTeamStanding
        vFinalStandingInCategory.text = userDetail.mStandingInCategory
        vStandingOnSection.text = userDetail.mStandingInSection
        vExpectedTime.text = userDetail.mExpectedTime
        vRealTime.text = userDetail.mRealTime
        vWorstTime.text = userDetail.mWorstTime
        vTimeAtFinish.text = userDetail.mTimeAtFinish
        vTeamTime.text = userDetail.mTeamTime
    }
}