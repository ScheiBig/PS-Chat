package ScheiBig.PS.UDPChat.Messages

/**
 * Object representing message sent when [nickname] leaves [roomname]
 * @property roomname Name of chatroom that user wants to leave
 * @property nickname Nickname of user leaving the chatroom
 * @constructor Creates message with specified [roomname] and [nickname]
 */
internal class LeaveRoomNickChatMessage(roomname: String,
                               nickname: String) : RoomNickChatMessage(roomname, nickname) {
    /**
     * Returns a string representation of the message
     * @return "LEAVE [roomname] [nickname]"
     */
    override fun toString(): String = "LEAVE ${super.toString()}"
}