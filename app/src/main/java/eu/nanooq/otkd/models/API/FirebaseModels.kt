package eu.nanooq.otkd.models.API


/**
 *
 * Created by rd on 29/07/2017.
 */
//Firebase models
data class Section(
        var description: String? = null,
        var description_for_car: String? = null,
        var down: Long? = null,
        var high: Long? = null,
        var hard: Long? = null,
        var id: Int? = null,
        var km: Float? = null,
        var route_profile: String? = null,
        var section_name: String? = null
) {

}

data class Team(
        var members: ArrayList<Member>?,
        var team_number: Int?
)


data class UserData(
        var captain_email: String? = null,
        var category: String? = null,
        var finish_datetime: String? = null,
        var start_datetime: String? = null,
        var team_id: Long? = null,
        var team_logo: String? = null,
        var team_name: String? = null,
        var team_number: Long? = null

)

data class TeamResult(
        var category: String? = null,
        var final_result: Int? = null,
        var final_result_in_category: Int? = null,
        var final_team_time: String? = null,
        var sections: ArrayList<TeamSectionResult>? = null,
        var team_id: Int? = null,
        var team_name: String? = null,
        var team_number: Int? = null,
        var time_from_start: Float? = null

)

data class TeamSectionResult(
        var best_time: Float? = 0.0f,
        var estimated_time: Float? = 0.0f,
        var real_time: Float? = 0.0f,
        var result_on_section: Int? = 0,
        var runner: String? = "",
        var section_id: Int? = 0,
        var section_name: String? = "",
        var time_when_run: String? = "",
        var worst_time: Float? = 0.0f
)
