package eu.nanooq.otkd.models.UI

/**
 *
 * Created by rd on 03/08/2017.
 */
data class SectionResult(
        var mSectionId: Int = 0,
        var mSectionName: String = "",
        var mSectionTeamCount: Int = 0,
        var mSectionCompletedByYourTeam: Boolean = false
)