package eu.nanooq.otkd.models.API


/**
 *
 * Created by rd on 27/07/2017.
 */
data class UserCaptain(
        var email: String = "",
        var password: String = ""
)
data class UserRunner(
        var email: String = "",
        var first_name: String = "",
        var last_name: String = "",
        var team_name: String = ""
)
