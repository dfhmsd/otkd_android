package eu.nanooq.otkd.models.UI

/**
 *
 * Created by rd on 29/07/2017.
 */
data class SectionItem(
        var Id: Int = 0,
        var name: String = "",
        var length: Float = 0.0f,
        var difficulty: Long = 0,
        var crossFall: String = "",
        var runnerName: String = "",
        var runnerImgUrl: String = ""
)
