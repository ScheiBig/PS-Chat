package ScheiBig.PS.UDPChat.Messages

/**
 * Object representing message sent when [nickname] leaves [roomname]
 * @property roomname Name of chatroom that user sends message to
 * @property nickname Nickname of user sending the message
 * @property message Message that is being send
 * @constructor Creates message with specified [roomname], [nickname] and [message]
 */
internal class MessageRoomNickChatMessage(override val roomname: String,
                                 override val nickname: String,
                                 val message: String) : RoomNickChatMessage() {
    /**
     * Returns a string representation of the message
     * @return "MSG [roomname] [nickname] [message]"
     */
    override fun toString(): String = "MSG ${super.toString()} $message"
}