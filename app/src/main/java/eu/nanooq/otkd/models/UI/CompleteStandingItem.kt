package eu.nanooq.otkd.models.UI

/**
 *
 * Created by rd on 04/08/2017.
 */
data class CompleteStandingItem(
        var mStanding: Int = 0,
        var mTeamName: String = "",
        var mTeamTime: String = "",
        var mCategoryStanding: Int = 0,
        var mIsYourTeam: Boolean = false
) : ItemType {
    override fun getViewType(): Int = CompleteStandingItemTypes.ITEM
}

class CompleteStandingHeader() : ItemType {
    override fun getViewType(): Int = CompleteStandingItemTypes.HEADER
}

interface ItemType {
    fun getViewType(): Int
}

class CompleteStandingItemTypes {
    companion object {
        const val HEADER = 0
        const val ITEM = 1
    }
}

data class SectionStandingItem(
        var mStanding: Int = 0,
        var mTeamName: String = "",
        var mTeamTime: Float = 0.0f,
        var mCompleteTime: Float = 0.0f,
        var mIsYourTeam: Boolean = false
) : ItemType {
    override fun getViewType(): Int = CompleteStandingItemTypes.ITEM
}