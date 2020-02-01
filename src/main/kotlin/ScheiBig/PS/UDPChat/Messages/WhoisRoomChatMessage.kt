package ScheiBig.PS.UDPChat.Messages

/**
 * Object representing message sent when user checks for who is in [roomname]
 * @property roomname Name of chatroom that is being checked
 * @constructor Creates message with specified [roomname]
 */
internal class WhoisRoomChatMessage(val roomname: String): ChatMessage() {
    /**
     * Returns a string representation of the message
     * @return "WHOIS [roomname]"
     */
    override fun toString(): String = "WHOIS $roomname"
}