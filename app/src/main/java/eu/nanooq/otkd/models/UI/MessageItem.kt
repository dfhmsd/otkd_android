package eu.nanooq.otkd.models.UI

/**
 *
 * Created by rd on 29/07/2017.
 */
data class MessageItem(
        var captain: Boolean = false,
        var sender: String = "",
        var senderId: String = "",
        var text: String = "",
        var timestamp: Long = 0
)
