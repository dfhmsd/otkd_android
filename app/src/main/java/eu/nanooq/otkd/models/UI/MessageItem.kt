package eu.nanooq.otkd.models.UI

/**
 *
 * Created by rd on 29/07/2017.
 */
data class MessageItem(
        var isCaptain: Boolean = false,
        var isUser: Boolean = false,
        var sender: String = "",
        var senderId: String = "",
        var text: String = "",
        var timestamp: Long = 0
)
