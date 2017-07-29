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
)

data class Team(
        var members: ArrayList<Member>? ,
        var team_number: Int?
)

data class Member(
        var birth_year: Int? = null,
        var time_per_10_km: String? = null,
        var car_number: String? = null,
        var email: String? = null,
        var first_name: String? = null,
        var is_captain: Boolean? = null,
        var last_name: String? = null,
        var order: Int? = null,
        var paid: Boolean? = null,
        var phone_number: String? = null,
        var sections: ArrayList<Int>? = null,
        var sex: String? = null,
        var t_shirt_size: String? = null,
        var user_photo: String? = null
)
