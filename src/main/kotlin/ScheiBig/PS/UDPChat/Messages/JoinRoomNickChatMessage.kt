package ScheiBig.PS.UDPChat.Messages

/**
 * Object representing message sent when [nickname] joins [roomname]
 * @property roomname Name of chatroom that user wants to join
 * @property nickname Nickname of user joining into chatroom
 * @constructor Creates message with specified [roomname] and [nickname]
 */
internal class JoinRoomNickChatMessage(override val roomname: String,
                              override val nickname: String) : RoomNickChatMessage() {
    /**
     * Returns a string representation of the message
     * @return "JOIN [roomname] [nickname]"
     */
    override fun toString(): String = "JOIN ${super.toString()}"
}