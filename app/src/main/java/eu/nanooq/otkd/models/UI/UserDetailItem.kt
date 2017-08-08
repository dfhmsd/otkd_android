package eu.nanooq.otkd.models.UI

/**
 *
 * Created by rd on 07/08/2017.
 */
data class UserDetailItem(
        var mBestTime: String = "",
        var mTeamStanding: String = "",
        var mStandingInCategory: String = "",
        var mStandingInSection: String = "",
        var mExpectedTime: String = "",
        var mRealTime: String = "",
        var mWorstTime: String = "",
        var mTimeAtFinish: String = "",
        var mTeamTime: String = ""
)
